package com.example.team5_be.habit.dao;

import com.example.team5_be.habit.domain.entity.HabitRelationshipEntity;
import com.example.team5_be.habit.domain.entity.HabitRelationshipEntity.HabitRelationshipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HabitRelationshipRepository extends JpaRepository<HabitRelationshipEntity, HabitRelationshipId> {

    boolean existsById_UserIdAndId_HabitId(String userId, Integer habitId);
    Optional<HabitRelationshipEntity> findById_UserIdAndId_HabitId(String userId, Integer habitId);
    void deleteById_UserIdAndId_HabitId(String userId, Integer habitId);
}
