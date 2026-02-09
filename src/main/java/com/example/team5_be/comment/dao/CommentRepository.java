package com.example.team5_be.comment.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.team5_be.comment.domain.entity.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer>{
    public List<CommentEntity> findByMission_MissionId(Integer missionId);

}
