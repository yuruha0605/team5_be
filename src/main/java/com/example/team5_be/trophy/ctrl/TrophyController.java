package com.example.team5_be.trophy.ctrl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.team5_be.trophy.domain.dto.TrophyDTO;
import com.example.team5_be.trophy.service.TrophyService;
import com.example.team5_be.user.dao.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/trophy")
@RequiredArgsConstructor
public class TrophyController {

    private final TrophyService trophyService;
    private final UserRepository userRepo;

    // 습관 완료 → 트로피 지급
    @PostMapping("/award/{habitId}")
    public ResponseEntity<String> awardTrophy(@PathVariable Integer habitId,
                                          @AuthenticationPrincipal String userId) {
        boolean awarded = trophyService.awardTrophy(userId, habitId);
        if (awarded) {
            return ResponseEntity.ok("Trophy awarded!");
        } else {
            return ResponseEntity.ok("Habit not completed or trophy already awarded.");
        }
    }

    // 유저 트로피 진열장 조회
    @GetMapping("/display")
    public ResponseEntity<List<TrophyDTO>> displayTrophies(@AuthenticationPrincipal String userId) {
        List<TrophyDTO> trophies = trophyService.getUserTrophies(userId);
        return ResponseEntity.ok(trophies);
    }
}