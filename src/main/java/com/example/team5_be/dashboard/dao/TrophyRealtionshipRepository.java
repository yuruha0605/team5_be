package com.example.team5_be.dashboard.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.team5_be.dashboard.domain.dto.DashBoardRowDTO;
import com.example.team5_be.dashboard.domain.entity.TrophyRelationshipEntity;

public interface TrophyRealtionshipRepository extends JpaRepository<TrophyRelationshipEntity, Long> {

    @Query("""
        select new com.example.team5_be.dashboard.domain.dto.DashBoardRowDTO(
            0, tr.user.userId, tr.user.userName, null, count(tr.id)
        )
        from TrophyRelationshipEntity tr
        group by tr.user.userId, tr.user.userName
        order by count(tr.id) desc
    """)
    List<DashBoardRowDTO> findRankingByTrophyCount();
}
