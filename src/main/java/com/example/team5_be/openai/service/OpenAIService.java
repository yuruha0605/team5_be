package com.example.team5_be.openai.service;

import com.example.team5_be.habit.dao.HabitRepository;
import com.example.team5_be.habit.domain.entity.HabitEntity;
import com.example.team5_be.openai.domain.dto.HabitRecommendationResponseDTO;
import com.example.team5_be.openai.domain.dto.MissionRecommendationResponseDTO;
import com.example.team5_be.user.dao.UserRepository;
import com.example.team5_be.user.domain.entity.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAIService {

    @Value("${spring.ai.openai.api.url}")
    private String openaiUrl;

    @Value("${spring.ai.openai.api.key}")
    private String openaiKey;

    @Value("${spring.ai.openai.model}")
    private String model;

    private final UserRepository userRepository;
    private final HabitRepository habitRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final org.springframework.data.redis.core.RedisTemplate<String, String> redisTemplate;

    // 1. 회원가입 시 습관/미션 추천
    public HabitRecommendationResponseDTO recommendHabitAndMission(String userId) {

        // ===== 캐시 확인 =====
        String cacheKey = "ai:habit:recommend:" + userId;
        try {
            String cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached != null) {
                System.out.println(">>>> 캐시에서 습관 추천 반환: " + userId);
                return objectMapper.readValue(cached, HabitRecommendationResponseDTO.class);
            }
        } catch (Exception e) {
            System.out.println(">>>> Redis 조회 실패, GPT 직접 호출: " + e.getMessage());
        }
        // ====================

        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        String prompt = String.format(
            "사용자 정보:\n" +
            "- 직업: %s\n" +
            "- 관심사: %s\n\n" +
            "이 사용자에게 적합한 습관 3개를 추천하고, 각 습관마다 미션 2개를 추천해주세요.\n" +
            "응답은 반드시 아래 JSON 형식으로만 작성해주세요. 다른 설명은 포함하지 마세요:\n\n" +
            "{\n" +
            "  \"recommendedHabits\": [\n" +
            "    {\n" +
            "      \"habitName\": \"습관 이름\",\n" +
            "      \"habitDefinition\": \"습관 설명\",\n" +
            "      \"tagName\": \"운동\",\n" +
            "      \"styleId\": 2,\n" +
            "      \"recommendedMissions\": [\n" +
            "        {\n" +
            "          \"missionName\": \"미션 이름\",\n" +
            "          \"missionDefinition\": \"미션 설명\",\n" +
            "          \"levelName\": \"3일\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}\n\n" +
            "참고:\n" +
            "- tagName은 \"운동\", \"식사\", \"취미\", \"공부\", \"수면\", \"기타\" 중 하나\n" +
            "- styleId는 1(반복형) 또는 2(점진형)\n" +
            "- levelName은 \"1일\", \"3일\", \"1주\", \"2주\", \"1달\", \"2달\" 중 하나",
            user.getUserJob(),
            user.getUserInterest()
        );

        String gptResponse = callGPT(prompt);
        HabitRecommendationResponseDTO result = parseHabitRecommendation(gptResponse);  // ← 변수에 담기!

        // ===== 캐시 저장 (24시간) =====
        try {
            String json = objectMapper.writeValueAsString(result);
            redisTemplate.opsForValue().set(cacheKey, json,
                java.time.Duration.ofHours(24));
            System.out.println(">>>> 습관 추천 캐시 저장 완료: " + userId);
        } catch (Exception e) {
            System.out.println(">>>> Redis 저장 실패 (무시): " + e.getMessage());
        }
        // ==============================

        return result;
    }

