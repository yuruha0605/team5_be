package com.example.team5_be.mission.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.team5_be.common.health.domain.dto.HealthResponseDTO;
import com.example.team5_be.common.health.domain.entity.HealthEntity;
import com.example.team5_be.mission.dao.MissionRepository;
import com.example.team5_be.mission.domain.dto.MissionRequestDTO;
import com.example.team5_be.mission.domain.dto.MissionResponseDTO;
import com.example.team5_be.mission.domain.entity.MissionEntity;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MissionService {
    private final MissionRepository missionRepository ;


    /*
    Mission RequestDTO
        private Integer memberId;
        private Integer habitId;
        private Integer modeId;
        private Integer levelId;
        private Integer statusId;
        
        private String missionName;
        private String missionDefinition;
    */

    // 미션 생성
    @Transactional
    public MissionResponseDTO create(MissionRequestDTO request) {
        System.out.println(">>>> Entered mission service : create");
        MissionEntity entity = missionRepository.save(
                                    MissionEntity.builder()
                                            .habitName(request.getHabitId().getHabitName())
                                            .modeName(request.getModeId().getModeName())
                                            .levelName(request.getLevelId().getLevelName())
                                            .statusName(request.getStatusId().getStatusName())
                                            .missionName(request.getMissionName())
                                            .missionDefinition(request.getMissionDefinition())
                                            .build()) ;   
        MissionResponseDTO response = MissionResponseDTO.fromEntity(entity);
        return response ;
    }


    // 미션 ID 단건 조회
    @Transactional
    public MissionResponseDTO read(Integer missionId) {
        System.out.println(">>>> Entered mission service : read");
        MissionEntity entity = missionRepository.findById(missionId)
                                .orElseThrow(() -> new RuntimeException("read fail"));

        MissionResponseDTO response = MissionResponseDTO.fromEntity(entity);
        return response ;
    }

    // 모든 미션 조회
    @Transactional
    public List<MissionResponseDTO> list() {
        System.out.println(">>>> Entered mission service : list");
        List<MissionEntity> entities = missionRepository.findAll();
        
        List<MissionResponseDTO> responses = entities.stream()
                                                .map(MissionResponseDTO::fromEntity)
                                                .toList();
        return responses ;
    }


    // 미션 업데이트
    @Transactional
    public MissionResponseDTO update(Integer missionId, MissionRequestDTO request) {
        System.out.println(">>>> Entered mission service : update");
        MissionEntity entity = missionRepository.findById(missionId)
                                .orElseThrow(() -> new RuntimeException("read fail"));
        
        MissionEntity updated = MissionEntity.builder()
                                .missionId(entity.getMissionId())
                                .missionName(request.getMissionName())
                                .missionDefinition(request.getMissionDefinition())
                                .build();
        missionRepository.save(updated);
        MissionResponseDTO response = MissionResponseDTO.fromEntity(updated);
                                        
        return response ;
    }
    

    // 미션 삭제
    @Transactional
     public boolean delete(Integer missionId) {
        System.out.println(">>>> Entered mission service : delete");
     
        MissionEntity entity = missionRepository.findById(missionId)
                                .orElseThrow(() -> new RuntimeException("read fail"));
        
        missionRepository.deleteById(entity.getMissionId());
        
        return true ;
    }
    
}
