package com.study.springboard.exceptions;

import com.study.springboard.repositories.BoardSearchCondition;
import lombok.Getter;

/**
 * 게시물을 삭제할 수 없는 예외 클래스입니다.
 */
@Getter
public class BoardCanNotDelete extends RuntimeException {

    private String message;

    private int boardId;

    private BoardSearchCondition boardSearchCondition;

    /**
     * 새로운 게시물을 삭제할 수 없는 예외를 생성합니다
     *
     * @param message              화면에 보여질 메세지
     * @param boardId              게시글 Id
     * @param boardSearchCondition 예외 처리 시 리다이렉트에 필요한 검색 조건
     */
    public BoardCanNotDelete(String message, int boardId,
                             BoardSearchCondition boardSearchCondition
    ) {
        super();

        this.message = message;
        this.boardId = boardId;
        this.boardSearchCondition = boardSearchCondition;
    }
}
