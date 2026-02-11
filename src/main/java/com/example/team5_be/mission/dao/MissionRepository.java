package com.example.team5_be.mission.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.team5_be.mission.domain.entity.MissionEntity;

@Repository
public interface MissionRepository extends JpaRepository<MissionEntity, Integer>{
    public Optional<MissionEntity> findByMissionName(String missionName);
    public List<MissionEntity> findByUser_UserId(String userId);
    public List<MissionEntity> findByUser_UserIdAndMissionNameContainingIgnoreCase(String userId, String missionName);
    public Optional<MissionEntity> findByMissionIdAndUser_UserId(Integer missionId, String userId);

    List<MissionEntity> findByUser_UserIdAndMissionStartDateLessThanEqualAndMissionEndDateGreaterThanEqual(
            String userId,
            LocalDate endDate,
            LocalDate startDate);
}
