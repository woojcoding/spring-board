package com.study.springboard.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 게시글 상세 정보를 나타내는 DTO 클래스입니다.
 *
 * 이 DTO 클래스는 게시글의 자세한 정보를 포함하고 있으며, 다음과 같은 필드들을 가지고 있습니다:
 * - Integer boardId: 게시글 ID
 * - String writer: 작성자
 * - String title: 제목
 * - String content: 내용
 * - String views: 조회수
 * - String createdAt: 작성일
 * - String modifiedAt: 수정일
 * - String categoryName: 카테고리명
 * - List<CommentResponseDto> commentList: 댓글 목록
 * - List<FileDto> fileDtoList: 파일 목록
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
