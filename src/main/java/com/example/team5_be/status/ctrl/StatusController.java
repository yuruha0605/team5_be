package com.example.team5_be.status.ctrl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.team5_be.status.domain.dto.StatusResponseDTO;
import com.example.team5_be.status.service.StatusService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/status")
@RequiredArgsConstructor
@Tag(name = "Status", description = "상태 목록 API")
public class StatusController {
    private final StatusService statusService;

    @Operation(summary = "상태 목록 조회", description = "상태 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<StatusResponseDTO>> listStatuses() {
        return ResponseEntity.ok(statusService.listStatuses());
    }

    @Operation(summary = "상태 이름 검색", description = "입력한 키워드를 포함한 상태의 ID를 조회합니다.")
    @GetMapping("/search")
    public ResponseEntity<Integer> searchStatusId(@RequestParam("keyword") String keyword) {
        Integer statusId = statusService.searchStatusId(keyword);
        if (statusId == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(statusId);
    }
}
