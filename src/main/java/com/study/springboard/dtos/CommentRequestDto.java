package com.study.springboard.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * 댓글 작성에 사용되는 DTO 클래스입니다.
 *
 * 이 DTO 클래스는 댓글 작성에 필요한 정보를 담고 있으며, 다음과 같은 필드를 가지고 있습니다:
 * - String content: 댓글 본문 내용
 */
@Getter
@Setter
public class CommentRequestDto {

    private String content; // 본문 내용
}
