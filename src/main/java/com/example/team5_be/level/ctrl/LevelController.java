package com.example.team5_be.level.ctrl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.team5_be.level.domain.dto.LevelResponseDTO;
import com.example.team5_be.level.service.LevelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/levels")
@RequiredArgsConstructor
@Tag(name = "Level", description = "레벨 목록 API")
public class LevelController {
    private final LevelService levelService;

    @Operation(summary = "레벨 목록 조회", description = "레벨 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<LevelResponseDTO>> listLevels() {
        return ResponseEntity.ok(levelService.listLevels());
    }

    @Operation(summary = "레벨 이름 검색", description = "입력한 키워드를 포함한 레벨의 ID를 조회합니다.")
    @GetMapping("/search")
    public ResponseEntity<Integer> searchLevelId(@RequestParam("keyword") String keyword) {
        Integer levelId = levelService.searchLevelId(keyword);
        if (levelId == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(levelId);
    }
}
