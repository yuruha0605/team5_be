package com.example.team5_be.mode.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.team5_be.mode.dao.ModeRepository;
import com.example.team5_be.mode.domain.dto.ModeResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModeService {
    private final ModeRepository modeRepository;

    @Transactional(readOnly = true)
    public List<ModeResponseDTO> listModes() {
        return modeRepository.findAll(Sort.by(Sort.Direction.ASC, "modeId"))
                .stream()
                .map(ModeResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public Integer searchModeId(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return modeRepository.findByModeNameContainingIgnoreCaseOrderByModeIdAsc(keyword)
                .stream()
                .map(mode -> mode.getModeId())
                .findFirst()
                .orElse(null);
    }
}
