package com.example.team5_be.common.health.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.team5_be.common.health.dao.HealthRepository;
import com.example.team5_be.common.health.domain.dto.HealthResponseDTO;
import com.example.team5_be.common.health.domain.entity.HealthEntity;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class HealthService {
    
    private final HealthRepository healthRepository ;

    public HealthResponseDTO create() {
        //insert~
        HealthEntity entity = healthRepository
                                .save(HealthEntity
                                    .builder()
                                    .message("ok")
                                    .build());
        HealthResponseDTO response = HealthResponseDTO.fromEntity(entity);
        return response ;
    }

    public HealthResponseDTO read() {
        //insert~
        // Optional<HealthEntity> find = healthRepository.findById(1L);
        HealthEntity entity = healthRepository.findById(1L)
                                .orElseThrow(() -> new RuntimeException("read fail"));
        HealthResponseDTO response = HealthResponseDTO.fromEntity(entity);
        //entity는 영속성 형식이기 때문에 data DTO로 변환해야 함 
        return response ;
    }

    public HealthResponseDTO update() {
        //insert~
        // Optional<HealthEntity> find = healthRepository.findById(1L);
        HealthEntity entity = healthRepository.findById(1L)
                                .orElseThrow(() -> new RuntimeException("read fail"));
        
        // setter가 있다면 
        entity.setMessage("fail");
        HealthEntity result = healthRepository.save(entity);
        HealthResponseDTO response = HealthResponseDTO.fromEntity(result);
        
        // HealthEntity updated = HealthEntity.builder()
        //                         .id(entity.getId())
        //                         .message("fail")
        //                         .build();
        // healthRepository.save(updated);
        // HealthResponseDTO response = HealthResponseDTO.fromEntity(entity);
        return response ;
    }
    
     public boolean delete() {
     
        HealthEntity entity = healthRepository.findById(1L)
                                .orElseThrow(() -> new RuntimeException("read fail"));
        
        healthRepository.deleteById(entity.getId());
        
        return true ;
    }
}
