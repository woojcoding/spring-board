package com.study.springboard.exceptions;

import lombok.Getter;

/**
 * 게시물을 삭제할 수 없는 예외 클래스입니다.
 */
@Getter
public class BoardCanNotDelete extends RuntimeException {

    private String message;

    private int boardId;

    /**
     * 새로운 게시물을 삭제할 수 없는 예외를 생성합니다
     *
     * @param message 화면에 보여질 메세지
     * @param boardId 게시글 Id
     */
    public BoardCanNotDelete(String message, int boardId) {
        super();

        this.message = message;
        this.boardId = boardId;
    }
}
