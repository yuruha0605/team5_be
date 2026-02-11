package com.example.team5_be.comment.ctrl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.team5_be.comment.domain.dto.CommentRequestDTO;
import com.example.team5_be.comment.domain.dto.CommentResponseDTO;
import com.example.team5_be.comment.service.CommentService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/comments")
@Tag(name = "Mission Comments API" ,  description = "Mission 댓글관련 API 명세서")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;


    @ApiResponses(
        {
            @ApiResponse(
                responseCode="200", 
                description="조회 성공",
                content = @Content(schema = @Schema(implementation = CommentResponseDTO.class))),
            @ApiResponse(responseCode="404" , description="존재하지 않는 글")
        }
    )
    @Operation(
        summary = "미션 댓글 조회",
        description = "미션 아이디를 이용한 조회"
    )
    @GetMapping("/mission/{missionId}/comment")
    public ResponseEntity<List<CommentResponseDTO>> read( 
        @Parameter(description = "mission ID" , example = "1") 
        @PathVariable("missionId") Integer missionId) {

        System.out.println(">>>> comment ctrl path : /read"); 
        System.out.println(">>>> params : "+ missionId); 

        List<CommentResponseDTO> response = commentService.read(missionId) ;
        if( response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK); // 200
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }
    

    @ApiResponses(
        {
            @ApiResponse(responseCode="201" , description="데이터 입력 성공"),
            @ApiResponse(responseCode="401" , description="인증 필요"),
            @ApiResponse(responseCode="400" , description="잘못된 요청")
        }
    )
    @Operation(
        summary = "특정 미션에 댓글 작성",
        description = "댓글을 신규로 작성한다(content, commentId)"
    )
    @PostMapping("/mission/{missionId}/comments/create")
    public ResponseEntity<List<CommentResponseDTO>> write(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "미션 댓글 작성 요청 DTO",
        required = true,
        content = @Content(
            schema = @Schema(implementation = CommentRequestDTO.class)
        ))
        Authentication authentication,
        @PathVariable Integer missionId,
        @RequestBody CommentRequestDTO request) {

        System.out.println(">>>> mission / comment  ctrl path : /write"); 
        System.out.println(">>>> params : "+ request); 
        System.out.println(">>>> title   : " + request.getTitle());
        System.out.println(">>>> content : " + request.getContent());

        if (authentication == null || authentication.getName() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.write(missionId, request)) ; 
        
    }

    @ApiResponses(
            {
                @ApiResponse(responseCode="204" , description="데이터 삭제 성공"),
                @ApiResponse(responseCode="401" , description="인증 필요"),
                @ApiResponse(responseCode="400" , description="잘못된 요청")
            }
        )
    @Operation(
        summary = "특정 미션에 댓글 삭제",
        description = "댓글을 삭제한다(content, commentId)"
    )
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Void> delete(
        Authentication authentication,
        @PathVariable("commentId") Integer commentId) {
        System.out.println(">>>> mission / comment  ctrl path : /delete"); 
        System.out.println(">>>> params : "+ commentId); 

        if (authentication == null || authentication.getName() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        commentService.delete(commentId); 
        return ResponseEntity.noContent().build() ; 

    }


    @ApiResponses(
        {
            @ApiResponse(responseCode="200" , description="데이터 수정 성공"),
            @ApiResponse(responseCode="401" , description="인증 필요"),
            @ApiResponse(responseCode="404" , description="댓글 찾지 못함")
        }
    )
    @Operation(
        summary = "특정 미션에 댓글 수정",
        description = "댓글을 수정한다"
    )
    @PutMapping("/update/{commentId}")
    public ResponseEntity<Void> update( 
        Authentication authentication,
        @Parameter(description = "댓글 ID" , example = "1") 
        @PathVariable("commentId") Integer commentId, 
        // @Schema(description = "블로그 DTO")
        @RequestBody CommentRequestDTO request) {
        System.out.println(">>>> comment ctrl path : /udpate"); 
        System.out.println(">>>> commentId : "+ commentId);
        System.out.println(">>>> params  : "+ request); 

        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CommentResponseDTO response = commentService.update(commentId, request);

        if(response != null ) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build() ;
        }
        
    }
}
