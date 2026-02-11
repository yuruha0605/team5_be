package com.example.team5_be.habit.dao;

import com.example.team5_be.habit.domain.entity.HabitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<HabitEntity, Integer> {
    List<HabitEntity> findAllByTag_TagId(Integer tagId);
}
