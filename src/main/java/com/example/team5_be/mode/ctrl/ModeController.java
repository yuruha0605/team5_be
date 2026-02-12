package com.example.team5_be.mode.ctrl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.team5_be.mode.domain.dto.ModeResponseDTO;
import com.example.team5_be.mode.service.ModeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/mode")
@RequiredArgsConstructor
@Tag(name = "Mode", description = "모드 목록 API")
public class ModeController {
    private final ModeService modeService;

    @Operation(summary = "모드 목록 조회", description = "모드 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<ModeResponseDTO>> listModes() {
        return ResponseEntity.ok(modeService.listModes());
    }

    @Operation(summary = "모드 이름 검색", description = "입력한 키워드를 포함한 모드의 ID를 조회합니다.")
    @GetMapping("/search")
    public ResponseEntity<Integer> searchModeId(@RequestParam("keyword") String keyword) {
        Integer modeId = modeService.searchModeId(keyword);
        if (modeId == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(modeId);
    }
}
