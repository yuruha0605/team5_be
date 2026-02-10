package com.example.team5_be.habit.ctrl;

import com.example.team5_be.habit.domain.dto.*;
import com.example.team5_be.habit.service.HabitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Habit", description = "습관 관련 API")
@RestController
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;

    private String getLoginUserIdOrNull() {
        try {
            var auth = org.springframework.security.core.context.SecurityContextHolder
                    .getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) return null;
            String name = auth.getName();
            if (name == null || "anonymousUser".equals(name)) return null;
            return name;
        } catch (Exception e) {
            return null;
        }
    }

    // 1) 습관 카테고리(Tag) 조회
    @Operation(
            summary = "습관 태그 목록 조회",
            description = "전체 습관 카테고리(태그) 목록을 조회합니다."
    )
    @GetMapping("/habit/tag")
    public List<TagListResponseDTO> getTags() {
        return habitService.getTags();
    }

    // 2) 습관 단건 조회
    @Operation(
            summary = "습관 단건 조회",
            description = "habitId에 해당하는 습관의 상세 정보를 조회합니다."
    )
    @GetMapping("/habits/{habitId}")
    public HabitDetailResponseDTO getHabitDetail(
            @Parameter(description = "습관 ID", example = "10", required = true)
            @PathVariable Long habitId
    ) {
        return habitService.getHabitDetail(habitId, getLoginUserIdOrNull());
    }

    // 3) 태그별 전체 습관 목록 조회
    @Operation(
            summary = "태그별 전체 습관 목록 조회",
            description = "선택한 태그에 속한 전체 습관 목록을 조회합니다."
    )
    @GetMapping("/habit")
    public HabitListResponseDTO getHabitsByTag(
            @Parameter(description = "태그 ID", example = "1", required = true)
            @RequestParam Long tagId
    ) {
        return habitService.getHabitsByTag(tagId, getLoginUserIdOrNull());
    }

    // 4) 커스텀 습관 카탈로그에 추가
    @Operation(
            summary = "커스텀 습관 생성",
            description = "사용자가 직접 새로운 습관을 생성하여 카탈로그에 추가합니다."
    )
    @PostMapping("/habit/create")
    public HabitCreateResponseDTO createHabit(
            @RequestBody HabitCreateRequestDTO req
    ) {
        return habitService.createHabit(req);
    }

    // 5) 습관 챌린지 참여
    @Operation(
            summary = "습관 챌린지 참여",
            description = "선택한 습관 챌린지에 참여합니다."
    )
    @PostMapping("/habits/{habitId}/join")
    public HabitJoinResponseDTO joinHabit(
            @Parameter(description = "습관 ID", example = "10", required = true)
            @PathVariable Long habitId
    ) {
        return habitService.joinHabit(habitId, getLoginUserIdOrNull());
    }

    // 6) 참여 습관 수정
    @Operation(
            summary = "참여 중인 습관 정보 수정",
            description = "참여 중인 습관의 목표값, 단계값 등을 수정합니다."
    )
    @PatchMapping("/habits/{habitId}/join")
    public HabitJoinResponseDTO updateJoin(
            @Parameter(description = "습관 ID", example = "10", required = true)
            @PathVariable Long habitId,
            @RequestBody HabitJoinUpdateRequestDTO req
    ) {
        return habitService.updateJoin(habitId, getLoginUserIdOrNull(), req);
    }

    // 7) 참여 습관 삭제
    @Operation(
            summary = "습관 챌린지 참여 취소",
            description = "참여 중인 습관 챌린지를 취소합니다."
    )
    @DeleteMapping("/habits/{habitId}/join")
    public HabitUnJoinResponseDTO unjoinHabit(
            @Parameter(description = "습관 ID", example = "10", required = true)
            @PathVariable Long habitId
    ) {
        return habitService.unjoinHabit(habitId, getLoginUserIdOrNull());
    }
}
