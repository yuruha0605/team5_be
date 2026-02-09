package com.example.team5_be.comment.service;

import java.util.List;

import javax.xml.stream.events.Comment;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.team5_be.comment.dao.CommentRepository;
import com.example.team5_be.comment.domain.dto.CommentRequestDTO;
import com.example.team5_be.comment.domain.dto.CommentResponseDTO;
import com.example.team5_be.comment.domain.entity.CommentEntity;
import com.example.team5_be.mission.dao.MissionRepository;
import com.example.team5_be.mission.domain.entity.MissionEntity;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    
    //의존성 주임
    private final MissionRepository missionRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public List<CommentResponseDTO> write(CommentRequestDTO request){
        System.out.println(">>>> mission/comment service write");        //디버그
        
        List<CommentResponseDTO> list = null ;
        
        MissionEntity mission = missionRepository.findById(request.getMissionId())
            .orElseThrow(() -> new EntityNotFoundException("Mission not found : " + request.getMissionId()));

        commentRepository.save(request.toEntity(mission));


        return commentRepository.findByMission_MissionId(request.getMissionId())
                .stream()
                .map(CommentResponseDTO::fromEntity)
                .toList();

        
    }


    @Transactional
    public void delete(Integer commentId) {
        System.out.println(">>>> mission/comment service delete");
        CommentEntity comment = commentRepository.findById(commentId)
                                .orElseThrow(() ->  
                                    new EntityNotFoundException("댓글 없음 : " + commentId));
        commentRepository.delete(comment);
    }


    @Transactional
    public CommentResponseDTO update(Integer commentId, CommentRequestDTO request){
        System.out.println(">>>> comment service update");  
        CommentEntity comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new EntityNotFoundException("게시글 없음"));

        comment.update(request.getMissionName(), request.getContent());
        // save() 안 해도 됨 (Dirty Checking)
        // blogRepository.save(blog) ;
        
        return CommentResponseDTO.fromEntityWithoutComments(comment);
    }
    //고민
    private String getAuthEmail() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName() ;
    }
}