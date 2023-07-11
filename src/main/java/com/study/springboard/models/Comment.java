package com.study.springboard.models;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Comment {

    private Integer commentId; // 댓글 ID

    private String content; // 댓글 내용

    private String createdAt; // 작성 일시

    private Integer boardId; // 연관된 게시물 ID
}
