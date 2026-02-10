package com.example.team5_be.common.health.ctrl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.team5_be.common.health.domain.dto.HealthResponseDTO;
import com.example.team5_be.common.health.service.HealthService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/health")
@RequiredArgsConstructor
public class HealthController {

    private final HealthService healthService ;

    @GetMapping("/create") //스웨거나 포스트맨 아닌 브라우저로 테스트할 때는 getmapping밖에 안됨
    public ResponseEntity<HealthResponseDTO> create() {
        System.out.println(">>>> health ctrl create check");
        return ResponseEntity.status(HttpStatus.CREATED).body(healthService.create());
    }

    @GetMapping("/read") 
    public ResponseEntity<HealthResponseDTO> read() {
        System.out.println(">>>> health ctrl read check");
        return ResponseEntity.status(HttpStatus.OK).body(healthService.read());
    }

    @GetMapping("/update") 
    public ResponseEntity<HealthResponseDTO> update() {
        System.out.println(">>>> health ctrl update check");
        return ResponseEntity.status(HttpStatus.OK).body(healthService.update());
    }
    @GetMapping("/delete") 
    public ResponseEntity<Boolean> delete() {
        System.out.println(">>>> health ctrl delete check");
        return ResponseEntity.status(HttpStatus.OK).body(healthService.delete());
    }
    
}
