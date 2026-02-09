package com.example.team5_be.user.domain.dto;



import com.example.team5_be.user.domain.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    
    private String userId, userPassword, userName, userJob, userInterest ;

    public static UserResponseDTO fromEntity(UserEntity entity) {
        return UserResponseDTO.builder()
                .userId(entity.getUserId())
                .userPassword(entity.getUserPassword())
                .userName(entity.getUserName())
                .userJob(entity.getUserJob())
                .userInterest(entity.getUserInterest())
                .build() ;
    }
}
