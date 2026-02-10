package com.example.team5_be.comment.domain.dto;

import com.example.team5_be.comment.domain.entity.CommentEntity;

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
public class CommentResponseDTO {
    private Integer commentId;
    private String title;
    private String content;
    private Integer missionId;
    private String userId;

    public static CommentResponseDTO fromEntity(CommentEntity entity) {
        return CommentResponseDTO.builder()
                .commentId(entity.getCommentId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .missionId(entity.getMission().getMissionId())
                .userId(entity.getUser().getUserId())
                .build();
    }
}
