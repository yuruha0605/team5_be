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

import com.example.team5_be.missionlog.domain.dto.CalendarMonthResponseDTO;
import com.example.team5_be.missionlog.domain.dto.DailyMissionListResponseDTO;
import com.example.team5_be.missionlog.domain.dto.MissionLogRequestDTO;
import com.example.team5_be.missionlog.domain.dto.MissionLogResponseDTO;
import com.example.team5_be.missionlog.service.MissionLogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/mission-logs")
@RequiredArgsConstructor
public class MissionLogController {
    private final MissionLogService missionLogService;

    @PostMapping
    public ResponseEntity<MissionLogResponseDTO> upsert(@RequestBody MissionLogRequestDTO request) {
        MissionLogResponseDTO response = missionLogService.upsert(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/calendar/{userId}")
    public ResponseEntity<CalendarMonthResponseDTO> getMonthStamps(
            @PathVariable("userId") String userId,
            @RequestParam("month") String month) {
        return ResponseEntity.status(HttpStatus.OK).body(missionLogService.getMonthStamps(userId, month));
    }

    @GetMapping("/daily/{userId}")
    public ResponseEntity<DailyMissionListResponseDTO> getDailyMissions(
            @PathVariable("userId") String userId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.status(HttpStatus.OK).body(missionLogService.getDailyMissions(userId, date));
    }
}
