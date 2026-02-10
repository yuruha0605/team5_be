package com.example.team5_be.habit.dao;

import com.example.team5_be.habit.domain.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
}
