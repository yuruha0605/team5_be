package com.example.team5_be.mypage.domain.dto;

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
public class HabitProgressDTO {
    private Integer habitId;
    private String habitName;
    private double progress;  // 0 ~ 1, 진행률
}
