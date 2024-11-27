package com.ezen.boot_JPA.service;

import com.ezen.boot_JPA.dto.CommentDTO;
import com.ezen.boot_JPA.entity.Board;
import com.ezen.boot_JPA.entity.Comment;
import com.ezen.boot_JPA.repository.BoardRepository;
import com.ezen.boot_JPA.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Override
    public long post(CommentDTO commentDTO) {
        Comment savedComment = commentRepository.save(convertDtoToEntity(commentDTO));

        long bno = savedComment.getBno();
        Optional<Board> boardOptional = boardRepository.findById(bno);

        if (boardOptional.isPresent()) {
            Board board = boardOptional.get();
            int updatedCount = board.getCommentCount() + 1;

            if (updatedCount >= 0) {
                board.setCommentCount(updatedCount);
                boardRepository.save(board);
            }
        }
        return savedComment.getCno();
    }

    @Override
    public Page<CommentDTO> getList(long bno, int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("cno").descending());
        Page<Comment> list = commentRepository.findByBno(bno, pageable);
        Page<CommentDTO> dtoPageList = list.map(c -> convertEntityToDto(c));
        return dtoPageList;
    }

    /*@Override
    public List<CommentDTO> getList(long bno) {
        // select * from comment where bno = #{bno}
        List<Comment> list = commentRepository.findByBno(bno);
        List<CommentDTO> dtoList = list.stream().map(c -> convertEntityToDto(c)).toList();
        return dtoList;
    }*/

    @Override
    public long remove(long cno) {
        Optional<Comment> optionalComment = commentRepository.findById(cno);

        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            long bno = comment.getBno();
            commentRepository.deleteById(cno);

            Optional<Board> boardOptional = boardRepository.findById(bno);
            if (boardOptional.isPresent()) {
                Board board = boardOptional.get();
                int updatedCount = board.getCommentCount() - 1;

                if (updatedCount >= 0) {
                    board.setCommentCount(updatedCount);
                    boardRepository.save(board);
                }
            }
            System.out.println("comment.getCno() ===============================================================" + comment.getCno());
            return comment.getCno();
        }

        return 0L;
    }

    @Override
    public long update(CommentDTO commentDTO) {
        Optional<Comment> optionalComment = commentRepository.findById(commentDTO.getCno());
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setContent(commentDTO.getContent());
            return commentRepository.save(comment).getCno();
        }
        return 0;
    }
}
