package com.example.team5_be.missionlog.ctrl;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.team5_be.mission.domain.dto.MissionResponseDTO;
import com.example.team5_be.missionlog.domain.dto.CalendarMonthResponseDTO;
import com.example.team5_be.missionlog.domain.dto.DailyMissionListResponseDTO;
import com.example.team5_be.missionlog.domain.dto.MissionLogRequestDTO;
import com.example.team5_be.missionlog.domain.dto.MissionLogResponseDTO;
import com.example.team5_be.missionlog.service.MissionLogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/mission-logs")
@RequiredArgsConstructor
@Tag(name = "Mission Log API" ,  description = "미션 로그 관련 API 명세서")
public class MissionLogController {
    private final MissionLogService missionLogService;



    @ApiResponses(
        {
            @ApiResponse(
                responseCode="200", 
                description="미션 로그 생성(업서트) 성공",
                content = @Content(schema = @Schema(implementation = MissionLogResponseDTO.class))),
            @ApiResponse(responseCode="400" , description="미션 로그 생성(업서트) 실패")
        }
    )
    @Operation(
        summary = "미션 로그 생성(업서트)",
        description = "새로운 미션 로그를 생성하거나 기존 미션 로그를 업데이트합니다."
    )
    @PostMapping("/check")
    public ResponseEntity<MissionLogResponseDTO> upsert(@RequestBody MissionLogRequestDTO request) {
        MissionLogResponseDTO response = missionLogService.upsert(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ApiResponses(
        {
            @ApiResponse(
                responseCode="200", 
                description="월간 스탬프 조회 성공",
                content = @Content(schema = @Schema(implementation = CalendarMonthResponseDTO.class))),
            @ApiResponse(responseCode="400" , description="월간 스탬프 조회 실패")
        }
    )
    @Operation(
        summary = "월간 스탬프 조회",
        description = "특정 사용자의 월간 미션 스탬프 데이터를 조회합니다."
    )
    @GetMapping("/calendar/{userId}")
    public ResponseEntity<CalendarMonthResponseDTO> getMonthStamps(
            @PathVariable("userId") String userId,
            @RequestParam("month") String month) {
        return ResponseEntity.status(HttpStatus.OK).body(missionLogService.getMonthStamps(userId, month));
    }



    @ApiResponses(
        {
            @ApiResponse(
                responseCode="200", 
                description="일간 미션 조회 성공",
                content = @Content(schema = @Schema(implementation = DailyMissionListResponseDTO.class))),
            @ApiResponse(responseCode="400" , description="일간 미션 조회 실패")
        }
    )
    @Operation(
        summary = "일간 미션 조회",
        description = "특정 사용자의 특정 일자의 미션 목록을 조회합니다."
    )
    @GetMapping("/daily/{userId}")
    public ResponseEntity<DailyMissionListResponseDTO> getDailyMissions(
            @PathVariable("userId") String userId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.status(HttpStatus.OK).body(missionLogService.getDailyMissions(userId, date));
    }
}
