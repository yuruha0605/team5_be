package com.example.team5_be.common.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final RedisTemplate<String, String> redisTemplate;
    
    @Value("${jwt.secret}") 
    private String secret ;
    private Key key;

    @PostConstruct 
    private void init() {
        System.out.println(">>>> JwtFilter init jwt secret : "+secret);
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, 
                                    FilterChain chain) throws ServletException, IOException {
        System.out.println(">>>> JwtAuthenticationFilter doFilter");
        
        String endPoint = request.getRequestURI();
        System.out.println(">>> User endpoint : "+endPoint);
        String method = request.getMethod() ;
        System.out.println(">>>> JwtFilter Request Method : "+method);
                            
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {

        chain.doFilter(request, response);
        return;
        }

        if (endPoint.startsWith("/user/resetPassword")) {
          
            chain.doFilter(request, response);
            return;
        }

        // header , token 검증을 해서 통과 또는 리첵
        String authHeader = request.getHeader("Authorization");
        System.out.println(">>>> JwtFilter Authorization : "+authHeader);
        if( authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println(">>>> JwtFilter not Authorization ");
            chain.doFilter(request, response); 
            return ;
        }

        String token = authHeader.substring(7);
        System.out.println(">>>> JwtFilter token : "+token);
        System.out.println(">>>> JwtFilter token validation check ");
        try {

            //블랙리스트 체크
            if (redisTemplate.hasKey("BL:" + token)) {
            System.out.println(">>>> JwtAuthenticationFilter BLACKLIST TOKEN");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그아웃된 토큰");
            return;
            }

            Claims claims = Jwts.parserBuilder()
                                .setSigningKey(key)
                                .build()
                                .parseClaimsJws(token)
                                .getBody();
            
            String id = claims.getSubject() ;
            System.out.println(">>>> JwtAuthenticationFilter claims get id : "+id);

            List<GrantedAuthority> authorities =
            List.of(new SimpleGrantedAuthority("ROLE_USER"));

            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    id,null,authorities 
                );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);


        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            chain.doFilter(request, response);
            return;
        }  
        
        chain.doFilter(request, response);
    }  
}
