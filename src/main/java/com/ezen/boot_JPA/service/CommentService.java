package com.ezen.boot_JPA.service;

import com.ezen.boot_JPA.dto.CommentDTO;
import com.ezen.boot_JPA.entity.Comment;
import org.springframework.data.domain.Page;

public interface CommentService {
    long post(CommentDTO commentDTO);

    // convert 작업
    // DTO => Entity
    default Comment convertDtoToEntity(CommentDTO commentDTO) {
        return Comment.builder()
                .cno(commentDTO.getCno())
                .bno(commentDTO.getBno())
                .writer(commentDTO.getWriter())
                .content(commentDTO.getContent())
                .build();
    }

    // Entity => DTO
    default CommentDTO convertEntityToDto(Comment comment) {
        return CommentDTO.builder()
                .cno(comment.getCno())
                .bno(comment.getBno())
                .writer(comment.getWriter())
                .content(comment.getContent())
                .registerAt(comment.getRegisterAt())
                .modifyAt(comment.getModifyAt())
                .build();
    }

    Page<CommentDTO> getList(long bno, int page);

    long remove(long cno);

    long update(CommentDTO commentDTO);


    //List<CommentDTO> getList(long bno);
}
