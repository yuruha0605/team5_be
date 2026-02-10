package com.example.team5_be.user.ctrl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.team5_be.user.domain.dto.UserPublicDTO;
import com.example.team5_be.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserPublicDTO> getProfile(@PathVariable String userId,
                                              @AuthenticationPrincipal String viewerId) {  //현재 로그인 되어있는 userId 추출
        UserPublicDTO profile = userService.getPublicProfile(userId, viewerId);
        return ResponseEntity.ok(profile);
    }
}
