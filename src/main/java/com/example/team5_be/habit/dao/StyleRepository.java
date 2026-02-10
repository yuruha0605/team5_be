package com.example.team5_be.habit.dao;

import com.example.team5_be.habit.domain.entity.StyleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StyleRepository extends JpaRepository<StyleEntity, Integer> {
}
