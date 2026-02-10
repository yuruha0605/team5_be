package com.example.team5_be.habit.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitUnJoinResponseDTO {
    private Integer habitId;
    private String message; // "HABIT_UNJOINED"
}
