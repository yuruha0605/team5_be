package com.example.team5_be.comment.domain.dto;

import com.example.team5_be.comment.domain.entity.CommentEntity;
import com.example.team5_be.mission.domain.entity.MissionEntity;
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
public class CommentRequestDTO {
    private String content;



    public CommentEntity toEntity(MissionEntity mission, UserEntity user) {
        return CommentEntity.builder()
                    .content(content)
                    .mission(mission)
                    .user(user)
                    .build();
    }

}
