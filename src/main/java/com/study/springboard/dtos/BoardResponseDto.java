package com.study.springboard.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * 게시글 목록 조회에서 사용되는 개별 게시글 정보를 나타내는 DTO 클래스입니다.
 *
 * 이 DTO 클래스는 개별 게시글의 정보를 담고 있으며, 다음과 같은 필드들을 가지고 있습니다:
 * - Integer boardId: 게시글 ID
 * - String categoryName: 카테고리명
 * - String writer: 작성자
 * - String title: 제목
 * - String views: 조회수
 * - String createdAt: 작성일
 * - String modifiedAt: 수정일
 * - boolean isAttached: 첨부 여부
 */
@Getter
@Setter
public class BoardResponseDto {

    private Integer boardId; // 게시글 ID

    private String categoryName; // 카테고리명

    private String writer; // 작성자

    private String title; // 제목

    private String views; // 조회수

    private String createdAt; // 작성일

    private String modifiedAt; // 수정일

    private boolean isAttached; // 첨부 여부
}
