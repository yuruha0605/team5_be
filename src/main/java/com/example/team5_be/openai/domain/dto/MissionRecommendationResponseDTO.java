package com.example.team5_be.openai.domain.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionRecommendationResponseDTO {
    private Integer habitId;
    private String habitName;
    private List<Mission> missions;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Mission {
        private String missionName;
        private String missionDefinition;
        private String levelName;
    }
}