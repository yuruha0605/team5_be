package com.example.team5_be.dashboard.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashBoardResponseDTO {
    private String sort;
    private Integer limit;
    private Integer offset;
    private List<DashBoardRowDTO> items;
}
