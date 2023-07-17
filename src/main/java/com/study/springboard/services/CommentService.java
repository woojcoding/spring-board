package com.study.springboard.services;

import com.study.springboard.dtos.CommentRequestDto;
import com.study.springboard.dtos.CommentResponseDto;
import com.study.springboard.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Comment service.
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * 게시글Id로 댓글들을 가져온ㄴ 메서드
     *
     * @param boardId 게시글 Id
     * @return 댓글 List
     */
    public List<CommentResponseDto> getCommentList(int boardId) {
        return commentRepository.getCommentList(boardId);
    }

    /**
     * 댓글을 저장하는 메서드
     *
     * @param boardId           게시글 Id
     * @param commentRequestDto 게시글 본문을 담은 Dto
     */
    public void postComment(int boardId, CommentRequestDto commentRequestDto) {
        commentRepository.postComment(boardId, commentRequestDto);
    }
}
