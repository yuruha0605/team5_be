package com.example.team5_be.comment.service;

import java.util.List;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.team5_be.comment.dao.CommentRepository;
import com.example.team5_be.comment.domain.dto.CommentRequestDTO;
import com.example.team5_be.comment.domain.dto.CommentResponseDTO;
import com.example.team5_be.comment.domain.entity.CommentEntity;
import com.example.team5_be.mission.dao.MissionRepository;
import com.example.team5_be.mission.domain.entity.MissionEntity;
import com.example.team5_be.user.dao.UserRepository;
import com.example.team5_be.user.domain.entity.UserEntity;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    
    //의존성 주임
    private final MissionRepository missionRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    //작성
    @Transactional
    public List<CommentResponseDTO> write(Integer missionId, CommentRequestDTO request){
        System.out.println(">>>> mission/comment service write");        //디버그
        MissionEntity mission = missionRepository.findById(missionId)
            .orElseThrow(() -> new EntityNotFoundException("Mission not found"));

        String userId = getAuthUserId();
        UserEntity user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found : " + userId));
        commentRepository.save(request.toEntity(mission, user));

        CommentEntity saved = commentRepository.save(request.toEntity(mission, user));

        System.out.println(">>>> saved commentId   : " + saved.getCommentId());
        System.out.println(">>>> saved commentDate : " + saved.getCreatedAt());

        return commentRepository.findByMission_MissionId(missionId)
                .stream()
                .map(CommentResponseDTO::fromEntity)
                .toList();
    }

    //삭제
    @Transactional
    public void delete(Integer commentId) {
        System.out.println(">>>> mission/comment service delete");
        CommentEntity comment = commentRepository.findById(commentId)
                                .orElseThrow(() ->  
                                    new EntityNotFoundException("댓글 없음 : " + commentId));
        String userId = getAuthUserId();
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("본인 댓글만 수정 가능");
    }
        commentRepository.delete(comment);
    }


    //수정
    @Transactional
    public CommentResponseDTO update(Integer commentId, CommentRequestDTO request){
        System.out.println(">>>> comment service update");  
        CommentEntity comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new EntityNotFoundException("게시글 없음"));

        comment.update(request.getTitle(), request.getContent());

        String userId = getAuthUserId();
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("본인 댓글만 수정 가능");
        }
        
        return CommentResponseDTO.fromEntity(comment);
    }


    //조회
    @Transactional(readOnly = true)
    public List<CommentResponseDTO> read(Integer missionId) {
        System.out.println(">>>> comment service read"); 
        
        return commentRepository.findByMission_MissionId(missionId)
                                .stream()
                                .map(CommentResponseDTO::fromEntity)
                                .toList();

        }
                
    


    private String getAuthUserId() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName() ;
    }
}