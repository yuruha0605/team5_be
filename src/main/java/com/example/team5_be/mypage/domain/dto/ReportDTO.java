package com.example.team5_be.mypage.domain.dto;

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
    
    private int consecutiveSuccessDays;          // 연속 성공일수
    private int missionsThisMonth;               // 이번 달 진행한 미션 수
    private Map<String, Long> categoryCounts;   // 카테고리별 미션 수 (습관 태그 기준)
}
