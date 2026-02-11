package com.example.team5_be.trophy.domain.entity;

import com.example.team5_be.user.domain.entity.UserEntity;

import jakarta.persistence.Entity;
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
@Table(name="Trophy_relationship")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TrophyRelationshipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto-increment
    private Long id; // 중간 테이블 PK용

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name="trophy_id")
    private TrophyEntity trophy;
}
