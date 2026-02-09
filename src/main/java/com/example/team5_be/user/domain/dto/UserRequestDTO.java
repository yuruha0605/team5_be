package com.example.team5_be.user.domain.dto;



import com.example.team5_be.user.domain.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    
    // 추후 spring validation 이용한 패턴검증 진행 예정
    private String userId ;
    private String userPassword ;
    private String userName ;
    private String userJob ;
    private String userInterest;

    // factory method pattern (dto -> entity) : JPA 작업가능
    public UserEntity toEntity() {
        return UserEntity.builder()
                .userId(this.userId)
                .userPassword(this.userPassword)
                .userName(this.userName)
                .userJob(this.userJob)
                .userInterest(this.userInterest)
                .build();
    }

}
