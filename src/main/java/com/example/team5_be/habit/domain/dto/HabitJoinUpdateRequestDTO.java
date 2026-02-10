package com.example.team5_be.habit.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitJoinUpdateRequestDTO {
    private HabitStatus status;
}
