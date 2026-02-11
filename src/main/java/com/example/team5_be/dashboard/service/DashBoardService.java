package com.example.team5_be.dashboard.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;


import com.example.team5_be.dashboard.dao.DashBoardRepository;
import com.example.team5_be.dashboard.domain.dto.DashBoardResponseDTO;
import com.example.team5_be.dashboard.domain.dto.DashBoardRowDTO;
import com.example.team5_be.trophy.dao.TrophyRelationshipRepository;
import com.example.team5_be.trophy.dao.TrophyRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DashBoardService {


    private final DashBoardRepository dashboardRepository;
    private final TrophyRelationshipRepository trophyRelationshipRepository;


    public DashBoardResponseDTO getRankings(String sort, Integer limit, Integer offset) {

        String safeSort = (sort == null || sort.isBlank())
                ? "TOTAL_SCORE"
                : sort.toUpperCase();

        int safeLimit = (limit == null || limit <= 0) ? 20 : limit;
        int safeOffset = (offset == null || offset < 0) ? 0 : offset;

        List<DashBoardRowDTO> allRows;

        if ("TROPHY_COUNT".equals(safeSort)) {

            var views = trophyRelationshipRepository.findRankingByTrophyCount();

            allRows = views.stream()
                    .map(v -> DashBoardRowDTO.builder()
                            .userId(v.getUserId())
                            .userName(v.getUserName())
                            .totalScore(0) // 총합 정렬이 아니니 0으로
                            .trophyCount(v.getTrophyCount())
                            .build())
                    .collect(Collectors.toList());

        } else { // TOTAL_SCORE

            var views = dashboardRepository.findRankingByTotalScore();

            allRows = views.stream()
                    .map(v -> DashBoardRowDTO.builder()
                            .userId(v.getUserId())
                            .userName(v.getUserName())
                            .totalScore(v.getTotalScore())
                            .trophyCount(0) // 총합 정렬이니 0으로
                            .build())
                    .collect(Collectors.toList());

            safeSort = "TOTAL_SCORE";
        }

        // offset / limit 자르기
        int fromIndex = Math.min(safeOffset, allRows.size());
        int toIndex = Math.min(safeOffset + safeLimit, allRows.size());
        List<DashBoardRowDTO> slicedRows = allRows.subList(fromIndex, toIndex);

        List<DashBoardRowDTO> rankedRows = applyRank(safeOffset, slicedRows);

        return DashBoardResponseDTO.builder()
                .sort(safeSort)
                .limit(safeLimit)
                .offset(safeOffset)
                .items(rankedRows)
                .build();
    }

    private List<DashBoardRowDTO> applyRank(int offset, List<DashBoardRowDTO> rows) {
        List<DashBoardRowDTO> result = new ArrayList<>(rows.size());
        for (int i = 0; i < rows.size(); i++) {
            DashBoardRowDTO r = rows.get(i);
            result.add(DashBoardRowDTO.builder()
                    .rank(offset + i + 1)
                    .userId(r.getUserId())
                    .userName(r.getUserName())
                    .totalScore(r.getTotalScore())
                    .trophyCount(r.getTrophyCount())
                    .build());
        }
        return result;
    }
}
