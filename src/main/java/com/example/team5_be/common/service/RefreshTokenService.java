package com.example.team5_be.common.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
  
    private final RedisTemplate<String, Object> redisTemplate ;

    private static final long REFRESH_TOKEN_TTL =  60*60*24*7 ; //7일 

    public void saveToken(String id, String refreshToken) {
        System.out.println(">>>> RefreshTokenService save token");
        redisTemplate.opsForValue()
            .set("RT:"+id, refreshToken, REFRESH_TOKEN_TTL, TimeUnit.SECONDS);
    }

    public void deleteToken(String id) {
        System.out.println(">>>> RefreshTokenService delete token");
        redisTemplate.delete("RT:"+id) ;
    }

    //실제로 필요하진 않음, 그냥 확인용 코드
    public String findById (String id) {
        System.out.println(">>>> RefreshTokenService findByEmail");
        return (String)redisTemplate.opsForValue().get("RT:"+id) ;
    }

}
