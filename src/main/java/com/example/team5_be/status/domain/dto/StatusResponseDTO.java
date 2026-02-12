package com.example.team5_be.status.domain.dto;

import com.example.team5_be.status.domain.entity.StatusEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StatusResponseDTO {
    private Integer statusId;
    private String statusName;

    public static StatusResponseDTO fromEntity(StatusEntity entity) {
        return StatusResponseDTO.builder()
                .statusId(entity.getStatusId())
                .statusName(entity.getStatusName())
                .build();
    }
}
