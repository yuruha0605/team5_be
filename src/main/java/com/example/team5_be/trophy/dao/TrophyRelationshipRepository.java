package com.example.team5_be.trophy.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.team5_be.dashboard.domain.dto.DashBoardRowDTO;
import com.example.team5_be.trophy.domain.entity.TrophyEntity;
import com.example.team5_be.trophy.domain.entity.TrophyRelationshipEntity;
import com.example.team5_be.user.domain.entity.UserEntity;

@Repository
public interface TrophyRelationshipRepository extends JpaRepository<TrophyRelationshipEntity, Long> {
    boolean existsByUserAndTrophy(UserEntity user, TrophyEntity trophy);

    List<TrophyRelationshipEntity> findAllByUser(UserEntity user);

    @Query("""
    select new com.example.team5_be.dashboard.domain.dto.DashBoardRowDTO(
        0,
        tr.user.userId,
        tr.user.userName,
        null,
        count(tr.id)
    )
    from TrophyRelationshipEntity tr
    group by tr.user.userId, tr.user.userName
    order by count(tr.id) desc
    """)
    List<DashBoardRowDTO> findRankingByTrophyCount();
}
