package com.example.team5_be.common.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
   
    @Value("${jwt.secret}") //키(서명) 생성
    private String secret ;
   
    private final long ACCESS_TOKEN_EXPIRY = 1000L * 60 *30 ; //30분
    private final long REFRESH_TOKEN_EXPIRY = 1000L * 60 * 60 * 24 * 7 ; //7일
    
    //토큰에 서명
    private Key getStringKey() {
        System.out.println(">>>> Provider jwt secret : "+secret);
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰 생성
    // access token 발급
    public String createAT(String id) {
        System.out.println(">>>> Provider createAT : "+id);
        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ 1000*60*30))
                .signWith(getStringKey())
                .compact();
    }

    // refresh token 발급
    public String createRT(String id) {
         System.out.println(">>>> Provider createRT : "+id);
        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ 1000*60*60*24*7))
                .signWith(getStringKey())
                .compact();
    }

    // token에서 subject 추출
    // Bearer xxxxxx
    public String getUserIdFromToken(String token) {
        System.out.println(">>>> Provider getUserEmailFromToken token : "+token);
        if(token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        // id 정보 가져옴 
        Claims claims = Jwts.parser()
                            .setSigningKey(getStringKey())
                            .parseClaimsJws(token)
                            .getBody();
            
        return claims.getSubject() ;
    }

    public Date getExpiration(String token) {

    // Bearer 제거, 로그아웃
    if (token.startsWith("Bearer ")) {
        token = token.substring(7);
    }

    try {
        return Jwts.parserBuilder()
                .setSigningKey(getStringKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

    } catch (ExpiredJwtException e) {
        // 만료된 토큰도 exp는 필요함 (로그아웃용)
        return e.getClaims().getExpiration();
    }
    }

    public long getATE() {
        return ACCESS_TOKEN_EXPIRY ;

    }

     public long getRTE() {
        return REFRESH_TOKEN_EXPIRY ;
        
    }
}