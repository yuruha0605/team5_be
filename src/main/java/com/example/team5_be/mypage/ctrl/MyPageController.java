package com.example.team5_be.mypage.ctrl;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.team5_be.mypage.domain.dto.ReportDTO;
import com.example.team5_be.mypage.service.MyPageService;

import java.time.YearMonth;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/report")
    public ResponseEntity<ReportDTO> getMonthlyReport(
            @AuthenticationPrincipal String userId,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        if (month == null) month = YearMonth.now();

        ReportDTO report = myPageService.getReport(userId, month);
        return ResponseEntity.ok(report);
    }
}
