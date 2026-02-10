package com.example.team5_be.mission.domain.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Many;

import com.example.team5_be.comment.domain.entity.CommentEntity;
import com.example.team5_be.level.domain.entity.LevelEntity;
import com.example.team5_be.mode.domain.entity.ModeEntity;
import com.example.team5_be.status.domain.entity.StatusEntity;
import com.example.team5_be.user.domain.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "MISSION_TBL")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MissionEntity {
    /*
    [PK] mission_id
    [FK] user_id
    [FK] habit_id
    [FK] mode_id
    [FK] level_id
    [FK] status_id
    mission_name
    mission_definition
    mission_start_date
    mission_end_date
    */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer missionId;

    @Column(nullable = false, length = 100)
    private String missionName;

    @Column(nullable = false, length = 500)
    private String missionDefinition;


    @Column(nullable = false)
    private LocalDate missionStartDate;

    @Column(nullable = false)
    private LocalDate missionEndDate;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "habitId", nullable = false)
    private HabitEntity habit;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "modeId", nullable = false)
    private ModeEntity mode;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "levelId", nullable = false)
    private LevelEntity level;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "statusId", nullable = false)
    private StatusEntity status;


    @OneToMany(mappedBy = "mission", fetch = FetchType.LAZY)
    private List<CommentEntity> comments = new ArrayList<>();



    
    
}
