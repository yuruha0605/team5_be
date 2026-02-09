package com.example.team5_be.mission.domain.dto;

import com.example.team5_be.mission.domain.entity.MissionEntity;

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
public class MissionResponseDTO {
    private Integer missionId;
    private Integer memberName;
    private Integer habitName;
    private Integer modeName;
    private Integer levelName;
    private Integer statusName;
    private String missionName;
    private String missionDefinition;


    public static MissionResponseDTO fromEntity(MissionEntity entity) {
        return MissionResponseDTO.builder()
                .missionId(entity.getMissionId())
                .memberName(entity.getMemberName())
                .habitName(entity.getHabitName())
                .modeName(entity.getModeName())
                .levelName(entity.getLevelName())
                .statusName(entity.getStatusName())
                .missionName(entity.getMissionName())
                .missionDefinition(entity.getMissionDefinition())
                .build() ;
    }
}
