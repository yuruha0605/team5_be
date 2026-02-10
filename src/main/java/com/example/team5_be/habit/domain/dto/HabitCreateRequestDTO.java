package com.example.team5_be.habit.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitCreateRequestDTO {
    private Long tagId;
    private Long styleId;

    private String habitName;
    private String habitDefinition;

    // 반복형이면 null 허용
    private Integer startValue;
    private Integer stepValue;
    private Integer targetValue;
    private String unit;
}