// 2. 특정 습관의 미션 추천
public MissionRecommendationResponseDTO recommendMission(Integer habitId, String userId) {

    // ===== 캐시 확인 =====
    String cacheKey = "ai:mission:recommend:" + habitId + ":" + userId;
    try {
        String cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            System.out.println(">>>> 캐시에서 미션 추천 반환: habitId=" + habitId + ", userId=" + userId);
            return objectMapper.readValue(cached, MissionRecommendationResponseDTO.class);
        }
    } catch (Exception e) {
        System.out.println(">>>> Redis 조회 실패, GPT 직접 호출: " + e.getMessage());
    }
    // ====================

    HabitEntity habit = habitRepository.findById(habitId)
        .orElseThrow(() -> new EntityNotFoundException("Habit not found: " + habitId));

    UserEntity user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

    String prompt = String.format(
        "습관 정보:\n" +
        "- 습관명: %s\n" +
        "- 설명: %s\n\n" +
        "사용자 정보:\n" +
        "- 직업: %s\n" +
        "- 관심사: %s\n\n" +
        "이 습관에 적합한 미션 5개를 추천해주세요.\n" +
        "응답은 반드시 아래 JSON 형식으로만 작성해주세요:\n\n" +
        "{\n" +
        "  \"missions\": [\n" +
        "    {\n" +
        "      \"missionName\": \"미션 이름\",\n" +
        "      \"missionDefinition\": \"미션 설명\",\n" +
        "      \"levelName\": \"3일\"\n" +
        "    }\n" +
        "  ]\n" +
        "}\n\n" +
        "levelName은 \"1일\", \"3일\", \"1주\", \"2주\", \"1달\", \"2달\" 중 하나",
        habit.getHabitName(),
        habit.getHabitDefinition(),
        user.getUserJob(),
        user.getUserInterest()
    );

    String gptResponse = callGPT(prompt);
    Map<String, Object> parsed = parseJSON(gptResponse);

    @SuppressWarnings("unchecked")
    List<Map<String, String>> missionList = (List<Map<String, String>>) parsed.get("missions");

    List<MissionRecommendationResponseDTO.Mission> missions = missionList.stream()
        .map(m -> MissionRecommendationResponseDTO.Mission.builder()
            .missionName(m.get("missionName"))
            .missionDefinition(m.get("missionDefinition"))
            .levelName(m.get("levelName"))
            .build())
        .toList();

    MissionRecommendationResponseDTO result = MissionRecommendationResponseDTO.builder()
        .habitId(habitId)
        .habitName(habit.getHabitName())
        .missions(missions)
        .build();

    // ===== 캐시 저장 (12시간) =====
    try {
        String json = objectMapper.writeValueAsString(result);
        redisTemplate.opsForValue().set(cacheKey, json,
            java.time.Duration.ofHours(12));
        System.out.println(">>>> 미션 추천 캐시 저장 완료: habitId=" + habitId + ", userId=" + userId);
    } catch (Exception e) {
        System.out.println(">>>> Redis 저장 실패 (무시): " + e.getMessage());
    }
    // ==============================

    return result;
}
    // 3. 응원 메시지 생성
    public String generateEncouragementMessage(String missionName, boolean isChecked) {
        String prompt;

        if (isChecked) {
            prompt = String.format(
                "미션 '%s'을(를) 성공적으로 완료했습니다.\n\n" +
                "사용자를 격려하는 짧은 메시지를 한 문장으로 작성해주세요. " +
                "이모지 1개를 포함하고, 친근하고 따뜻하게 작성해주세요. " +
                "30자 이내로 작성해주세요.",
                missionName
            );
        } else {
            prompt = String.format(
                "미션 '%s'을(를) 오늘은 완료하지 못했습니다.\n\n" +
                "사용자를 위로하고 다시 도전하도록 격려하는 짧은 메시지를 한 문장으로 작성해주세요. " +
                "이모지 1개를 포함하고, 친근하고 따뜻하게 작성해주세요. " +
                "30자 이내로 작성해주세요.",
                missionName
            );
        }

        try {
            String gptResponse = callGPT(prompt);
            return gptResponse.trim()
                .replaceAll("^[\"']|[\"']$", "");
        } catch (Exception e) {
            return isChecked ? "멋져요! 계속 해봐요! 💪" : "괜찮아요, 내일 다시 도전! 🌟";
        }
    }

    // GPT API 호출 (공통)
    private String callGPT(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiKey);

        Map<String, Object> requestBody = Map.of(
            "model", model,
            "messages", List.of(
                Map.of("role", "user", "content", prompt)
            ),
            "temperature", 0.7
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(openaiUrl, request, Map.class);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        @SuppressWarnings("unchecked")
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

        return (String) message.get("content");
    }

    // JSON 파싱
    private HabitRecommendationResponseDTO parseHabitRecommendation(String json) {
        try {
            String cleaned = json.replaceAll("```json\\s*", "").replaceAll("```\\s*", "").trim();
            return objectMapper.readValue(cleaned, HabitRecommendationResponseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse GPT response: " + json, e);
        }
    }

    private Map<String, Object> parseJSON(String json) {
        try {
            String cleaned = json.replaceAll("```json\\s*", "").replaceAll("```\\s*", "").trim();
            @SuppressWarnings("unchecked")
            Map<String, Object> result = objectMapper.readValue(cleaned, Map.class);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON: " + json, e);
        }
    }
}