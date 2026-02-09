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

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    
    //의존성 주임
    private final MissonRepository missonRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public List<CommentResponseDTO> write(CommentRequestDTO request){
        System.out.println(">>>> misson/comment service write");        //디버그
        
        List<CommentResponseDTO> list = null ;
        
        MissonEntity misson = missonRepository.findById(request.getMissonId())
            .orElseThrow(() -> new EntityNotFoundException("Misson not found : " + request.getMissonId()));

        commentRepository.save(request.toEntity(misson));


        return commentRepository.findByMisson_MissonId(request.getMissonId())
                .stream()
                .map(CommentResponseDTO::fromEntity)
                .toList();

        
    }


    @Transactional
    public void delete(Integer commentId) {
        System.out.println(">>>> misson/comment service delete");
        CommentEntity comment = commentRepository.findById(commentId)
                                .orElseThrow(() ->  
                                    new EntityNotFoundException("댓글 없음 : " + commentId));
        commentRepository.delete(comment);
    }


    @Transactional
    public CommentResponseDTO update(Integer commentId, CommentRequestDTO request){
        System.out.println(">>>> blog service update");  
        CommentEntity comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new EntityNotFoundException("게시글 없음"));

        comment.update(request.getMissonName(), request.getContent());
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