package com.example.team5_be.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.team5_be.user.dao.UserRepository;
import com.example.team5_be.user.domain.dto.MyPageResponseDTO;
import com.example.team5_be.user.domain.dto.MyPageUpdateRequestDTO;
import com.example.team5_be.user.domain.entity.UserEntity;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageService {
    
    private final UserRepository userRepository ;
    private final PasswordEncoder passwordEncoder;

    public MyPageResponseDTO getMyPage(String userId) {
    UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("유저 없음"));

    return MyPageResponseDTO.from(user);
    }

    @Transactional
    public MyPageResponseDTO updateMyPage(String userId, MyPageUpdateRequestDTO dto) {
    
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        // 비밀번호 변경
        if (dto.getUserPassword() != null && !dto.getUserPassword().isEmpty()) {
            user.setUserPassword(passwordEncoder.encode(dto.getUserPassword()));
        }

        if (dto.getUserName() != null && !dto.getUserName().isBlank()) user.setUserName(dto.getUserName());
        if (dto.getProfilePublic() != null) user.setProfilePublic(dto.getProfilePublic());
        if (dto.getUserJob() != null && !dto.getUserJob().isBlank()) user.setUserJob(dto.getUserJob());
        if (dto.getUserInterest() != null && !dto.getUserInterest().isBlank()) user.setUserInterest(dto.getUserInterest());

        userRepository.save(user);

        return MyPageResponseDTO.from(user);
    }


}
