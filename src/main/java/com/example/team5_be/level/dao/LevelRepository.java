package com.example.team5_be.level.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.team5_be.level.domain.entity.LevelEntity;

@Repository
public interface LevelRepository extends JpaRepository<LevelEntity, Integer> {
}
