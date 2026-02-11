package com.example.team5_be.habit.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitCreateResponseDTO {
    private Integer habitId;
    private String message; // "HABIT_CREATED"
}
