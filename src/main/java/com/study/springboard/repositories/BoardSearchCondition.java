package com.study.springboard.repositories;

import lombok.Getter;
import lombok.Setter;

/**
 * 게시글 검색 조건
 */
@Getter
@Setter
public class BoardSearchCondition {

    private int pageNum = 1; // 현재 페이지

    private String startDate; // 검색 시작일

    private String endDate; // 검색 종료일

    private String categoryId; // 카테고리 ID

    private String keyword; // 검색 키워드
}
