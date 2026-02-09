package com.example.team5_be.user.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.team5_be.mission.domain.entity.MissionEntity;

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
@Table(name = "MEMBER") 
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    
    @Id
    @Column(name = "MEMBER_ID", unique = true, nullable = false)
    private String userId ;

    @Column(name = "MEMBER_PASSWORD", unique = true, nullable = false, length = 200)
    private String userPassword ;

    @Column(name = "MEMBER_NAME", nullable = false)
    private String userName ;

    @Column(name = "MEMBER_JOB", nullable = true)
    private String userJob ;

    @Column(name = "MEMBER_INTEREST", nullable = true)
    private String userInterest ; 

    @Column(name = "PROFILE_PUBLIC", nullable = false)
    private Boolean profilePublic = false;

    @PrePersist
    public void prePersist() {
        if (profilePublic == null) {
            profilePublic = false;
        }
    }

    @OneToMany(mappedBy = "author", orphanRemoval = false)
    private List<MissionEntity> missions = new ArrayList<>();


}
