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
public class MonthlyReportDTO {
    private Map<String, Long> monthTagCounts;
    private List<HabitProgressDTO> habitProgressList;
}
