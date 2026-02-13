package com.example.team5_be.missionlog.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.time.temporal.ChronoUnit;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.team5_be.mission.dao.MissionRepository;
import com.example.team5_be.mission.domain.entity.MissionEntity;
import com.example.team5_be.mission.service.MissionFailureScheduler;
import com.example.team5_be.missionlog.dao.MissionLogRepository;
import com.example.team5_be.missionlog.domain.dto.CalendarMonthResponseDTO;
import com.example.team5_be.missionlog.domain.dto.DailyMissionItemDTO;
import com.example.team5_be.missionlog.domain.dto.DailyMissionListResponseDTO;
import com.example.team5_be.missionlog.domain.dto.MissionLogRequestDTO;
import com.example.team5_be.missionlog.domain.dto.MissionLogResponseDTO;
import com.example.team5_be.missionlog.domain.entity.MissionLogEntity;
import com.example.team5_be.openai.service.OpenAIService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MissionLogService {
    private static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
                private static final int STATUS_FAILED_ID = 4;
                private static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");

    private final MissionLogRepository missionLogRepository;
    private final MissionRepository missionRepository;
        private final MissionFailureScheduler missionFailureScheduler;
    private final OpenAIService openAIService;



public MissionLogResponseDTO upsert(MissionLogRequestDTO request) {
    MissionEntity mission = missionRepository.findById(request.getMissionId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid missionId: " + request.getMissionId()));
    LocalDate checkDate = resolveCheckDate(request, mission);

    MissionLogEntity entity = missionLogRepository
            .findByMission_MissionIdAndCheckDate(request.getMissionId(), checkDate)
            .orElseGet(() -> MissionLogEntity.builder()
                    .mission(mission)
                    .checkDate(checkDate)
                    .build());

    entity.setIsChecked(request.getIsChecked());

    MissionLogEntity saved = missionLogRepository.save(entity);

    if (Boolean.TRUE.equals(saved.getIsChecked()) && !isFailedStatus(mission)) {
            // completion is handled by scheduler at midnight
    }

        if (Boolean.TRUE.equals(saved.getIsChecked())
                        && checkDate.isBefore(LocalDate.now(KOREA_ZONE))) {
                missionFailureScheduler.processMissionForDate(mission, checkDate);
        }

    // ========== AI 응원 메시지 생성 ==========
    String encouragement = openAIService.generateEncouragementMessage(
        mission.getMissionName(),
        request.getIsChecked()
    );
    

    return MissionLogResponseDTO.builder()
            .missionlogId(saved.getMissionlogId())
            .missionId(saved.getMission().getMissionId())
            .checkDate(saved.getCheckDate())
            .isChecked(saved.getIsChecked())
            .encouragementMessage(encouragement)  
            .build();
    
}


        private LocalDate resolveCheckDate(MissionLogRequestDTO request, MissionEntity mission) {
                LocalDate checkDate = request.getCheckDate();
                if (checkDate == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "checkDate is required");
                }

                LocalDate startDate = mission.getMissionStartDate();
                LocalDate endDate = mission.getMissionEndDate();

                if (checkDate.isBefore(startDate) || checkDate.isAfter(endDate)) {
                        LocalDate today = LocalDate.now();
                        long gapDays = Math.abs(ChronoUnit.DAYS.between(checkDate, today));

                        // If the client date is off by a day (timezone/ISO conversion), normalize to today
                        if (gapDays <= 1 && !today.isBefore(startDate) && !today.isAfter(endDate)) {
                                return today;
                        }

                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "checkDate is outside mission period");
                }

                return checkDate;
        }



        private boolean isFailedStatus(MissionEntity mission) {
                com.example.team5_be.status.domain.entity.StatusEntity status = mission.getStatus();
                return status != null && STATUS_FAILED_ID == status.getStatusId();
        }



    public CalendarMonthResponseDTO getMonthStamps(String userId, String month) {
        YearMonth yearMonth = YearMonth.parse(month, YEAR_MONTH_FORMATTER);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<MissionEntity> missions = missionRepository
                .findByUser_UserIdAndMissionStartDateLessThanEqualAndMissionEndDateGreaterThanEqual(userId, endDate, startDate);

        List<LocalDate> activeDates = new ArrayList<>();
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            LocalDate currentDate = current;
            List<MissionEntity> activeMissions = missions.stream()
                    .filter(mission -> !currentDate.isBefore(mission.getMissionStartDate())
                            && !currentDate.isAfter(mission.getMissionEndDate()))
                    .toList();

            if (!activeMissions.isEmpty()) {
                activeDates.add(currentDate);
            }

            current = current.plusDays(1);
        }

        return CalendarMonthResponseDTO.builder()
                .month(yearMonth.format(YEAR_MONTH_FORMATTER))
                .activeDates(activeDates)
                .build();
    }



    public DailyMissionListResponseDTO getDailyMissions(String userId, LocalDate date) {
        List<MissionEntity> missions = missionRepository
                .findByUser_UserIdAndMissionStartDateLessThanEqualAndMissionEndDateGreaterThanEqual(userId, date, date);

        List<Integer> missionIds = missions.stream()
                .map(MissionEntity::getMissionId)
                .toList();

        if (missionIds.isEmpty()) {
            return DailyMissionListResponseDTO.builder()
                    .date(date.toString())
                    .missions(List.of())
                    .build();
        }

        Map<Integer, MissionLogEntity> logsByMissionId = missionLogRepository
                .findByMission_MissionIdInAndCheckDate(missionIds, date).stream()
                .collect(Collectors.toMap(
                        log -> log.getMission().getMissionId(),
                        Function.identity(),
                        (left, right) -> left));

        List<DailyMissionItemDTO> items = missions.stream()
                .map(mission -> DailyMissionItemDTO.builder()
                        .missionId(mission.getMissionId())
                        .missionName(mission.getMissionName())
                        .success(resolveSuccess(logsByMissionId.get(mission.getMissionId())))
                        .build())
                .toList();

        return DailyMissionListResponseDTO.builder()
                .date(date.toString())
                .missions(items)
                .build();
    }


    
    private Boolean resolveSuccess(MissionLogEntity log) {
        return log == null ? null : log.getIsChecked();
    }
}