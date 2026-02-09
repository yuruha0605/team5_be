package com.example.team5_be.user.ctrl;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.team5_be.user.domain.dto.UserRequestDTO;
import com.example.team5_be.user.domain.dto.UserResponseDTO;
import com.example.team5_be.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 생성 및 로그인, 탈퇴 관련 명세서")
public class UserController {
    private final UserService userService ;

    //회원가입 암호 해싱처리 위해 
    private final PasswordEncoder passwordEncoder;
    
    @ApiResponses(
        {
            @ApiResponse(responseCode = "201", description = "데이터 입력 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
        }
    )
    @Operation(
        summary = "회원가입",
        description = "신규 회원 가입(email,password,name)" 
    )
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "사용자 가입 DTO",
        required = true,
        content = @Content(
            schema = @Schema(implementation = UserRequestDTO.class)
        ))
        @RequestBody UserRequestDTO request) {
        
        System.out.println(">>> user ctrl path : /signup");
        System.out.println(">>> params : "+request);

        //패스워드 해싱 작업을 추가
        request.setUserPassword(passwordEncoder.encode(request.getUserPassword()));

        UserResponseDTO response = userService.join(request);

        if(response != null) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else { 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();   
        }  
    }

     @ApiResponses(
        {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
        }
    )
    @Operation(
        summary = "로그인",
        description = "인증된 사용자 로그인(id,password)" 
    )
    @PostMapping("/signin")
    
    public ResponseEntity<UserResponseDTO> signIn(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "사용자 로그인 DTO",
        required = true,
        content = @Content(
            schema = @Schema(implementation = UserRequestDTO.class)
        ))
        @RequestBody UserRequestDTO request) {
      
        System.out.println(">>> user ctrl path : /signin");
        System.out.println(">>> params : "+request);

        Map<String,Object> map = userService.login(request);

        System.out.println(">>>> response body : "+(UserResponseDTO)(map.get("response")));
        System.out.println(">>>> response body : "+(String)(map.get("access")));
        System.out.println(">>>> response body : "+(String)(map.get("refresh")));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+(String)(map.get("access")) ) ;
        headers.add("Refresh-Token", (String)(map.get("refresh")) ) ;
        headers.add("Access-Control-Expose-Headers","Authorization, Refresh-Token");
        //CORS 문제 해결용
        //브라우저는 기본적으로 응답 헤더 중 일부(Content-Type, Cache-Control 등)만 읽을 수 있음
        //노출하고 싶은 커스텀 헤더(Authorization, Refresh-Token)를 Access-Control-Expose-Headers에 추가해야 브라우저에서 읽을 수 있음

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body((UserResponseDTO)(map.get("response")));
    }

    @PostMapping("/logout")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authorization){
        System.out.println(">>> user service logout header : "+authorization);
        String accessToken = authorization.replace("Bearer ", "");
        userService.logout(accessToken);

        return ResponseEntity.noContent().build();
        
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> withdraw(@AuthenticationPrincipal String userId) {
        userService.withdraw(userId);
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }
    
     

}
