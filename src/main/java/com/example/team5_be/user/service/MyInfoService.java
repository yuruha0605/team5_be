package com.example.team5_be.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.team5_be.user.dao.UserRepository;
import com.example.team5_be.user.domain.dto.MyInfoResponseDTO;
import com.example.team5_be.user.domain.dto.MyInfoUpdateRequestDTO;
import com.example.team5_be.user.domain.entity.UserEntity;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyInfoService {
    
    private final UserRepository userRepository ;
    private final PasswordEncoder passwordEncoder;

    public MyInfoResponseDTO getMyPage(String userId) {
    UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("유저 없음"));

    return MyInfoResponseDTO.from(user);
    }

    @Transactional
    public MyInfoResponseDTO updateMyPage(String userId, MyInfoUpdateRequestDTO dto) {
    
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

        return MyInfoResponseDTO.from(user);
    }
}
