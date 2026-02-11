package com.example.team5_be.missionlog.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.team5_be.mission.dao.MissionRepository;
import com.example.team5_be.mission.domain.entity.MissionEntity;
import com.example.team5_be.missionlog.dao.MissionLogRepository;
import com.example.team5_be.missionlog.domain.dto.CalendarMonthResponseDTO;
import com.example.team5_be.missionlog.domain.dto.DailyMissionItemDTO;
import com.example.team5_be.missionlog.domain.dto.DailyMissionListResponseDTO;
import com.example.team5_be.missionlog.domain.dto.MissionLogRequestDTO;
import com.example.team5_be.missionlog.domain.dto.MissionLogResponseDTO;
import com.example.team5_be.missionlog.domain.entity.MissionLogEntity;
import com.example.team5_be.status.dao.StatusRepository;
import com.example.team5_be.status.domain.entity.StatusEntity;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MissionLogService {
    private static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    private static final String STATUS_COMPLETED_NAME = "완료";
    private static final String MODE_LEVEL_UP_NAME = "레벨업";
    private static final String MODE_SELF_SELECT_NAME = "자율 선택";

    private final MissionLogRepository missionLogRepository;
    private final MissionRepository missionRepository;
    private final StatusRepository statusRepository;



    public MissionLogResponseDTO upsert(MissionLogRequestDTO request) {
        MissionEntity mission = missionRepository.findById(request.getMissionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid missionId: " + request.getMissionId()));
        validateCheckDate(request, mission);

        MissionLogEntity entity = missionLogRepository
                .findByMission_MissionIdAndCheckDate(request.getMissionId(), request.getCheckDate())
                .orElseGet(() -> MissionLogEntity.builder()
                        .mission(mission)
                        .checkDate(request.getCheckDate())
                        .build());

        entity.setIsChecked(request.getIsChecked());

        MissionLogEntity saved = missionLogRepository.save(entity);

        if (Boolean.TRUE.equals(saved.getIsChecked())
                && isCompletionTargetMode(mission)
                && !saved.getCheckDate().isBefore(mission.getMissionEndDate())) {
            markMissionCompleted(mission);
        }

        return MissionLogResponseDTO.fromEntity(saved);
    }


    private void validateCheckDate(MissionLogRequestDTO request, MissionEntity mission) {
        LocalDate checkDate = request.getCheckDate();
        if (checkDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "checkDate is required");
        }

        LocalDate startDate = mission.getMissionStartDate();
        LocalDate endDate = mission.getMissionEndDate();

        if (checkDate.isBefore(startDate) || checkDate.isAfter(endDate)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "checkDate is outside mission period");
        }
    }



    private boolean isCompletionTargetMode(MissionEntity mission) {
        String modeName = mission.getMode().getModeName();
        return MODE_LEVEL_UP_NAME.equals(modeName) || MODE_SELF_SELECT_NAME.equals(modeName);
    }



    private void markMissionCompleted(MissionEntity mission) {
        StatusEntity completed = statusRepository.findByStatusName(STATUS_COMPLETED_NAME)
                .orElseThrow(() -> new IllegalArgumentException("Invalid statusName: " + STATUS_COMPLETED_NAME));

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
                .missionEndDate(mission.getMissionEndDate())
                .comments(mission.getComments())
                .build();

        missionRepository.save(updated);
    }



    public CalendarMonthResponseDTO getMonthStamps(String userId, String month) {
        YearMonth yearMonth = YearMonth.parse(month, YEAR_MONTH_FORMATTER);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<MissionEntity> missions = missionRepository
                .findByUser_UserIdAndMissionStartDateLessThanEqualAndMissionEndDateGreaterThanEqual(userId, endDate, startDate);

        List<LocalDate> activeDates = new ArrayList<>();
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            LocalDate currentDate = current;
            List<MissionEntity> activeMissions = missions.stream()
                    .filter(mission -> !currentDate.isBefore(mission.getMissionStartDate())
                            && !currentDate.isAfter(mission.getMissionEndDate()))
                    .toList();

            if (!activeMissions.isEmpty()) {
                activeDates.add(currentDate);
            }

            current = current.plusDays(1);
        }

        return CalendarMonthResponseDTO.builder()
                .month(yearMonth.format(YEAR_MONTH_FORMATTER))
                .activeDates(activeDates)
                .build();
    }



    public DailyMissionListResponseDTO getDailyMissions(String userId, LocalDate date) {
        List<MissionEntity> missions = missionRepository
                .findByUser_UserIdAndMissionStartDateLessThanEqualAndMissionEndDateGreaterThanEqual(userId, date, date);

        List<Integer> missionIds = missions.stream()
                .map(MissionEntity::getMissionId)
                .toList();

        if (missionIds.isEmpty()) {
            return DailyMissionListResponseDTO.builder()
                    .date(date.toString())
                    .missions(List.of())
                    .build();
        }

        Map<Integer, MissionLogEntity> logsByMissionId = missionLogRepository
                .findByMission_MissionIdInAndCheckDate(missionIds, date).stream()
                .collect(Collectors.toMap(
                        log -> log.getMission().getMissionId(),
                        Function.identity(),
                        (left, right) -> left));

        List<DailyMissionItemDTO> items = missions.stream()
                .map(mission -> DailyMissionItemDTO.builder()
                        .missionId(mission.getMissionId())
                        .missionName(mission.getMissionName())
                        .success(resolveSuccess(logsByMissionId.get(mission.getMissionId())))
                        .build())
                .toList();

        return DailyMissionListResponseDTO.builder()
                .date(date.toString())
                .missions(items)
                .build();
    }


    
    private Boolean resolveSuccess(MissionLogEntity log) {
        return log == null ? null : log.getIsChecked();
    }
}