package com.example.team5_be.habit.dao;

import com.example.team5_be.habit.domain.entity.HabitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitRepository extends JpaRepository<HabitEntity, Integer> {
    List<HabitEntity> findAllByTag_TagId(Integer tagId);
}
