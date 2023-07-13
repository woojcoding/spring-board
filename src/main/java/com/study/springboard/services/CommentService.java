package com.study.springboard.services;

import com.study.springboard.dtos.CommentRequestDto;
import com.study.springboard.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * The type Comment service.
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

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
