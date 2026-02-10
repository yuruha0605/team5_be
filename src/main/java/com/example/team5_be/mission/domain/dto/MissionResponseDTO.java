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
    private String userName;
    private String habitName;
    private String modeName;
    private String levelName;
    private String statusName;
    private String missionName;
    private String missionDefinition;


    public static MissionResponseDTO fromEntity(MissionEntity entity) {
        return MissionResponseDTO.builder()
                .missionId(entity.getMissionId())
                .userName(entity.getUser().getUserName())
                .habitName(entity.getHabit().getHabitName())
                .modeName(entity.getMode().getModeName())
                .levelName(entity.getLevel().getLevelName())
                .statusName(entity.getStatus().getStatusName())
                .missionName(entity.getMissionName())
                .missionDefinition(entity.getMissionDefinition())
                .build() ;
    }
}
