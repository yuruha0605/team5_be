// package com.example.team5_be.trophy.domain.dto;

// import com.example.team5_be.trophy.domain.entity.TrophyEntity;

// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Getter;
// import lombok.NoArgsConstructor;

// @Builder
// @Getter
// @NoArgsConstructor
// @AllArgsConstructor
// public class TrophyDTO {
    
<<<<<<< HEAD
//     private Long trophyId;
=======
    private Integer trophyId;
>>>>>>> d85bec330150517729fb1c69906e3fe1880b2ec0

//     private String trophyName;

<<<<<<< HEAD
//     // Habit 정보 중 필요한 것만 담기
//     private Long habitId;
//     private String habitName;
=======
    // Habit 정보 중 필요한 것만 담기
    private Integer habitId;
    private String habitName;
>>>>>>> d85bec330150517729fb1c69906e3fe1880b2ec0

//     public TrophyDTO toDTO(TrophyEntity trophy) {
//     return TrophyDTO.builder()
//             .trophyId(trophy.getTrophyId())
//             .trophyName(trophy.getTrophyName())
//             .habitId(trophy.getHabit().getHabitId())
//             .habitName(trophy.getHabit().getHabitName())
//             .build();
//     }
// }
