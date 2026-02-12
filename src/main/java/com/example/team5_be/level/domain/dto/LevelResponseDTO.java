package com.example.team5_be.level.domain.dto;

import com.example.team5_be.level.domain.entity.LevelEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LevelResponseDTO {
    private Integer levelId;
    private String levelName;
    private Integer levelDate;

    public static LevelResponseDTO fromEntity(LevelEntity entity) {
        return LevelResponseDTO.builder()
                .levelId(entity.getLevelId())
                .levelName(entity.getLevelName())
                .levelDate(entity.getLevelDate())
                .build();
    }
}
