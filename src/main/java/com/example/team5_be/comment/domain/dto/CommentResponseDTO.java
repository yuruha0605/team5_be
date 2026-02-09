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
    private String content;
    private Integer missonId;

    public static CommentResponseDTO fromEntityWithoutComments(CommentEntity entity) {
        return CommentResponseDTO.builder()
                .commentId(entity.getCommentId())
                .title(entity.getMisson())
                .content(entity.getContent())
                .build();
    }

    public static CommentResponseDTO fromEntity(CommentEntity entity) {
        return CommentResponseDTO.builder()
                                .commentId(entity.getCommentId())
                                .content(entity.getContent())
                                .missonId(entity.getMisson().getMissonId())
                                .build();
    }
}
