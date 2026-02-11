package com.example.team5_be.openai.ctrl;

import org.springframework.web.bind.annotation.*;

import com.example.team5_be.openai.domain.dto.HabitRecommendationResponseDTO;
import com.example.team5_be.openai.domain.dto.MissionRecommendationResponseDTO;
import com.example.team5_be.openai.service.OpenAIService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "OpenAI", description = "AI 추천 API")
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class OpenAIController {

    private final OpenAIService openAIService;

    @Operation(
        summary = "회원가입 시 습관 및 미션 추천",
        description = "사용자의 직업과 관심사를 기반으로 습관 3개와 각 습관별 미션 2개를 추천합니다."
    )
    @GetMapping("/recommend/habit")
    public HabitRecommendationResponseDTO recommendHabit(
        @RequestParam String userId
    ) {
        return openAIService.recommendHabitAndMission(userId);
    }

    @Operation(
        summary = "특정 습관의 미션 추천",
        description = "선택한 습관에 적합한 미션들을 추천합니다."
    )
    @GetMapping("/recommend/mission")
    public MissionRecommendationResponseDTO recommendMission(
        @RequestParam Long habitId,
        @RequestParam String userId
    ) {
        return openAIService.recommendMission(habitId, userId);
    }
}