package com.example.team5_be.dashboard.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.team5_be.dashboard.domain.dto.DashBoardRowDTO;
import com.example.team5_be.dashboard.domain.entity.DashBoardEntity;

public interface DashBoardRepository extends JpaRepository<DashBoardEntity, String> {

    @Query("""
        select new com.example.team5_be.dashboard.domain.dto.DashBoardRowDTO(
            0, u.userId, u.userName, u.totalScore, null
        )
        from UserEntity u
        order by u.totalScore desc
    """)
    List<DashBoardRowDTO> findRankingByTotalScore();
}
