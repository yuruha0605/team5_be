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
@Table(name = "USER_TBL") 
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

    @OneToMany(mappedBy = "user", orphanRemoval = false)
    private List<MissionEntity> missions = new ArrayList<>();


}
