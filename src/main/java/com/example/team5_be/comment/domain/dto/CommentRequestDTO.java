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
public class CommentRequestDTO {
    private String content;
    private Integer MissonId;


    public CommentEntity toEntity(MissonEntity misson) {
        return CommentEntity.builder()
                    .content(this.content)
                    .misson(misson)
                    .build();

}
