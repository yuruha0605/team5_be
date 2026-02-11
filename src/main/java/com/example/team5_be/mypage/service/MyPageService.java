package com.example.team5_be.mypage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.team5_be.habit.domain.entity.HabitEntity;
import com.example.team5_be.habit.domain.entity.TagEntity;
import com.example.team5_be.mission.domain.entity.MissionEntity;
import com.example.team5_be.mypage.dao.MyPageRepository;
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

    private final MyPageRepository missionRepo;

    public ReportDTO getMonthlyReport(String userId, YearMonth month) {

        // 이번 달 미션 조회
        List<MissionEntity> missions = missionRepo.findAllByUserIdAndMonth(
                userId,
                month.getMonthValue(),
                month.getYear()
        );

        // 연속 성공일수
        int consecutiveDays = calculateConsecutiveSuccessDays(missions);

        // 이번 달 진행한 미션 수
        int missionsThisMonth = missions.size();

        // 카테고리별 미션 수 (습관 태그 기준)
        Map<String, Long> categoryCounts = missions.stream()
                .map(MissionEntity::getHabit)     // Mission → Habit
                .filter(Objects::nonNull)
                .map(HabitEntity::getTag)         // Habit → Tag
                .filter(Objects::nonNull)
                .map(TagEntity::getTagName)       // Tag → tagName
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        return ReportDTO.builder()
                .consecutiveSuccessDays(consecutiveDays)
                .missionsThisMonth(missionsThisMonth)
                .categoryCounts(categoryCounts)
                .build();
    }

    // 연속 성공일수 계산
    private int calculateConsecutiveSuccessDays(List<MissionEntity> missions) {
        List<LocalDate> successDates = missions.stream()
            .filter(m -> "COMPLETED".equals(m.getStatus().getStatusName()))
            .map(MissionEntity::getMissionEndDate)
            .distinct() // 하루에 여러 미션 방지
            .sorted(Comparator.reverseOrder())
            .toList();

    if (successDates.isEmpty()) return 0;

    int streak = 1; // 첫 날은 무조건 1일
    LocalDate prevDate = successDates.get(0);

    for (int i = 1; i < successDates.size(); i++) {
        LocalDate current = successDates.get(i);

        // 정확히 하루 차이면 연속
        if (current.equals(prevDate.minusDays(1))) {
            streak++;
            prevDate = current;
        } else {
            break;
        }
    }

    return streak;
    }
}