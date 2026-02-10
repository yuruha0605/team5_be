package com.example.team5_be.missionlog.domain.dto;

import java.time.LocalDate;

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
public class MissionLogRequestDTO {
    private Integer missionId;
    private LocalDate checkDate;
    private Boolean isChecked;
}
