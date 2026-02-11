package com.example.team5_be.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyInfoUpdateRequestDTO {
    
    private String userPassword;     
    private String userName ;
    private Boolean profilePublic;
    private String userJob;
    private String userInterest;

}