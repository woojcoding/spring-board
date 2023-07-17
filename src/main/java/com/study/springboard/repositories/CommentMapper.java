package com.study.springboard.repositories;

import com.study.springboard.dtos.CommentRequestDto;
import com.study.springboard.dtos.CommentResponseDto;
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
     * @return List<CommentResponseDto>  댓글 List
     */
    List<CommentResponseDto> getCommentList(int boardId);

    /**
     * 댓글을 저장하는 메서드
     *
     * @param boardId           게시글 Id
     * @param commentRequestDto 게시글 본문을 담은 Dto
     */
    void postComment(int boardId, CommentRequestDto commentRequestDto);
}
