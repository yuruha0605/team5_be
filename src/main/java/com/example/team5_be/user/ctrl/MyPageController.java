package com.example.team5_be.user.ctrl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.team5_be.user.domain.dto.MyPageResponseDTO;
import com.example.team5_be.user.domain.dto.MyPageUpdateRequestDTO;
import com.example.team5_be.user.service.MyPageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class MyPageController {
    
    private final MyPageService myPageService;

    @GetMapping("/me")
    public ResponseEntity<MyPageResponseDTO> getMyPage(
            @AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(myPageService.getMyPage(userId));
    }

    @PutMapping("/me")
    public ResponseEntity<MyPageResponseDTO> updateMyPage(
            @AuthenticationPrincipal String userId,
            @RequestBody MyPageUpdateRequestDTO dto) {
        
        return ResponseEntity.ok(myPageService.updateMyPage(userId, dto));
    }
}
