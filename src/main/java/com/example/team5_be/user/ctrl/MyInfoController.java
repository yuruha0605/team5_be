package com.example.team5_be.user.ctrl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.team5_be.user.domain.dto.MyInfoResponseDTO;
import com.example.team5_be.user.domain.dto.MyInfoUpdateRequestDTO;
import com.example.team5_be.user.service.MyInfoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class MyInfoController {
    private final MyInfoService myInfoService;

    @GetMapping("/me")
    public ResponseEntity<MyInfoResponseDTO> getMyPage(
            @AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(myInfoService.getMyPage(userId));
    }

    @PutMapping("/me")
    public ResponseEntity<MyInfoResponseDTO> updateMyPage(
            @AuthenticationPrincipal String userId,
            @RequestBody MyInfoUpdateRequestDTO dto) {
        
        return ResponseEntity.ok(myInfoService.updateMyPage(userId, dto));
    }
}