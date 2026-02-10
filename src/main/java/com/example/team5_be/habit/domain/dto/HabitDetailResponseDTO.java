package com.example.team5_be.habit.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitDetailResponseDTO {
    private Long habitId;
    private Long tagId;
    private Long styleId;

    private String habitName;
    private String habitDefinition;

    private Integer startValue;
    private Integer stepValue;
    private Integer targetValue;
    private String unit;

    private boolean isJoined;
    private HabitStatus myStatus; // 없으면 null
}
