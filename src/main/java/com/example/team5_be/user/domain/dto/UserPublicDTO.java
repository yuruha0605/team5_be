package com.example.team5_be.user.domain.dto;

import com.example.team5_be.user.domain.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPublicDTO {
    private String userId;
    private String userName;
    private String userJob;
    private String userInterest;

    public static UserPublicDTO fromEntity(UserEntity user) {
        return UserPublicDTO.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userJob(user.getUserJob())
                .userInterest(user.getUserInterest())
                .build();
    }
}
