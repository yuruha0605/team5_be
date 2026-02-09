package com.example.team5_be.mission.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.team5_be.mission.domain.entity.MissionEntity;

@Repository
public interface MissionRepository extends JpaRepository<MissionEntity, Integer>{
    public Optional<MissionEntity> findByMissionName(String missionName);
}
