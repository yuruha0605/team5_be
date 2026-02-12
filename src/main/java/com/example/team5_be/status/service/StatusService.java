package com.example.team5_be.status.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.team5_be.status.dao.StatusRepository;
import com.example.team5_be.status.domain.dto.StatusResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatusService {
    private final StatusRepository statusRepository;

    @Transactional(readOnly = true)
    public List<StatusResponseDTO> listStatuses() {
        return statusRepository.findAll(Sort.by(Sort.Direction.ASC, "statusId"))
                .stream()
                .map(StatusResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public Integer searchStatusId(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return statusRepository.findByStatusNameContainingIgnoreCaseOrderByStatusIdAsc(keyword)
                .stream()
                .map(status -> status.getStatusId())
                .findFirst()
                .orElse(null);
    }
}
