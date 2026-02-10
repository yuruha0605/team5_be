package com.example.team5_be.habit.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitJoinResponseDTO {
    private Long habitId;
    private HabitStatus status;
    private String message; // "HABIT_JOINED" / "JOIN_UPDATED"
}
