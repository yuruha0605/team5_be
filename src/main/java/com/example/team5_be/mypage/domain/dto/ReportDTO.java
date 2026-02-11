package com.example.team5_be.mypage.domain.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    
    // 이번 달 통계 (MissionLog 기준)
    private int consecutiveSuccessDays;           // 연속 성공일수
    private Map<String, Long> monthTagCounts;     // 이번 달 많이 진행한 태그

    // 총 미션 기준 (MissionEntity 기준)
    private int completedMissions;                // 총 미션 중 완료된 개수
    private Map<String, Long> totalTagCounts;     // 총 진행한 습관 기준 태그

    // Habit별 진행률
    private List<HabitProgressDTO> habitProgressList;  
}

