package com.example.team5_be.openai.domain.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitRecommendationResponseDTO {
    private List<RecommendedHabit> recommendedHabits;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecommendedHabit {
        private String habitName;
        private String habitDefinition;
        private String tagName;
        private Integer styleId; // 1: 반복형, 2: 점진형
        private List<RecommendedMission> recommendedMissions;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecommendedMission {
        private String missionName;
        private String missionDefinition;
        private String levelName; // "1일", "3일", "1주", etc.
    }
}