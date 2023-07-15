package com.study.springboard.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 게시글의 자세한 정보를 가져옴
 */
@Getter
@Setter
public class BoardDetailResponseDto {

    private Integer boardId; // 게시글 ID

    private String writer; // 작성자

    private String title; // 제목

    private String content; // 내용

    private String views; // 조회수

    private String createdAt; // 작성일

    private String modifiedAt; // 수정일

    private String categoryName; // 카테고리명

    /**
     * 댓글 List
     */
    List<CommentResponseDto> commentList;

    /**
     * 파일 List
     */
    List<FileDto> fileDtoList;
}
