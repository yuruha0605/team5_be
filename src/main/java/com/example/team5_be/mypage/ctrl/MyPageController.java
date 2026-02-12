package com.example.team5_be.mypage.ctrl;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.team5_be.mypage.domain.dto.MonthlyReportDTO;
import com.example.team5_be.mypage.domain.dto.ReportDTO;
import com.example.team5_be.mypage.service.MyPageService;

import java.time.YearMonth;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

     private final MyPageService myPageService;

    // 누적/총 통계 화면
    @GetMapping
    public ResponseEntity<ReportDTO> getOverallStats(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(myPageService.getOverallStats(userId));
    }

    // 월별 리포트 화면
    @GetMapping("/report")
    public ResponseEntity<MonthlyReportDTO> getMonthlyReport(
            @AuthenticationPrincipal String userId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        if (month == null) month = YearMonth.now();
        return ResponseEntity.ok(myPageService.getMonthlyReport(userId, month));
    }
}

