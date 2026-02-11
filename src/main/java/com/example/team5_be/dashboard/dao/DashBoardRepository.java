package com.example.team5_be.dashboard.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.team5_be.dashboard.domain.dto.TotalScoreRankingView;
import com.example.team5_be.dashboard.domain.entity.DashBoardEntity;

public interface DashBoardRepository extends JpaRepository<DashBoardEntity, String> {

    @Query("""
    select d.userId as userId,
           d.userName as userName,
           d.totalScore as totalScore
    from DashBoardEntity d
    order by d.totalScore desc
    """)
    List<TotalScoreRankingView> findRankingByTotalScore();

}
