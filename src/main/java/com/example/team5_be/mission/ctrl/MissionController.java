package com.example.team5_be.mission.ctrl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.team5_be.comment.domain.dto.CommentResponseDTO;
import com.example.team5_be.mission.domain.dto.MissionRequestDTO;
import com.example.team5_be.mission.domain.dto.MissionResponseDTO;
import com.example.team5_be.mission.service.MissionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/missions")
@RequiredArgsConstructor
@Tag(name = "Mission", description = "미션 관련 명세서")
public class MissionController {

    private final MissionService missionService ;

    // 로그인 하지 않은 상태에서 미션 생성
    @ApiResponses(
        {
            @ApiResponse(
                responseCode="201", 
                description="미션 생성(등록) 성공",
                content = @Content(schema = @Schema(implementation = MissionResponseDTO.class))),
            @ApiResponse(responseCode="400" , description="미션 생성(등록) 실패")
        }
    )
    @Operation(
        summary = "미션 생성(등록)",
        description = "미션 생성(등록)을 수행합니다. 로그인 유저 기준으로 생성되며, 습관ID와 모드ID는 필수입니다. 상태는 기본값으로 '진행 중'으로 설정됩니다. 레벨은 모드에 따라, 레벨업의 경우 어떤 입력이 없어도 레벨1이 설정되며 자율선택 모드일 경우 사용자가 직접 입력해야 합니다."
    )
    @PostMapping("/register")
    public ResponseEntity<MissionResponseDTO> create(@RequestBody MissionRequestDTO request, String userId) {
        System.out.println(">>>> mission ctrl create check");

        MissionResponseDTO response = missionService.createForUser(request, userId);

        if (response != null)   return ResponseEntity.status(HttpStatus.CREATED).body(response);       // 201
        else                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);           // 400
    }




    // 로그인한 유저의 미션 생성
    @ApiResponses(
        {
            @ApiResponse(
                responseCode="201", 
                description="미션 생성(등록) 성공",
                content = @Content(schema = @Schema(implementation = MissionResponseDTO.class))),
            @ApiResponse(responseCode="400" , description="미션 생성(등록) 실패")
        }
    )
    @Operation(
        summary = "미션 생성(등록)",
        description = "미션 생성(등록)을 수행합니다. 로그인 유저 기준으로 생성되며, 습관ID와 모드ID는 필수입니다. 상태는 기본값으로 '진행 중'으로 설정됩니다. 레벨은 모드에 따라, 레벨업의 경우 어떤 입력이 없어도 레벨1이 설정되며 자율선택 모드일 경우 사용자가 직접 입력해야 합니다."
    )
    @PostMapping("/me/register")
    public ResponseEntity<MissionResponseDTO> createForUser(
            Authentication authentication,
            @RequestBody MissionRequestDTO request) {
        System.out.println(">>>> mission ctrl create check");
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        MissionResponseDTO response = missionService.createForUser(request, authentication.getName());

        if (response != null)   return ResponseEntity.status(HttpStatus.CREATED).body(response);       // 201
        else                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);           // 400
    }


    // mission ID로 미션 단건 조회
    @ApiResponses(
        {
            @ApiResponse(
                responseCode="200", 
                description="미션 조회 성공",
                content = @Content(schema = @Schema(implementation = MissionResponseDTO.class))),
            @ApiResponse(responseCode="404" , description="존재하지 않는 미션")
        }
    )
    @Operation(
        summary = "미션 단건 조회",
        description = "미션 아이디를 이용한 조회"
    )
    @GetMapping("/read/{missionId}") 
    public ResponseEntity<MissionResponseDTO> read(@PathVariable("missionId") Integer missionId) {
        System.out.println(">>>> mission ctrl read check");

        MissionResponseDTO response = missionService.read(missionId);


        if (response != null)   return ResponseEntity.status(HttpStatus.OK).body(response);            // 200
        else                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);   // 404
    }


    // 로그인한 유저의 미션 조회
    @ApiResponses(
        {
            @ApiResponse(
                responseCode="200",
                description="로그인 유저 미션 조회 성공",
                content = @Content(schema = @Schema(implementation = MissionResponseDTO.class))),
            @ApiResponse(responseCode="401" , description="인증 필요"),
            @ApiResponse(responseCode="404" , description="존재하지 않는 미션")
        }
    )
    @Operation(
        summary = "로그인 유저 미션 단건 조회",
        description = "로그인된 유저의 미션만 조회합니다."
    )
    @GetMapping("/me/read/{missionId}")
    public ResponseEntity<MissionResponseDTO> readMyMission(
            Authentication authentication,
            @PathVariable("missionId") Integer missionId) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        MissionResponseDTO response = missionService.readForUser(missionId, authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // 특정 유저의 모든 미션 조회
    @ApiResponses(
        {
            @ApiResponse(
                responseCode="200", 
                description="미션 목록 조회 성공",
                content = @Content(schema = @Schema(implementation = MissionResponseDTO.class))),
            @ApiResponse(responseCode="404" , description="존재하지 않는 유저")
        }
    )
    @Operation(
        summary = "유저의 개인 미션 목록 조회",
        description = "유저 아이디를 이용한 미션 목록 조회"
    )
    @GetMapping("/list/{userId}")
    public ResponseEntity<List<MissionResponseDTO>> list(@PathVariable("userId") String userId) {
        System.out.println(">>>> mission ctrl list check");

        return ResponseEntity.status(HttpStatus.OK).body(missionService.list(userId));
    }

    // 로그인한 유저의 미션 목록 조회
    @ApiResponses(
        {
            @ApiResponse(
                responseCode="200",
                description="로그인 유저 미션 목록 조회 성공",
                content = @Content(schema = @Schema(implementation = MissionResponseDTO.class))),
            @ApiResponse(responseCode="401" , description="인증 필요")
        }
    )
    @Operation(
        summary = "로그인 유저 미션 목록 조회",
        description = "로그인된 유저의 미션 목록을 조회합니다."
    )
    @GetMapping("/me/list")
    public ResponseEntity<List<MissionResponseDTO>> listMyMissions(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(missionService.listForUser(authentication.getName()));
    }

    // 미션 제목으로 검색
    @ApiResponses(
        {
            @ApiResponse(
                responseCode="200",
                description="미션 제목 검색 성공",
                content = @Content(schema = @Schema(implementation = MissionResponseDTO.class))),
            @ApiResponse(responseCode="400" , description="미션 제목 검색 실패")
        }
    )
    @Operation(
        summary = "미션 제목 검색",
        description = "유저 아이디와 키워드로 미션 제목을 부분 검색합니다."
    )
    @GetMapping("/search")
    public ResponseEntity<List<MissionResponseDTO>> searchByName(
            @RequestParam("userId") String userId,
            @RequestParam("keyword") String keyword) {
        System.out.println(">>>> mission ctrl search check");

        return ResponseEntity.status(HttpStatus.OK).body(missionService.searchByName(userId, keyword));
    }

    // 로그인한 유저의 미션 제목으로 검색
    @ApiResponses(
        {
            @ApiResponse(
                responseCode="200",
                description="로그인 유저 미션 제목 검색 성공",
                content = @Content(schema = @Schema(implementation = MissionResponseDTO.class))),
            @ApiResponse(responseCode="401" , description="인증 필요")
        }
    )
    @Operation(
        summary = "로그인 유저 미션 제목 검색",
        description = "로그인된 유저의 미션을 키워드로 부분 검색합니다."
    )
    @GetMapping("/me/search")
    public ResponseEntity<List<MissionResponseDTO>> searchMyMissionsByName(
            Authentication authentication,
            @RequestParam("keyword") String keyword) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String userId = authentication.getName();
        return ResponseEntity.status(HttpStatus.OK).body(missionService.searchByName(userId, keyword));
    }


    // 미션 수정
    @ApiResponses(
        {
            @ApiResponse(
                responseCode="200", 
                description="미션 수정 성공",
                content = @Content(schema = @Schema(implementation = MissionResponseDTO.class))),
            @ApiResponse(responseCode="400" , description="미션 수정 실패")
        }
    )
    @Operation(
        summary = "미션 수정",
        description = "MissionID를 가진 Mission의 Name, Definition을 수정한다."
    )
    @PutMapping("/update/{missionId}")
    public ResponseEntity<MissionResponseDTO> update(@PathVariable("missionId") Integer missionId, @RequestBody MissionRequestDTO request) {
        System.out.println(">>>> mission ctrl update check");

        MissionResponseDTO response = missionService.update(missionId, request);

        if(response != null)    return ResponseEntity.status(HttpStatus.OK).body(response);             // 200
        else                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);    // 400
    }

    // 로그인한 유저의 미션 수정
    @ApiResponses(
        {
            @ApiResponse(
                responseCode="200",
                description="로그인 유저 미션 수정 성공",
                content = @Content(schema = @Schema(implementation = MissionResponseDTO.class))),
            @ApiResponse(responseCode="401" , description="인증 필요")
        }
    )
    @Operation(
        summary = "로그인 유저 미션 수정",
        description = "로그인된 유저의 미션만 수정합니다."
    )
    @PutMapping("/me/update/{missionId}")
    public ResponseEntity<MissionResponseDTO> updateMyMission(
            Authentication authentication,
            @PathVariable("missionId") Integer missionId,
            @RequestBody MissionRequestDTO request) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        MissionResponseDTO response = missionService.updateForUser(missionId, request, authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 미션 레벨업 업데이트
    // @ApiResponses(
    //     {
    //         @ApiResponse(
    //             responseCode="200", 
    //             description="미션 레벨업 수정 성공",
    //             content = @Content(schema = @Schema(implementation = MissionResponseDTO.class))),
    //         @ApiResponse(responseCode="400" , description="미션 레벨업 수정 실패")
    //     }
    // )
    // @Operation(
    //     summary = "미션 레벨업 수정",
    //     description = "해당 MissionID를 가진 Mission이 레벨업을 위한 수정이다."
    // )
    // @PutMapping("/levelup/{missionId}")
    // public ResponseEntity<MissionResponseDTO> levelUpUpdate(@PathVariable("missionId") Integer missionId, @RequestBody MissionRequestDTO request) {
    //     System.out.println(">>>> mission ctrl levelUp check");

    //     MissionResponseDTO response = missionService.levelUpUpdate(missionId, request);

    //     if(response != null)    return ResponseEntity.status(HttpStatus.OK).body(response);             // 200
    //     else                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);    // 400
    // }


    // 미션 삭제
    @ApiResponses(
        {
            @ApiResponse(
                responseCode="204", 
                description="미션 삭제 성공"),
            @ApiResponse(responseCode="404" , description="미션 삭제 실패")
        }
    )
    @Operation(
        summary = "미션 삭제",
        description = "미션 아이디를 이용한 삭제"
    )
    @DeleteMapping("/delete/{missionId}") 
    public ResponseEntity<Boolean> delete(@PathVariable("missionId") Integer missionId) {
        System.out.println(">>>> mission ctrl delete check");

        Boolean flag = missionService.delete(missionId);
        if(flag)   return new ResponseEntity<>(HttpStatus.NO_CONTENT);     // 204
        else       return new ResponseEntity<>(HttpStatus.NOT_FOUND);      // 404
    }

    // 로그인한 유저의 미션 삭제
    @ApiResponses(
        {
            @ApiResponse(
                responseCode="204",
                description="로그인 유저 미션 삭제 성공"),
            @ApiResponse(responseCode="401" , description="인증 필요")
        }
    )
    @Operation(
        summary = "로그인 유저 미션 삭제",
        description = "로그인된 유저의 미션만 삭제합니다."
    )
    @DeleteMapping("/me/delete/{missionId}")
    public ResponseEntity<Boolean> deleteMyMission(
            Authentication authentication,
            @PathVariable("missionId") Integer missionId) {
        if (authentication == null || authentication.getName() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Boolean flag = missionService.deleteForUser(missionId, authentication.getName());
        if (flag) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
}
