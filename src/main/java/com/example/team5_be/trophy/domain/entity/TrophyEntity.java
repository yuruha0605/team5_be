package com.example.team5_be.trophy.domain.entity;

import java.util.List;

import com.example.team5_be.habit.domain.entity.HabitEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="trophy_tbl")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TrophyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer trophyId;

    @Column(nullable = false)
    private String trophyName;

    // 각 트로피는 하나의 습관과만 연결 (Unique)
    @OneToOne
    @JoinColumn(name = "HABIT_ID", unique = true, nullable = false)
    private HabitEntity habit;

    @OneToMany(mappedBy = "trophy")
    private List<TrophyRelationshipEntity> users; //trophy를 가진 유저목록 
}