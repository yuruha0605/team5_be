package com.example.team5_be.comment.ctrl;

import java.util.List;

import javax.xml.stream.events.Comment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("misson/{missonId}/comment")
@Tag(name = "Misson Comments API" ,  description = "Misson 댓글관련 API 명세서")
public class CommentController {
    private final CommentService commentService;

    @ApiResponses(
        {
            @ApiResponse(responseCode="404" , description="데이터 입력 성공"),
            @ApiResponse(responseCode="400" , description="잘못된 요청")
        }
    )
    @Operation(
        summary = "특정 미션에 댓글 작성",
        description = "댓글을 신규로 작성한다(content, commentId)"
    )
    @PostMapping("/write")
    public ResponseEntity<List<CommentResponseDTO>> write(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "미션 댓글 작성 요청 DTO",
        required = true,
        content = @Content(
            schema = @Schema(implementation = CommentRequestDTO.class)
        ))
        @RequestBody CommentRequestDTO request) {

        System.out.println(">>>> misson / comment  ctrl path : /write"); 
        System.out.println(">>>> params : "+ request); 

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.write(request)) ; 
        
    }


    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable("commentId") Integer commentId) {
        System.out.println(">>>> blog / comment  ctrl path : /delete"); 
        System.out.println(">>>> params : "+ commentId); 

        commentService.delete(commentId); 
        return ResponseEntity.noContent().build() ; 

    }
}
