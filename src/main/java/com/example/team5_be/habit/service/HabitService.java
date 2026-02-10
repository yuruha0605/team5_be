package com.example.team5_be.habit.service;

import com.example.team5_be.habit.dao.*;
import com.example.team5_be.habit.domain.dto.*;
import com.example.team5_be.habit.domain.entity.*;
import com.example.team5_be.status.dao.StatusRepository;
import com.example.team5_be.status.domain.entity.StatusEntity;
import com.example.team5_be.user.domain.entity.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HabitService {

    private final HabitRepository habitRepository;
    private final HabitRelationshipRepository habitRelationshipRepository;
    private final TagRepository tagRepository;
    private final StyleRepository styleRepository;
    private final StatusRepository statusRepository;

    // 1) 태그 조회: GET /habit/tag
    public List<TagListResponseDTO> getTags() {
        return tagRepository.findAll().stream()
                .map(t -> TagListResponseDTO.builder()
                        .tagId(t.getTagId())
                        .tagName(t.getTagName())
                        .build())
                .toList();
    }

    // 2) 습관 단건 조회: GET /habits/{habitId}
    public HabitDetailResponseDTO getHabitDetail(Integer habitId, String loginUserIdOrNull) {
        HabitEntity habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new EntityNotFoundException("Habit not found: " + habitId));

        boolean joined = false;
        HabitStatus myStatus = null;

        if (loginUserIdOrNull != null) {
            var relOpt = habitRelationshipRepository.findById_UserIdAndId_HabitId(loginUserIdOrNull, habitId);
            if (relOpt.isPresent()) {
                joined = true;
                myStatus = mapStatusEntityToEnum(relOpt.get().getStatus());
            }
        }

        return HabitDetailResponseDTO.builder()
                .habitId(habit.getHabitId())
                .tagId(habit.getTag().getTagId())
                .styleId(habit.getStyle().getStyleId())
                .habitName(habit.getHabitName())
                .habitDefinition(habit.getHabitDefinition())
                .startValue(habit.getStartValue())
                .stepValue(habit.getStepValue())
                .targetValue(habit.getTargetValue())
                .unit(habit.getUnit())
                .isJoined(joined)
                .myStatus(myStatus)
                .build();
    }

    // 3) 태그별 전체 습관 목록: GET /habit?tagId=
    public HabitListResponseDTO getHabitsByTag(Integer tagId, String loginUserIdOrNull) {
        tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found: " + tagId));

        List<HabitEntity> habits = habitRepository.findAllByTag_TagId(tagId);

        List<HabitListResponseDTO.Item> items = habits.stream()
                .map(h -> {
                    boolean joined = false;
                    HabitStatus myStatus = null;

                    if (loginUserIdOrNull != null) {
                        var relOpt = habitRelationshipRepository.findById_UserIdAndId_HabitId(loginUserIdOrNull, h.getHabitId());
                        if (relOpt.isPresent()) {
                            joined = true;
                            myStatus = mapStatusEntityToEnum(relOpt.get().getStatus());
                        }
                    }

                    return HabitListResponseDTO.Item.builder()
                            .habitId(h.getHabitId())
                            .styleId(h.getStyle().getStyleId())
                            .habitName(h.getHabitName())
                            .habitDefinition(h.getHabitDefinition())
                            .startValue(h.getStartValue())
                            .stepValue(h.getStepValue())
                            .targetValue(h.getTargetValue())
                            .unit(h.getUnit())
                            .isJoined(joined)
                            .myStatus(myStatus)
                            .build();
                })
                .toList();

        return HabitListResponseDTO.builder()
                .tagId(tagId)
                .habits(items)
                .build();
    }

    // 4) 커스텀 습관 추가: POST /habit/create
    @Transactional
    public HabitCreateResponseDTO createHabit(HabitCreateRequestDTO req) {
        TagEntity tag = tagRepository.findById(req.getTagId())
                .orElseThrow(() -> new EntityNotFoundException("Tag not found: " + req.getTagId()));

        StyleEntity style = styleRepository.findById(req.getStyleId())
                .orElseThrow(() -> new EntityNotFoundException("Style not found: " + req.getStyleId()));

        HabitEntity saved = habitRepository.save(
                HabitEntity.builder()
                        .tag(tag)
                        .style(style)
                        .habitName(req.getHabitName())
                        .habitDefinition(req.getHabitDefinition())
                        .startValue(req.getStartValue())
                        .stepValue(req.getStepValue())
                        .targetValue(req.getTargetValue())
                        .unit(req.getUnit())
                        .build()
        );

        return HabitCreateResponseDTO.builder()
                .habitId(saved.getHabitId())
                .message("HABIT_CREATED")
                .build();
    }

    // 5) 습관 참여: POST /habits/{habitId}/join
    @Transactional
    public HabitJoinResponseDTO joinHabit(Integer habitId, String loginUserId) {
        if (loginUserId == null) throw new IllegalStateException("Login required");

        HabitEntity habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new EntityNotFoundException("Habit not found: " + habitId));

        if (habitRelationshipRepository.existsById_UserIdAndId_HabitId(loginUserId, habitId)) {
            throw new IllegalStateException("Already joined");
        }

        StatusEntity defaultStatus = findStatusEntityByEnum(HabitStatus.IN_PROGRESS); // 기본 진행중

        UserEntity userRef = new UserEntity();
        userRef.setUserId(loginUserId);

        HabitRelationshipEntity.HabitRelationshipId id =
                new HabitRelationshipEntity.HabitRelationshipId(loginUserId, habitId);

        HabitRelationshipEntity rel = HabitRelationshipEntity.builder()
                .id(id)
                .user(userRef)
                .habit(habit)
                .status(defaultStatus)
                .build();

        habitRelationshipRepository.save(rel);

        return HabitJoinResponseDTO.builder()
                .habitId(habitId)
                .status(HabitStatus.IN_PROGRESS)
                .message("HABIT_JOINED")
                .build();
    }

    // 6) 참여 습관 수정: PATCH /habits/{habitId}/join
    @Transactional
    public HabitJoinResponseDTO updateJoin(Integer habitId, String loginUserId, HabitJoinUpdateRequestDTO req) {
        if (loginUserId == null) throw new IllegalStateException("Login required");
        if (req == null || req.getStatus() == null) throw new IllegalArgumentException("status is required");

        HabitRelationshipEntity rel = habitRelationshipRepository
                .findById_UserIdAndId_HabitId(loginUserId, habitId)
                .orElseThrow(() -> new EntityNotFoundException("Join relationship not found"));

        StatusEntity newStatus = findStatusEntityByEnum(req.getStatus());
        rel.changeStatus(newStatus);


        return HabitJoinResponseDTO.builder()
                .habitId(habitId)
                .status(req.getStatus())
                .message("JOIN_UPDATED")
                .build();
    }

    // 7) 참여 습관 삭제: DELETE /habits/{habitId}/join
    @Transactional
    public HabitUnJoinResponseDTO unjoinHabit(Integer habitId, String loginUserId) {
        if (loginUserId == null) throw new IllegalStateException("Login required");

        if (!habitRelationshipRepository.existsById_UserIdAndId_HabitId(loginUserId, habitId)) {
            throw new EntityNotFoundException("Join relationship not found");
        }

        habitRelationshipRepository.deleteById_UserIdAndId_HabitId(loginUserId, habitId);

        return HabitUnJoinResponseDTO.builder()
                .habitId(habitId)
                .message("HABIT_UNJOINED")
                .build();
    }

    // ===== 내부 매핑 =====

    private HabitStatus mapStatusEntityToEnum(StatusEntity statusEntity) {
        if (statusEntity == null) return null;
        return HabitStatus.from(statusEntity.getStatusName());
    }

    private StatusEntity findStatusEntityByEnum(HabitStatus status) {
        return statusRepository.findByStatusName(status.getValue())
                .orElseThrow(() -> new EntityNotFoundException("Status not found in DB: " + status.getValue()));
    }
}
