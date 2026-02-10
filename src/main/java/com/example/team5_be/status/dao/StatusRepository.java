package com.example.team5_be.status.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.team5_be.status.domain.entity.StatusEntity;

@Repository
public interface StatusRepository extends JpaRepository<StatusEntity, Integer> {
    
}
