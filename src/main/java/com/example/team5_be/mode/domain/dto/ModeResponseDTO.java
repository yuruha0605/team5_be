package com.example.team5_be.mode.domain.dto;

import com.example.team5_be.mode.domain.entity.ModeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModeResponseDTO {
    private Integer modeId;
    private String modeName;

    public static ModeResponseDTO fromEntity(ModeEntity entity) {
        return ModeResponseDTO.builder()
                .modeId(entity.getModeId())
                .modeName(entity.getModeName())
                .build();
    }
}
