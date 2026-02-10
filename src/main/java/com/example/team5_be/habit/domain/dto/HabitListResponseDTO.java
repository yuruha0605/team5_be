package com.example.team5_be.habit.domain.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitListResponseDTO {
    private Integer tagId;
    private List<Item> habits;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Item {
        private Integer habitId;
        private Integer styleId;

        private String habitName;
        private String habitDefinition;

        private Integer startValue;
        private Integer stepValue;
        private Integer targetValue;
        private String unit;

        private boolean isJoined;
        private HabitStatus myStatus; // 없으면 null
    }
}
