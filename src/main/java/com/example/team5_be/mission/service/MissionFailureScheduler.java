package com.example.team5_be.mission.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.team5_be.habit.dao.HabitRelationshipRepository;
import com.example.team5_be.habit.domain.dto.HabitStatus;
import com.example.team5_be.habit.domain.entity.HabitRelationshipEntity;
import com.example.team5_be.level.dao.LevelRepository;
import com.example.team5_be.level.domain.entity.LevelEntity;
import com.example.team5_be.mission.dao.MissionRepository;
import com.example.team5_be.mission.domain.entity.MissionEntity;
import com.example.team5_be.missionlog.dao.MissionLogRepository;
import com.example.team5_be.missionlog.domain.entity.MissionLogEntity;
import com.example.team5_be.status.dao.StatusRepository;
import com.example.team5_be.status.domain.entity.StatusEntity;
import com.example.team5_be.trophy.service.TrophyService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MissionFailureScheduler {
    private static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");
    private static final String STATUS_IN_PROGRESS_NAME = "진행중";
    private static final String STATUS_COMPLETED_NAME = "완료";
    private static final String MODE_LEVEL_UP_NAME = "레벨업";
    private static final String MODE_SELF_SELECT_NAME = "자율 선택";
    private static final int STATUS_FAILED_ID = 4;

    private final MissionRepository missionRepository;
    private final MissionLogRepository missionLogRepository;
    private final StatusRepository statusRepository;
    private final LevelRepository levelRepository;
    private final HabitRelationshipRepository habitRelationshipRepository;
    private final TrophyService trophyService;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void processMissionFailures() {
        LocalDate today = LocalDate.now(KOREA_ZONE);
        LocalDate targetDate = today.minusDays(1);

        StatusEntity inProgress = statusRepository.findByStatusName(STATUS_IN_PROGRESS_NAME)
                .orElseThrow(() -> new IllegalArgumentException("Invalid statusName: " + STATUS_IN_PROGRESS_NAME));

        StatusEntity completed = statusRepository.findByStatusName(STATUS_COMPLETED_NAME)
            .orElseThrow(() -> new IllegalArgumentException("Invalid statusName: " + STATUS_COMPLETED_NAME));

        StatusEntity failed = statusRepository.findById(STATUS_FAILED_ID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid statusId: " + STATUS_FAILED_ID));

        List<MissionEntity> candidates = missionRepository
                .findByStatus_StatusNameAndMissionStartDateLessThanEqualAndMissionEndDateGreaterThanEqual(
                        STATUS_IN_PROGRESS_NAME,
                        targetDate,
                        targetDate);

        for (MissionEntity mission : candidates) {
            if (targetDate.equals(mission.getMissionEndDate())) {
                if (allDaysChecked(mission)) {
                    if (MODE_LEVEL_UP_NAME.equals(mission.getMode().getModeName())) {
                        applyLevelUpUpgrade(mission, inProgress, completed, today, targetDate);
                    } else if (MODE_SELF_SELECT_NAME.equals(mission.getMode().getModeName())) {
                        applySelfSelectCompletion(mission, completed, targetDate);
                    }
                    continue;
                }

                if (MODE_LEVEL_UP_NAME.equals(mission.getMode().getModeName())) {
                    applyLevelUpFailure(mission, inProgress, failed, today, targetDate);
                } else if (MODE_SELF_SELECT_NAME.equals(mission.getMode().getModeName())) {
                    applySelfSelectFailure(mission, failed, targetDate);
                }
                continue;
            }

            MissionLogEntity log = missionLogRepository
                    .findByMission_MissionIdAndCheckDate(mission.getMissionId(), targetDate)
                    .orElse(null);

            if (log != null && Boolean.TRUE.equals(log.getIsChecked())) {
                continue;
            }

            if (MODE_LEVEL_UP_NAME.equals(mission.getMode().getModeName())) {
                applyLevelUpFailure(mission, inProgress, failed, today, targetDate);
            } else if (MODE_SELF_SELECT_NAME.equals(mission.getMode().getModeName())) {
                applySelfSelectFailure(mission, failed, targetDate);
            }
        }
    }

    private void applyLevelUpFailure(
            MissionEntity mission,
            StatusEntity inProgress,
            StatusEntity failed,
            LocalDate today,
            LocalDate targetDate) {
        Integer currentLevelId = mission.getLevel().getLevelId();
        if (currentLevelId <= 1) {
            applySelfSelectFailure(mission, failed, targetDate);
            return;
        }

        int downgradedLevelId = currentLevelId - 1;

        LevelEntity downgradedLevel = levelRepository.findById(downgradedLevelId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid levelId: " + downgradedLevelId));

        LocalDate newStartDate = today;
        LocalDate newEndDate = newStartDate.plusDays(downgradedLevel.getLevelDate());

        MissionEntity updated = MissionEntity.builder()
                .missionId(mission.getMissionId())
                .user(mission.getUser())
                .habit(mission.getHabit())
                .mode(mission.getMode())
                .level(downgradedLevel)
                .status(inProgress)
                .missionName(mission.getMissionName())
                .missionDefinition(mission.getMissionDefinition())
                .missionStartDate(newStartDate)
                .missionEndDate(newEndDate)
                .comments(mission.getComments())
                .build();

        missionRepository.save(updated);
    }

    private void applySelfSelectFailure(MissionEntity mission, StatusEntity failed, LocalDate targetDate) {
        MissionEntity updated = MissionEntity.builder()
                .missionId(mission.getMissionId())
                .user(mission.getUser())
                .habit(mission.getHabit())
                .mode(mission.getMode())
                .level(mission.getLevel())
                .status(failed)
                .missionName(mission.getMissionName())
                .missionDefinition(mission.getMissionDefinition())
                .missionStartDate(mission.getMissionStartDate())
                .missionEndDate(targetDate)
                .comments(mission.getComments())
                .build();

        missionRepository.save(updated);
    }


    private void applySelfSelectCompletion(MissionEntity mission, StatusEntity completed, LocalDate targetDate) {
        MissionEntity updated = MissionEntity.builder()
                .missionId(mission.getMissionId())
                .user(mission.getUser())
                .habit(mission.getHabit())
                .mode(mission.getMode())
                .level(mission.getLevel())
                .status(completed)
                .missionName(mission.getMissionName())
                .missionDefinition(mission.getMissionDefinition())
                .missionStartDate(mission.getMissionStartDate())
                .missionEndDate(targetDate)
                .comments(mission.getComments())
                .build();

        missionRepository.save(updated);
        awardTrophyForMission(mission);
    }


    private void applyLevelUpUpgrade(
            MissionEntity mission,
            StatusEntity inProgress,
            StatusEntity completed,
            LocalDate today,
            LocalDate targetDate) {
        Integer currentLevelId = mission.getLevel().getLevelId();
        int maxLevelId = resolveMaxLevelId();

        if (currentLevelId >= maxLevelId) {
            MissionEntity updated = MissionEntity.builder()
                    .missionId(mission.getMissionId())
                    .user(mission.getUser())
                    .habit(mission.getHabit())
                    .mode(mission.getMode())
                    .level(mission.getLevel())
                    .status(completed)
                    .missionName(mission.getMissionName())
                    .missionDefinition(mission.getMissionDefinition())
                    .missionStartDate(mission.getMissionStartDate())
                    .missionEndDate(targetDate)
                    .comments(mission.getComments())
                    .build();

            missionRepository.save(updated);
            awardTrophyForMission(mission);
            return;
        }

        int upgradedLevelId = currentLevelId + 1;
        LevelEntity upgradedLevel = levelRepository.findById(upgradedLevelId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid levelId: " + upgradedLevelId));

        LocalDate newStartDate = today;
        LocalDate newEndDate = newStartDate.plusDays(upgradedLevel.getLevelDate());

        MissionEntity updated = MissionEntity.builder()
                .missionId(mission.getMissionId())
                .user(mission.getUser())
                .habit(mission.getHabit())
                .mode(mission.getMode())
                .level(upgradedLevel)
                .status(inProgress)
                .missionName(mission.getMissionName())
                .missionDefinition(mission.getMissionDefinition())
                .missionStartDate(newStartDate)
                .missionEndDate(newEndDate)
                .comments(mission.getComments())
                .build();

        missionRepository.save(updated);
    }


    private boolean allDaysChecked(MissionEntity mission) {
        LocalDate startDate = mission.getMissionStartDate();
        LocalDate endDate = mission.getMissionEndDate();
        long expectedDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        long checkedDays = missionLogRepository
                .countByMission_MissionIdAndCheckDateBetweenAndIsCheckedTrue(
                        mission.getMissionId(),
                        startDate,
                        endDate);

        return checkedDays == expectedDays;
    }


    private int resolveMaxLevelId() {
        return levelRepository.findTopByOrderByLevelIdDesc()
                .map(LevelEntity::getLevelId)
                .orElseThrow(() -> new IllegalArgumentException("Level data is missing"));
    }


    private void awardTrophyForMission(MissionEntity mission) {
        HabitRelationshipEntity rel = habitRelationshipRepository
                .findById_UserIdAndId_HabitId(
                        mission.getUser().getUserId(),
                        mission.getHabit().getHabitId())
                .orElse(null);

        if (rel == null) {
            return;
        }

        StatusEntity completedStatus = statusRepository
                .findByStatusName(HabitStatus.COMPLETED.getValue())
                .orElseThrow(() -> new IllegalArgumentException("Invalid statusName: " + HabitStatus.COMPLETED.getValue()));

        rel.changeStatus(completedStatus);
        habitRelationshipRepository.save(rel);

        trophyService.awardTrophy(mission.getUser().getUserId(), mission.getHabit().getHabitId());
    }
}
