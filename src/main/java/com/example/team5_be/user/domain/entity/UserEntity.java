package com.example.team5_be.user.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.team5_be.comment.domain.entity.CommentEntity;
import com.example.team5_be.mission.domain.entity.MissionEntity;
import com.example.team5_be.trophy.domain.entity.TrophyRelationshipEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "user_tbl") 
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    
    @Id
    @Column(name = "user_id", unique = true, nullable = false)
    private String userId ;

    @Column(name = "user_password", unique = true, nullable = false, length = 200)
    private String userPassword ;

    @Column(name = "user_name", nullable = false)
    private String userName ;

    @Column(name = "user_job", nullable = true)
    private String userJob ;

    @Column(name = "user_interest", nullable = true)
    private String userInterest ; 

    @Column(name = "profile_public", nullable = false)
    private Boolean profilePublic = false;

    @PrePersist
    public void prePersist() {
        if (profilePublic == null) {
            profilePublic = false;
        }
    }

    // 1:N Mission
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MissionEntity> missions = new ArrayList<>();

    // 1:N Comment
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments = new ArrayList<>();

    // Habit과 ManyToMany 연결 (중간 테이블 HabitRelationship 사용)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HabitRelationshipEntity> habits = new ArrayList<>();

    // Trophy와 ManyToMany 연결 (중간 테이블 TrophyRelationship 사용)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrophyRelationshipEntity> trophies = new ArrayList<>();


}
