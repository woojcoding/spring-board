package com.study.springboard.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * 댓글 보기를 위해 사용되는 Dto
 */
@Getter
@Setter
public class CommentResponseDto {

    private String content; // 댓글 내용

    private String createdAt; // 작성 일시
}
