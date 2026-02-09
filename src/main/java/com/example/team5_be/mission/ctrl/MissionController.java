package com.example.team5_be.mission.ctrl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.team5_be.mission.domain.dto.MissionRequestDTO;
import com.example.team5_be.mission.domain.dto.MissionResponseDTO;
import com.example.team5_be.mission.service.MissionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService ;

    // 미션 생성
    @PostMapping("/create")
    public ResponseEntity<MissionResponseDTO> create(@RequestBody MissionRequestDTO request) {
        System.out.println(">>>> mission ctrl create check");
        MissionResponseDTO response = missionService.create(request);

        if (response != null)   return ResponseEntity.status(HttpStatus.CREATED).body(response);       // 201
        else                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);           // 400
    }


    // mission ID로 미션 단건 조회
    @GetMapping("/read/{missionId}") 
    public ResponseEntity<MissionResponseDTO> read(@PathVariable("missionId") Integer missionId) {
        System.out.println(">>>> mission ctrl read check");

        MissionResponseDTO response = missionService.read(missionId);


        if (response != null)   return ResponseEntity.status(HttpStatus.OK).body(response);            // 200
        else                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);   // 404
    }


    // 모든 미션 조회
    @GetMapping("/list")
    public ResponseEntity<List<MissionResponseDTO>> list() {
        System.out.println(">>>> mission ctrl list check");

        return ResponseEntity.status(HttpStatus.OK).body(missionService.list());
    }


    // 미션 수정
    @PutMapping("/update/{missionId}")
    public ResponseEntity<MissionResponseDTO> update(@PathVariable("missionId") Integer missionId, @RequestBody MissionRequestDTO request) {
        System.out.println(">>>> mission ctrl update check");

        MissionResponseDTO response = missionService.update(missionId, request);

        if(response != null)    return ResponseEntity.status(HttpStatus.OK).body(response);             // 200
        else                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);    // 400
    }


    // 미션 삭제
    @PostMapping("/delete/{missionId}") 
    public ResponseEntity<Boolean> delete(@PathVariable("missionId") Integer missionId) {
        System.out.println(">>>> mission ctrl delete check");

        Boolean flag = missionService.delete(missionId);
        if(flag)   return new ResponseEntity<>(HttpStatus.NO_CONTENT);     // 204
        else       return new ResponseEntity<>(HttpStatus.NOT_FOUND);      // 404
    }
    
}
