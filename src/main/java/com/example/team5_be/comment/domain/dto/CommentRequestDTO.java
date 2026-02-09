package com.example.team5_be.comment.domain.dto;

import com.example.team5_be.comment.domain.entity.CommentEntity;
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
public class CommentRequestDTO {
    private String content;
    private String missionName;
    private Integer missionId;



    public CommentEntity toEntity(MissionEntity mission) {
        return CommentEntity.builder()
                    .content(this.content)
                    .missionName(mission.getMissionName())
                    .mission(mission)
                    .build();
    }

}
