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
import com.example.team5_be.mypage.domain.dto.MonthlyReportDTO;
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

    // 누적/총 현황
    public ReportDTO getOverallStats(String userId) {
        List<MissionEntity> allMissions = missionRepo.findByUser_UserId(userId);

        long completedMissions = allMissions.stream()
                .filter(m -> m.getStatus() != null && Integer.valueOf(3).equals(m.getStatus().getStatusId()))
                .count();

        Map<String, Long> totalTagCounts = allMissions.stream()
                .map(MissionEntity::getHabit)
                .filter(Objects::nonNull)
                .map(h -> h.getTag())
                .filter(Objects::nonNull)
                .map(tag -> tag.getTagName())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // 연속 성공일수
        List<Integer> allMissionIds = allMissions.stream()
                .map(MissionEntity::getMissionId)
                .toList();

        List<LocalDate> successDates = missionLogRepo
                .findByMission_MissionIdInAndCheckDateBetween(
                        allMissionIds,
                        LocalDate.of(2000,1,1), // 아주 오래전부터
                        LocalDate.now()
                )
                .stream()
                .filter(log -> Boolean.TRUE.equals(log.getIsChecked())) // 성공 필터링
                .map(MissionLogEntity::getCheckDate)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .toList();

        int consecutiveDays = 0;
        LocalDate today = LocalDate.now();

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

        return ReportDTO.builder()
                .completedMissions((int) completedMissions)
                .totalTagCounts(totalTagCounts)
                .consecutiveSuccessDays(consecutiveDays)
                .build();
        }

        // MonthlyReport
        public MonthlyReportDTO getMonthlyReport(String userId, YearMonth month) {
                LocalDate monthStart = month.atDay(1);
                LocalDate monthEnd = month.atEndOfMonth();

                List<MissionEntity> allMissions = missionRepo.findByUser_UserId(userId);

                Map<Integer, List<MissionEntity>> habitMissionMap = allMissions.stream()
                        .collect(Collectors.groupingBy(m -> m.getHabit().getHabitId()));

                List<Integer> missionIds = allMissions.stream()
                        .map(MissionEntity::getMissionId)
                        .toList();

                List<MissionLogEntity> monthLogs = missionLogRepo
                        .findByMission_MissionIdInAndCheckDateBetween(
                                missionIds,
                                monthStart,
                                monthEnd
                        )
                        .stream()
                        .filter(log -> Boolean.TRUE.equals(log.getIsChecked())) // 성공 로그만
                        .toList();

                int daysInMonth = month.lengthOfMonth();

                List<HabitProgressDTO> habitProgressList = habitMissionMap.entrySet().stream()
                        .map(entry -> {
                        Integer habitId = entry.getKey();
                        List<MissionEntity> missions = entry.getValue();

                        Set<Integer> ids = missions.stream()
                                .map(MissionEntity::getMissionId)
                                .collect(Collectors.toSet());

                        long successDays = monthLogs.stream()
                                .filter(log -> ids.contains(log.getMission().getMissionId()))
                                .map(MissionLogEntity::getCheckDate)
                                .distinct()
                                .count();

                        double progress = (double) successDays / daysInMonth;
                        String habitName = missions.get(0).getHabit().getHabitName();
                        return new HabitProgressDTO(habitId, habitName, progress);
                        })
                        .toList();

                Map<String, Long> monthTagCounts = monthLogs.stream()
                        .map(log -> log.getMission().getHabit())
                        .filter(Objects::nonNull)
                        .map(HabitEntity::getTag)
                        .filter(Objects::nonNull)
                        .map(TagEntity::getTagName)
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

                return MonthlyReportDTO.builder()
                        .habitProgressList(habitProgressList)
                        .monthTagCounts(monthTagCounts)
                        .build();
        }
}
