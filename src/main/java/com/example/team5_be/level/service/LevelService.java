package com.example.team5_be.level.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.team5_be.level.dao.LevelRepository;
import com.example.team5_be.level.domain.dto.LevelResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LevelService {
    private final LevelRepository levelRepository;

    @Transactional(readOnly = true)
    public List<LevelResponseDTO> listLevels() {
        return levelRepository.findAll(Sort.by(Sort.Direction.ASC, "levelId"))
                .stream()
                .map(LevelResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public Integer searchLevelId(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return levelRepository.findByLevelNameContainingIgnoreCaseOrderByLevelIdAsc(keyword)
                .stream()
                .map(level -> level.getLevelId())
                .findFirst()
                .orElse(null);
    }
}
