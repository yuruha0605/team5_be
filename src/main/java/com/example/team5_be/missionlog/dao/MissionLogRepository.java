package com.example.team5_be.missionlog.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.team5_be.missionlog.domain.entity.MissionLogEntity;

@Repository
public interface MissionLogRepository extends JpaRepository<MissionLogEntity, Long> {
    Optional<MissionLogEntity> findByMission_MissionIdAndCheckDate(Integer missionId, LocalDate checkDate);

    List<MissionLogEntity> findByMission_MissionIdInAndCheckDateBetween(List<Integer> missionIds, LocalDate startDate, LocalDate endDate);

    List<MissionLogEntity> findByMission_MissionIdInAndCheckDate(List<Integer> missionIds, LocalDate checkDate);

    long countByMission_MissionIdAndCheckDateBetweenAndIsCheckedTrue(Integer missionId, LocalDate startDate, LocalDate endDate);

    void deleteByMission_MissionId(Integer missionId);
}
