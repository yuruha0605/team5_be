package com.example.team5_be.dashboard.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashBoardRowDTO {
    private Integer rank;
    private String userId;
    private String userName;
    private Integer totalScore;
    private Integer trophyCount;
}
