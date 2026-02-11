package com.example.team5_be.mission.ctrl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.team5_be.comment.domain.dto.CommentResponseDTO;
import com.example.team5_be.mission.domain.dto.MissionRequestDTO;
import com.example.team5_be.mission.domain.dto.MissionResponseDTO;
import com.example.team5_be.mission.service.MissionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/missions")
@RequiredArgsConstructor
@Tag(name = "Mission", description = "미션 관련 명세서")
public class MissionController {

    private final MissionService missionService ;

    // 미션 생성
    @ApiResponses(
        {
            @ApiResponse(
                responseCode="201", 
                description="미션 생성(등록) 성공",
                content = @Content(schema = @Schema(implementation = MissionResponseDTO.class))),
            @ApiResponse(responseCode="400" , description="미션 생성(등록) 실패")
        }
    )
    @Operation(
        summary = "미션 생성(등록)",
        description = "미션 생성(등록)을 수행합니다."
    )
    @PostMapping("/register")
    public ResponseEntity<MissionResponseDTO> create(@RequestBody MissionRequestDTO request) {
        System.out.println(">>>> mission ctrl create check");
        MissionResponseDTO response = missionService.create(request);

        if (response != null)   return ResponseEntity.status(HttpStatus.CREATED).body(response);       // 201
        else                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);           // 400
    }


    // mission ID로 미션 단건 조회
    @ApiResponses(
        {
            @ApiResponse(
                responseCode="200", 
                description="미션 조회 성공",
                content = @Content(schema = @Schema(implementation = MissionResponseDTO.class))),
            @ApiResponse(responseCode="404" , description="존재하지 않는 미션")
        }
    )
    @Operation(
        summary = "미션 단건 조회",
        description = "미션 아이디를 이용한 조회"
    )
    @GetMapping("/read/{missionId}") 
    public ResponseEntity<MissionResponseDTO> read(@PathVariable("missionId") Integer missionId) {
        System.out.println(">>>> mission ctrl read check");

        MissionResponseDTO response = missionService.read(missionId);


        if (response != null)   return ResponseEntity.status(HttpStatus.OK).body(response);            // 200
        else                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);   // 404
    }


    // 모든 미션 조회

    @ApiResponses(
        {
            @ApiResponse(
                responseCode="200", 
                description="미션 목록 조회 성공",
                content = @Content(schema = @Schema(implementation = MissionResponseDTO.class))),
            @ApiResponse(responseCode="404" , description="존재하지 않는 유저")
        }
    )
    @Operation(
        summary = "유저의 개인 미션 목록 조회",
        description = "유저 아이디를 이용한 미션 목록 조회"
    )
    @GetMapping("/list/{userId}")
    public ResponseEntity<List<MissionResponseDTO>> list(@PathVariable("userId") String userId) {
        System.out.println(">>>> mission ctrl list check");

        return ResponseEntity.status(HttpStatus.OK).body(missionService.list(userId));
    }


    // 미션 수정
    @ApiResponses(
        {
            @ApiResponse(
                responseCode="200", 
                description="미션 수정 성공",
                content = @Content(schema = @Schema(implementation = MissionResponseDTO.class))),
            @ApiResponse(responseCode="400" , description="미션 수정 실패")
        }
    )
    @Operation(
        summary = "미션 수정",
        description = "미션 아이디를 이용한 수정"
    )
    @PutMapping("/update/{missionId}")
    public ResponseEntity<MissionResponseDTO> update(@PathVariable("missionId") Integer missionId, @RequestBody MissionRequestDTO request) {
        System.out.println(">>>> mission ctrl update check");

        MissionResponseDTO response = missionService.update(missionId, request);

        if(response != null)    return ResponseEntity.status(HttpStatus.OK).body(response);             // 200
        else                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);    // 400
    }


    // 미션 삭제
    @ApiResponses(
        {
            @ApiResponse(
                responseCode="204", 
                description="미션 삭제 성공"),
            @ApiResponse(responseCode="404" , description="미션 삭제 실패")
        }
    )
    @Operation(
        summary = "미션 삭제",
        description = "미션 아이디를 이용한 삭제"
    )
    @DeleteMapping("/delete/{missionId}") 
    public ResponseEntity<Boolean> delete(@PathVariable("missionId") Integer missionId) {
        System.out.println(">>>> mission ctrl delete check");

        Boolean flag = missionService.delete(missionId);
        if(flag)   return new ResponseEntity<>(HttpStatus.NO_CONTENT);     // 204
        else       return new ResponseEntity<>(HttpStatus.NOT_FOUND);      // 404
    }
    
}
