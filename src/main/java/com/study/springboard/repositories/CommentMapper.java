package com.study.springboard.repositories;

import com.study.springboard.models.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * The interface Comment mapper.
 */
@Mapper
public interface CommentMapper {

    /**
     * 댓글 목록을 반환해주는 메서드
     *
     * @param boardId 게시글 Id
     * @return List<Comment> 댓글 List
     */
    List<Comment> findComments(int boardId);
}
