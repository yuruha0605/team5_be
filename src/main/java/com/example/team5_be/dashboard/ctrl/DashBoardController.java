// package com.example.team5_be.dashboard.ctrl;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import com.example.team5_be.dashboard.domain.dto.DashBoardResponseDTO;
// import com.example.team5_be.dashboard.service.DashBoardService;

// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.responses.ApiResponse;
// import io.swagger.v3.oas.annotations.responses.ApiResponses;
// import lombok.RequiredArgsConstructor;

// @RestController
// @RequestMapping("/dashboard")
// @RequiredArgsConstructor
// public class DashBoardController {

//     private final DashBoardService dashboardService;

//     @ApiResponses(
//         {
//             @ApiResponse(responseCode="200" , description="데이터 불러오기 성공"),
//             @ApiResponse(responseCode="404" , description="찾지 못함")
//         }
//     )
//     @Operation(
//         summary = "특정 미션에 댓글 수정",
//         description = "댓글을 수정한다"
//     )
//     @GetMapping("/ranking")
//     public ResponseEntity<DashBoardResponseDTO> rankings(
//             @RequestParam(defaultValue = "TOTAL_SCORE") String sort,
//             @RequestParam(defaultValue = "20") Integer limit,
//             @RequestParam(defaultValue = "0") Integer offset
//     ) {
//         System.out.println(">>>> dashboard ctrl check");
//         System.out.println(">>>> 기준 : " + sort);
//         System.out.println(">>>> limit : " + limit);
//         System.out.println(">>>> 시작 위치 : " + offset);

//         return ResponseEntity.ok(dashboardService.getRankings(sort, limit, offset));
//     }
// }
