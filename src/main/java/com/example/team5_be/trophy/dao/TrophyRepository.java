package com.example.team5_be.trophy.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.team5_be.habit.domain.entity.HabitEntity;
import com.example.team5_be.trophy.domain.entity.TrophyEntity;

@Repository
public interface TrophyRepository extends JpaRepository<TrophyEntity, Integer> {
    Optional<TrophyEntity> findByHabit(HabitEntity habit);
}