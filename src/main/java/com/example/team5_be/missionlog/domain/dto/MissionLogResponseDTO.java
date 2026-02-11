package com.example.team5_be.missionlog.domain.dto;

import java.time.LocalDate;

import com.example.team5_be.missionlog.domain.entity.MissionLogEntity;

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
public class MissionLogResponseDTO {
    private Long missionlogId;
    private Integer missionId;
    private LocalDate checkDate;
    private Boolean isChecked;
    private String encouragementMessage;

    public static MissionLogResponseDTO fromEntity(MissionLogEntity entity) {
        return MissionLogResponseDTO.builder()
                .missionlogId(entity.getMissionlogId())
                .missionId(entity.getMission().getMissionId())
                .checkDate(entity.getCheckDate())
                .isChecked(entity.getIsChecked())
                // encouragementMessage는 Service에서 별도로 set
                .build();
    }
}
