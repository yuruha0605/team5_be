package com.example.team5_be.common.health.domain.dto;

import com.example.team5_be.common.health.domain.entity.HealthEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HealthResponseDTO {
    private Long    id ;
    private String message;

    public static HealthResponseDTO fromEntity(HealthEntity entity) {
        return HealthResponseDTO.builder()
                .id(entity.getId())
                .message(entity.getMessage())
                .build() ;
    }

    //requestDTO는 toEntity로 만듬, DTO를 entity로 반환하는
}

