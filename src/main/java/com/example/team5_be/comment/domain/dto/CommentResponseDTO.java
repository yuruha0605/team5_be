package com.example.team5_be.comment.domain.dto;

import java.time.LocalDateTime;

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
    private String content;
    private Integer missionId;
    private String userId;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;


    public static CommentResponseDTO fromEntity(CommentEntity entity) {
        return CommentResponseDTO.builder()
                .commentId(entity.getCommentId())
                .content(entity.getContent())
                .createAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .missionId(entity.getMission().getMissionId())
                .userId(entity.getUser().getUserId())
                .build();
    }
}
