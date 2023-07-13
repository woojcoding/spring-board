package com.study.springboard.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 *  댓글 작성할 때 사용되는 Dto
 */
@Getter
@Setter
public class CommentRequestDto {

    private String content; // 본문 내용
}
