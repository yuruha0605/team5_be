package com.example.team5_be.mission.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.team5_be.habit.dao.HabitRepository;
import com.example.team5_be.habit.domain.entity.HabitEntity;
import com.example.team5_be.level.dao.LevelRepository;
import com.example.team5_be.level.domain.entity.LevelEntity;
import com.example.team5_be.mission.dao.MissionRepository;
import com.example.team5_be.mission.domain.dto.MissionRequestDTO;
import com.example.team5_be.mission.domain.dto.MissionResponseDTO;
import com.example.team5_be.mission.domain.entity.MissionEntity;
import com.example.team5_be.mode.dao.ModeRepository;
import com.example.team5_be.mode.domain.entity.ModeEntity;
import com.example.team5_be.status.dao.StatusRepository;
import com.example.team5_be.status.domain.entity.StatusEntity;
import com.example.team5_be.user.dao.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MissionService {
    private final MissionRepository missionRepository ;
    private final LevelRepository levelRepository;
    private final ModeRepository modeRepository;
    private final StatusRepository statusRepository;
    private final HabitRepository habitRepository;
    private final UserRepository userRepository;


    /*
    Mission RequestDTO
        private Integer userId;
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

        LevelEntity level = levelRepository.findById(request.getLevelId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid levelId: " + request.getLevelId()));

        ModeEntity mode = modeRepository.findById(request.getModeId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid modeId: " + request.getModeId()));

        StatusEntity status = statusRepository.findById(request.getStatusId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid statusId: " + request.getStatusId()));
        
        HabitEntity habit = habitRepository.findById(request.getHabitId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid habitId: " + request.getHabitId()));

        LocalDate startDate = LocalDate.now();

        if (level.getLevelDate() == null) {
            throw new IllegalArgumentException("Level duration is missing for levelId: " + request.getLevelId());
        }
        LocalDate endDate = startDate.plusDays(level.getLevelDate());

        MissionEntity entity = missionRepository.save(
                                    MissionEntity.builder()
                                            .habit(habit)
                                            .mode(mode)
                                            .level(level)
                                            .status(status)
                                            .missionName(request.getMissionName())
                                            .missionDefinition(request.getMissionDefinition())
                                            .missionStartDate(startDate)
                                            .missionEndDate(endDate)
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

    // 한 유저의 모든 미션 조회
    @Transactional
    public List<MissionResponseDTO> list(String userId) {
        System.out.println(">>>> Entered mission service : list");
        List<MissionEntity> entities = missionRepository.findByUser_UserId(userId);
        
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

    @Transactional
    public MissionEntity findById(Integer missionId) {
        return missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Mission not found"));
    }
    
}
