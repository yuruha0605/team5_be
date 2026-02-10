package com.example.team5_be.trophy.domain.dto;

import com.example.team5_be.trophy.domain.entity.TrophyEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TrophyDTO {
    
    private Long trophyId;

    private String trophyName;

    // Habit 정보 중 필요한 것만 담기
    private Long habitId;
    private String habitName;

    public TrophyDTO toDTO(TrophyEntity trophy) {
    return TrophyDTO.builder()
            .trophyId(trophy.getTrophyId())
            .trophyName(trophy.getTrophyName())
            .habitId(trophy.getHabit().getHabitId())
            .habitName(trophy.getHabit().getHabitName())
            .build();
}
}
