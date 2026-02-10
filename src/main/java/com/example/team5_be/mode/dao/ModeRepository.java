package com.example.team5_be.mode.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.team5_be.mode.domain.entity.ModeEntity;

@Repository
public interface ModeRepository extends JpaRepository<ModeEntity, Integer> {
    
}
