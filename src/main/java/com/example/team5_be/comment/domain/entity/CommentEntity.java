package com.example.team5_be.comment.domain.entity;

import com.example.team5_be.mission.domain.entity.MissionEntity;
import com.example.team5_be.user.domain.entity.UserEntity;

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
@Table(name = "comment_tbl")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class CommentEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer commentId ; 

    @Column(name = "title", nullable = false , length = 150)
    private String title;

    @Column(name = "content", nullable = false , length = 500)
    private String content ; 

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mission_id")
    private MissionEntity mission ;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user ;

    

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}