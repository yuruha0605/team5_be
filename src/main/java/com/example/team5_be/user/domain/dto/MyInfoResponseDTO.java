package com.example.team5_be.user.domain.dto;

import com.example.team5_be.user.domain.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyInfoResponseDTO {
    private String userId;
    private String userName;
    private Boolean profilePublic;
    private String userJob;
    private String userInterest;

    public static MyInfoResponseDTO from(UserEntity user) {
        return MyInfoResponseDTO.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .profilePublic(user.getProfilePublic())
                .userJob(user.getUserJob())
                .userInterest(user.getUserInterest())
                .build();
    }
}
