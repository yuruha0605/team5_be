package com.example.team5_be.trophy.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.team5_be.habit.dao.HabitRelationshipRepository;
import com.example.team5_be.habit.domain.entity.HabitEntity;
import com.example.team5_be.habit.domain.entity.HabitRelationshipEntity;
import com.example.team5_be.mission.domain.entity.MissionEntity;
import com.example.team5_be.trophy.dao.TrophyRelationshipRepository;
import com.example.team5_be.trophy.dao.TrophyRepository;
import com.example.team5_be.trophy.domain.dto.TrophyDTO;
import com.example.team5_be.trophy.domain.entity.TrophyEntity;
import com.example.team5_be.trophy.domain.entity.TrophyRelationshipEntity;
import com.example.team5_be.user.domain.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrophyService {

    private final TrophyRelationshipRepository trophyRelRepo;
    private final TrophyRepository trophyRepo;
    private final HabitRelationshipRepository habitRelRepo;

    // 미션 완료 시 트로피 지급 (트로피를 DB에 새로 생성)
    @Transactional
    public boolean awardTrophy(String userId, Integer habitId) {
        // 1. HabitRelationship 조회
        HabitRelationshipEntity hr = habitRelRepo.findById_UserIdAndId_HabitId(userId, habitId)
                .orElseThrow(() -> new RuntimeException("HabitRelationship not found"));

        // 2. 완료 여부 확인
        if (!hr.getStatus().getStatusName().equals("COMPLETED")) {
            return false; // 완료 X → 트로피 지급 안함
        }

        HabitEntity habit = hr.getHabit();
        UserEntity user = hr.getUser();

        // 3. 트로피 존재 확인
        TrophyEntity trophy = trophyRepo.findByHabit(habit)
                .orElseGet(() -> {
                    TrophyEntity newTrophy = TrophyEntity.builder()
                            .trophyName(habit.getHabitName() + " 트로피")
                            .habit(habit)
                            .build();
                    return trophyRepo.save(newTrophy);
                });

        // 4. 이미 지급 여부 확인
        boolean alreadyAwarded = trophyRelRepo.existsByUser_UserIdAndTrophy_TrophyId(user.getUserId(), trophy.getTrophyId());
        if (alreadyAwarded) return false;

        // 5. 트로피 지급
        TrophyRelationshipEntity tr = TrophyRelationshipEntity.builder()
                .user(user)
                .trophy(trophy)
                .build();
        trophyRelRepo.save(tr);

        return true;
    }

    // 획득한 트로피 진열장 조회
    @Transactional(readOnly = true)
    public List<TrophyDTO> getUserTrophies(String userId) {
        List<TrophyRelationshipEntity> relList = trophyRelRepo.findAllByUserId(userId);

        return relList.stream()
                .map(tr -> {
                    TrophyEntity trophy = tr.getTrophy();
                    HabitEntity habit = trophy.getHabit();
                    return TrophyDTO.builder()
                            .trophyId(trophy.getTrophyId())
                            .trophyName(trophy.getTrophyName())
                            .habitId(habit.getHabitId())
                            .habitName(habit.getHabitName())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
