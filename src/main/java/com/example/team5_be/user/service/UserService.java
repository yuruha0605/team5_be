package com.example.team5_be.user.service;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.team5_be.common.exception.DuplicateUserIdException;
import com.example.team5_be.common.service.RefreshTokenService;
import com.example.team5_be.common.util.JwtProvider;
import com.example.team5_be.user.dao.UserRepository;
import com.example.team5_be.user.domain.dto.UserRequestDTO;
import com.example.team5_be.user.domain.dto.UserResponseDTO;
import com.example.team5_be.user.domain.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository ;
    private final RedisTemplate<String, String> redisTemplate;

    // token + redis 의존성 주입
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    // passwordEncoder 의존성 주입
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO join(UserRequestDTO request) {
        System.out.println(">>> user service signup");

        // PK 중복 체크
        if (userRepository.existsById(request.getUserId())) {
            throw new DuplicateUserIdException("이미 존재하는 사용자입니다.");
        }

        UserEntity user = userRepository.save(request.toEntity());

        return UserResponseDTO.fromEntity(user);
    }

    public Map<String, Object> login(UserRequestDTO request) {
        System.out.println(">>> user service signin");
        Map<String, Object> map = new HashMap<>();
        
        System.out.println(">>>  1. user service 사용자 조회");
        //hashing 버전
        UserEntity entity = userRepository
                                .findById(request.getUserId())
                                .orElseThrow(() -> new RuntimeException(">>>> 로그인 실패!!"));
        //plain vs encoded
        if (!passwordEncoder.matches(request.getUserPassword(), entity.getUserPassword())) {
            throw new RuntimeException("Password Not Matched");
        }

        System.out.println(">>>  2. user service 토큰 생성");
        String at = jwtProvider.createAT(entity.getUserId());
        String rt = jwtProvider.createRT(entity.getUserId());

        System.out.println(">>>  3. user service RT 토큰을 Redis에 저장");
        refreshTokenService.saveToken(entity.getUserId(), rt);

        map.put("response", UserResponseDTO.fromEntity(entity));
        map.put("access", at);
        map.put("refresh", rt);

        return map ;
    }

    public void logout(String accessToken) {
        System.out.println(">>>> user service logout");
        String id = jwtProvider.getUserIdFromToken(accessToken);
        refreshTokenService.deleteToken(id);

        long expiration = jwtProvider.getExpiration(accessToken).getTime()
            - System.currentTimeMillis();

        if (expiration > 0) {
        redisTemplate.opsForValue()
                .set("BL:" + accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
    }
    }

    public void withdraw(String userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        userRepository.delete(user);
    }

}
       
