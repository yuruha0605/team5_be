package com.example.team5_be.level.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.team5_be.level.domain.entity.LevelEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface LevelRepository extends JpaRepository<LevelEntity, Integer> {
    Optional<LevelEntity> findByLevelName(String levelName);

    List<LevelEntity> findByLevelNameContainingIgnoreCaseOrderByLevelIdAsc(String keyword);

    Optional<LevelEntity> findTopByOrderByLevelIdDesc();
}
