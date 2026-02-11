package com.example.team5_be.mypage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.example.team5_be.habit.domain.entity.HabitEntity;
import com.example.team5_be.habit.domain.entity.TagEntity;
import com.example.team5_be.mission.dao.MissionRepository;
import com.example.team5_be.mission.domain.entity.MissionEntity;
import com.example.team5_be.missionlog.dao.MissionLogRepository;
import com.example.team5_be.missionlog.domain.entity.MissionLogEntity;
import com.example.team5_be.mypage.domain.dto.HabitProgressDTO;
import com.example.team5_be.mypage.domain.dto.ReportDTO;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private final MissionRepository missionRepo;
    private final MissionLogRepository missionLogRepo;

    public ReportDTO getReport(String userId, YearMonth month) {
        LocalDate today = LocalDate.now();

        // ---------------------
        // 1️⃣ 전체 Mission 조회 (Habit별 그룹핑)
        // ---------------------
        List<MissionEntity> allMissions = missionRepo.findByUser_UserId(userId);

        Map<Integer, List<MissionEntity>> habitMissionMap = allMissions.stream()
                .collect(Collectors.groupingBy(m -> m.getHabit().getHabitId()));

        // ---------------------
        // 2️⃣ 이번 달 MissionLog 조회
        // ---------------------
        LocalDate monthStart = month.atDay(1);
        LocalDate monthEnd = month.atEndOfMonth();

        List<Integer> missionIds = allMissions.stream()
                .map(MissionEntity::getMissionId)
                .toList();

        List<MissionLogEntity> monthLogs = missionLogRepo
                .findByMission_MissionIdInAndCheckDateBetween(missionIds, monthStart, monthEnd)
                .stream()
                .filter(MissionLogEntity::getIsChecked)
                .toList();

        // ---------------------
        // 3️⃣ 이번 달 태그별 진행 Mission 수
        // ---------------------
        Map<String, Long> monthTagCounts = monthLogs.stream()
                .map(log -> log.getMission().getHabit())
                .filter(Objects::nonNull)
                .map(HabitEntity::getTag)
                .filter(Objects::nonNull)
                .map(TagEntity::getTagName)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // ---------------------
        // 4️⃣ 총 완료 Mission 수
        // ---------------------
        long completedMissions = allMissions.stream()
                .filter(m -> "COMPLETED".equals(m.getStatus().getStatusName()))
                .count();

        // ---------------------
        // 5️⃣ Habit별 진행률 계산 (순차 진행)
        // ---------------------
        List<HabitProgressDTO> habitProgressList = habitMissionMap.entrySet().stream()
        .map(entry -> {
            Integer habitId = entry.getKey();
            List<MissionEntity> missions = entry.getValue();
            missions.sort(Comparator.comparing(MissionEntity::getMissionStartDate));

            // 완료된 단계 수
            long completedStages = missions.stream()
                    .filter(m -> "COMPLETED".equals(m.getStatus().getStatusName()))
                    .count();

            // 진행 중인 단계가 있으면 +0 (아직 완료 안 됨)
            // 진행률 = 완료된 단계 수 / 총 단계 수
            double progress = missions.isEmpty() ? 0.0 :
                    (double) completedStages / missions.size();

            String habitName = missions.get(0).getHabit().getHabitName();
            return new HabitProgressDTO(habitId, habitName, progress);
        })
        .toList();

        // ---------------------
        // 6️⃣ 전체 태그별 Mission 수
        // ---------------------
        Map<String, Long> totalTagCounts = allMissions.stream()
                .map(MissionEntity::getHabit)
                .filter(Objects::nonNull)
                .map(HabitEntity::getTag)
                .filter(Objects::nonNull)
                .map(TagEntity::getTagName)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // ---------------------
        // 7️⃣ 연속 성공일수 (오늘 기준)
        // ---------------------
        List<LocalDate> successDates = monthLogs.stream()
                .map(MissionLogEntity::getCheckDate)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .toList();

        int consecutiveDays = 0;
        if (!successDates.isEmpty() && successDates.get(0).equals(today)) {
            consecutiveDays = 1;
            LocalDate prevDate = today;
            for (int i = 1; i < successDates.size(); i++) {
                LocalDate current = successDates.get(i);
                if (current.equals(prevDate.minusDays(1))) {
                    consecutiveDays++;
                    prevDate = current;
                } else break;
            }
        }

        // ---------------------
        // 8️⃣ DTO 생성
        // ---------------------
        return ReportDTO.builder()
                .consecutiveSuccessDays(consecutiveDays)
                .monthTagCounts(monthTagCounts)
                .completedMissions((int) completedMissions)
                .totalTagCounts(totalTagCounts)
                .habitProgressList(habitProgressList)
                .build();
    }
}