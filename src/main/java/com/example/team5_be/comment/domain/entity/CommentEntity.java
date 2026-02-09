package com.example.team5_be.comment.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "COMMENT_TBL")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId ; 

    @Column(nullable = false , length = 150)
    private String title;

    @Column(nullable = false , length = 500)
    private String content ; 

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "missonId")
    private MissonEntity misson ;

    


    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}