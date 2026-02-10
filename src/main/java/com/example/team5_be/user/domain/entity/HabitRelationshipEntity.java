package com.example.team5_be.user.domain.entity;

import com.example.team5_be.status.domain.entity.StatusEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="habit_relationship")
public class HabitRelationshipEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    @Id
    @ManyToOne
    @JoinColumn(name = "HABIT_ID", nullable = false)
    private HabitEntity habit;

    @ManyToOne
    @JoinColumn(name = "STATUS_ID", nullable = false)
    private StatusEntity status;
}
