package com.example.team5_be.missionlog.domain.dto;

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
public class DailyMissionItemDTO {
    private Integer missionId;
    private String missionName;
    private Boolean success;
}
