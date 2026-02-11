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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.team5_be.user.domain.dto.ResetPasswordRequestDTO;
import com.example.team5_be.user.domain.dto.UserRequestDTO;
import com.example.team5_be.user.domain.dto.UserResponseDTO;
import com.example.team5_be.user.domain.entity.UserEntity;
import com.example.team5_be.user.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService ;

    //회원가입 암호 해싱처리 위해 
    private final PasswordEncoder passwordEncoder;
    
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody UserRequestDTO request) {
        
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

     
    @PostMapping("/signin")
    public ResponseEntity<UserResponseDTO> signIn(@RequestBody UserRequestDTO request) {
      
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

    //로그아웃
    @PostMapping("/logout")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authorization){
        System.out.println(">>> user service logout header : "+authorization);
        String accessToken = authorization.replace("Bearer ", "");
        userService.logout(accessToken);

        return ResponseEntity.noContent().build();
        
    }

    //탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<String> withdraw(@AuthenticationPrincipal String userId) {
        userService.withdraw(userId);
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }
    

    /////아이디랑 이름으로 회원조회 하고 비밀번호 변경   
    @PostMapping("/find") //민감정보라 post사용
    public ResponseEntity<String> findUser(@RequestParam String userId,
                                           @RequestParam String userName) {
        UserEntity user = userService.findUserByIdAndName(userId, userName);
        // 프론트에서 비밀번호 재설정 화면으로 이동시키면 됨
        return ResponseEntity.ok("User verified. Proceed to reset password.");
    }

    // 비밀번호 재설정
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        UserEntity user = userService.findById(request.getUserId());
        userService.updatePassword(user, request.getNewPassword());
        return ResponseEntity.ok("Password updated successfully.");
    }

     

}
