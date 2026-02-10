package com.example.team5_be.common.health.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.team5_be.common.health.domain.entity.HealthEntity;



@Repository
public interface HealthRepository extends JpaRepository<HealthEntity, Long>{
    // entity 당 하나의 레포지토리
}