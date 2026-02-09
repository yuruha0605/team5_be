package com.example.team5_be.common.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    
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
            chain.doFilter(request, response); //ctrl와 연결
            return ;
        }

        // header , token 검증을 해서 통과 또는 리첵
        String authHeader = request.getHeader("Authorization");
        System.out.println(">>>> JwtFilter Authorization : "+authHeader);
        if( authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println(">>>> JwtFilter not Authorization : ");
            chain.doFilter(request, response); 
            return ;
        }

        String token = authHeader.substring(7);
        System.out.println(">>>> JwtFilter token : "+token);
        System.out.println(">>>> JwtFilter token validation check ");
        try {
            Claims claims = Jwts.parserBuilder()
                                .setSigningKey(key)
                                .build()
                                .parseClaimsJws(token)
                                .getBody();
            String id = claims.getSubject() ;
            System.out.println(">>>> JwtAuthenticationFilter claims get id : "+id);

            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    id,
                    null
                );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);


        } catch (Exception e) {
            e.printStackTrace();
        }   
        
        chain.doFilter(request, response);
    }  
}
