package com.example.team5_be.missionlog.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DailyMissionListResponseDTO {
    private String date;
    private List<DailyMissionItemDTO> missions;
}
