package com.study.springboard.repositories;

import com.study.springboard.models.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The type Comment repository.
 */
@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final CommentMapper commentMapper;

    /**
     * 댓글 목록을 반환해주는 메서드
     *
     * @param boardId 게시글 Id
     * @return List<Comment> 댓글 List
     */
    public List<Comment> findComments(int boardId) {
        return commentMapper.findComments(boardId);
    }
}
