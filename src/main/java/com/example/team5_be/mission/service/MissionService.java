package com.example.team5_be.mission.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.team5_be.habit.dao.HabitRepository;
import com.example.team5_be.habit.domain.entity.HabitEntity;
import com.example.team5_be.level.dao.LevelRepository;
import com.example.team5_be.level.domain.entity.LevelEntity;
import com.example.team5_be.comment.dao.CommentRepository;
import com.example.team5_be.mission.dao.MissionRepository;
import com.example.team5_be.mission.domain.dto.MissionRequestDTO;
import com.example.team5_be.mission.domain.dto.MissionResponseDTO;
import com.example.team5_be.mission.domain.entity.MissionEntity;
import com.example.team5_be.missionlog.dao.MissionLogRepository;
import com.example.team5_be.mode.dao.ModeRepository;
import com.example.team5_be.mode.domain.entity.ModeEntity;
import com.example.team5_be.status.dao.StatusRepository;
import com.example.team5_be.status.domain.entity.StatusEntity;
import com.example.team5_be.user.dao.UserRepository;
import com.example.team5_be.user.domain.entity.UserEntity;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MissionService {
    private final MissionRepository missionRepository;
    private final LevelRepository levelRepository;
    private final ModeRepository modeRepository;
    private final StatusRepository statusRepository;
    private final HabitRepository habitRepository;
    private final UserRepository userRepository;
    private final MissionLogRepository missionLogRepository;
    private final CommentRepository commentRepository;

    private static final int LEVEL_UP_DAYS = 60;
    private static final String STATUS_IN_PROGRESS_NAME = "진행중";
    private static final String MODE_LEVEL_UP_NAME = "레벨업";
    private static final String LEVEL_UP_DEFAULT_LEVEL_NAME = "레벨 1";


    // 미션 생성
    @Transactional
    public MissionResponseDTO createForUser(MissionRequestDTO request, String userId) {
        System.out.println(">>>> Entered mission service : create");

        ModeEntity mode = modeRepository.findById(request.getModeId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid modeId: " + request.getModeId()));

        LevelEntity level;
        if (MODE_LEVEL_UP_NAME.equals(mode.getModeName())) {
            level = levelRepository.findByLevelName(LEVEL_UP_DEFAULT_LEVEL_NAME)
                .orElseThrow(() -> new IllegalArgumentException("Invalid levelName: " + LEVEL_UP_DEFAULT_LEVEL_NAME));
        } else {
            if (request.getLevelId() == null) {
                throw new IllegalArgumentException("levelId is required for mode: " + mode.getModeName());
            }
            level = levelRepository.findById(request.getLevelId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid levelId: " + request.getLevelId()));
        }

        StatusEntity status = statusRepository.findByStatusName(STATUS_IN_PROGRESS_NAME)
            .orElseThrow(() -> new IllegalArgumentException("Invalid statusName: " + STATUS_IN_PROGRESS_NAME));
        
        HabitEntity habit = habitRepository.findById(request.getHabitId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid habitId: " + request.getHabitId()));

        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid userId: " + userId));

        LocalDate startDate = LocalDate.now();

        LocalDate endDate;
        if (MODE_LEVEL_UP_NAME.equals(mode.getModeName())) {
            endDate = startDate.plusDays(LEVEL_UP_DAYS);
        } else {
            if (level.getLevelDate() == null) {
                throw new IllegalArgumentException("Level duration is missing for levelId: " + request.getLevelId());
            }
            endDate = startDate.plusDays(level.getLevelDate());
        }

        MissionEntity entity = missionRepository.save(
                                    MissionEntity.builder()
                                            .user(user)
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





    @Transactional
    public MissionResponseDTO readForUser(Integer missionId, String userId) {
        System.out.println(">>>> Entered mission service : readForUser");
        MissionEntity entity = missionRepository.findByMissionIdAndUser_UserId(missionId, userId)
                .orElseThrow(() -> new RuntimeException("read fail"));

        return MissionResponseDTO.fromEntity(entity);
    }


    // 로그인한 유저의 미션 목록 조회
    @Transactional
    public List<MissionResponseDTO> listForUser(String userId) {
        System.out.println(">>>> Entered mission service : list");
        List<MissionEntity> entities = missionRepository.findByUser_UserId(userId);
        
        List<MissionResponseDTO> responses = entities.stream()
                                                .map(MissionResponseDTO::fromEntity)
                                                .toList();
        return responses ;
    }


    // 로그인한 유저의 미션 이름으로 검색
    @Transactional
    public List<MissionResponseDTO> searchByName(String userId, String keyword) {
        System.out.println(">>>> Entered mission service : searchByName");
        List<MissionEntity> entities = missionRepository
                .findByUser_UserIdAndMissionNameContainingIgnoreCase(userId, keyword);

        return entities.stream()
                .map(MissionResponseDTO::fromEntity)
                .toList();
    }



    // 로그인한 유저의 미션 수정
    @Transactional
    public MissionResponseDTO updateForUser(Integer missionId, MissionRequestDTO request, String userId) {
        MissionEntity entity = missionRepository.findByMissionIdAndUser_UserId(missionId, userId)
                .orElseThrow(() -> new RuntimeException("read fail"));

        LevelEntity level = entity.getLevel();
        LocalDate endDate = entity.getMissionEndDate();

        if (request.getLevelId() != null && !MODE_LEVEL_UP_NAME.equals(entity.getMode().getModeName())) {
            LevelEntity newLevel = levelRepository.findById(request.getLevelId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid levelId: " + request.getLevelId()));
            level = newLevel;
            endDate = entity.getMissionStartDate().plusDays(newLevel.getLevelDate());
        }

        MissionEntity updated = MissionEntity.builder()
                .missionId(entity.getMissionId())
                .user(entity.getUser())
                .habit(entity.getHabit())
                .mode(entity.getMode())
                .level(level)
                .status(entity.getStatus())
                .missionName(request.getMissionName())
                .missionDefinition(request.getMissionDefinition())
                .missionStartDate(entity.getMissionStartDate())
                .missionEndDate(endDate)
                .build();

        missionRepository.save(updated);
        return MissionResponseDTO.fromEntity(updated);
    }

    


    @Transactional
    public boolean deleteForUser(Integer missionId, String userId) {
        System.out.println(">>>> Entered mission service : deleteForUser");

        MissionEntity entity = missionRepository.findByMissionIdAndUser_UserId(missionId, userId)
                .orElseThrow(() -> new RuntimeException("read fail"));

        // Remove dependent rows first to satisfy FK constraints.
        missionLogRepository.deleteByMission_MissionId(entity.getMissionId());
        commentRepository.deleteByMission_MissionId(entity.getMissionId());
        missionRepository.deleteById(entity.getMissionId());
        return true;
    }
    
}
