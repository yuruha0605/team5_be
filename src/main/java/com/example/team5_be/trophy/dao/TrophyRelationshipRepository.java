package com.example.team5_be.trophy.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.example.team5_be.trophy.domain.entity.TrophyRelationshipEntity;



@Repository
public interface TrophyRelationshipRepository extends JpaRepository<TrophyRelationshipEntity, Integer> {
    
    @Query("""
        SELECT 
            u.userId AS userId,
            u.userName AS userName,
            COUNT(tr.id) AS trophyCount
        FROM TrophyRelationshipEntity tr
        JOIN tr.user u
        GROUP BY u.userId, u.userName
        ORDER BY COUNT(tr.id) DESC
    """)
    List<com.example.team5_be.dashboard.domain.dto.TrophyCountRankingView> findRankingByTrophyCount();

    
    @Query("SELECT tr FROM TrophyRelationshipEntity tr JOIN FETCH tr.trophy t JOIN FETCH t.habit h WHERE tr.user.userId = :userId")
    List<TrophyRelationshipEntity> findAllByUserId(@Param("userId") String userId);

    boolean existsByUser_UserIdAndTrophy_TrophyId(String userId, Integer trophyId);
}
