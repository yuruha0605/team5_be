package com.example.team5_be.mypage.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.team5_be.mission.domain.entity.MissionEntity;

import java.util.List;

public interface MyPageRepository extends JpaRepository<MissionEntity, Long> {

    @Query("SELECT m FROM MissionEntity m " +
           "WHERE m.user.userId = :userId " +
           "AND FUNCTION('MONTH', m.missionEndDate) = :month " +
           "AND FUNCTION('YEAR', m.missionEndDate) = :year")
    List<MissionEntity> findAllByUserIdAndMonth(@Param("userId") String userId,
                                                @Param("month") int month,
                                                @Param("year") int year);
}