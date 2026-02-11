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

       
        // 전체 Mission 조회 (Habit별 그룹핑)
        List<MissionEntity> allMissions = missionRepo.findByUser_UserId(userId);

        Map<Integer, List<MissionEntity>> habitMissionMap = allMissions.stream()
                .collect(Collectors.groupingBy(m -> m.getHabit().getHabitId()));


        // 이번 달 MissionLog 조회
        LocalDate monthStart = month.atDay(1);
        LocalDate monthEnd = month.atEndOfMonth();

        List<MissionLogEntity> monthLogs = missionLogRepo
                .findByMission_MissionIdInAndCheckDateBetween(
                        allMissions.stream().map(MissionEntity::getMissionId).toList(),
                        monthStart, monthEnd
                )
                .stream()
                .filter(log -> Boolean.TRUE.equals(log.getIsChecked())) // null 대비
                .toList();


        // Habit별 진행률 계산 (이번 달 일일로그 완료 횟수 기준, 한달 진행률)
        List<HabitProgressDTO> habitProgressList = habitMissionMap.entrySet().stream()
                .map(entry -> {
                        Integer habitId = entry.getKey();
                        List<MissionEntity> missions = entry.getValue();

                        // 이번 달 이 habit에 속한 완료된 로그만
                        long completedThisMonth = monthLogs.stream()
                                .filter(log -> missions.stream()
                                        .anyMatch(m -> m.getMissionId().equals(log.getMission().getMissionId()))
                                )
                                .count();

                        double progress = missions.isEmpty() ? 0.0 :
                                (double) completedThisMonth / missions.size();

                        String habitName = missions.get(0).getHabit().getHabitName();
                        return new HabitProgressDTO(habitId, habitName, progress);
                })
                .toList();
        
        // 이번 달 태그별 완료 미션 수
        Map<String, Long> monthTagCounts = monthLogs.stream()
                .map(log -> log.getMission().getHabit())
                .filter(Objects::nonNull)
                .map(HabitEntity::getTag)
                .filter(Objects::nonNull)
                .map(TagEntity::getTagName)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        //총 완료 미션 수 
        long completedMissions = allMissions.stream()
                .filter(m -> "COMPLETED".equals(m.getStatus().getStatusName()))
                .count();
                
        // 전체 태그별 Mission 수
        Map<String, Long> totalTagCounts = allMissions.stream()
                .map(MissionEntity::getHabit)
                .filter(Objects::nonNull)
                .map(HabitEntity::getTag)
                .filter(Objects::nonNull)
                .map(TagEntity::getTagName)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));


        // 연속 성공일수 (오늘 기준)
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


        // DTO 생성
        return ReportDTO.builder()
                .consecutiveSuccessDays(consecutiveDays)
                .monthTagCounts(monthTagCounts)
                .completedMissions((int) completedMissions)
                .totalTagCounts(totalTagCounts)
                .habitProgressList(habitProgressList)
                .build();
    }
}