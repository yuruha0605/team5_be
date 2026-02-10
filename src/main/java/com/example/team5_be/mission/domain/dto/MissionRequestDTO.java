package com.example.team5_be.mission.domain.dto;

import com.example.team5_be.level.domain.entity.LevelEntity;
import com.example.team5_be.mission.domain.entity.MissionEntity;
import com.example.team5_be.mode.domain.entity.ModeEntity;
import com.example.team5_be.status.domain.entity.StatusEntity;
import com.example.team5_be.user.domain.entity.UserEntity;

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
public class MissionRequestDTO {

    // 미션 생성에 필요한 필드들
    private Integer userId;
    private Integer habitId;
    private Integer modeId;
    private Integer levelId;
    private Integer statusId;

    private String missionName;
    private String missionDefinition;
    


    public MissionEntity toEntity(  UserEntity user,
                                    //HabitEntity habit, 
                                    ModeEntity mode, 
                                    LevelEntity level, 
                                    StatusEntity status) {
        return MissionEntity.builder()
                .user(user)
                //.habit(habit)
                .mode(mode)
                .level(level)
                .status(status)
                .missionName(this.missionName)
                .missionDefinition(this.missionDefinition)
                .build() ;
    }
}
