package com.study.springboard.exceptions;

import com.study.springboard.dtos.BoardUpdateRequestDto;
import com.study.springboard.repositories.BoardSearchCondition;
import lombok.Getter;

/**
 * 게시물을 수정할 수 없는 예외 클래스입니다.
 */
@Getter
public class BoardCanNotUpdate extends RuntimeException {

    private BoardUpdateRequestDto boardUpdateRequestDto;

    private String message;

    private int boardId;

    private BoardSearchCondition boardSearchCondition;

    /**
     * 게시물을 수정할 수 없는 예외를 생성합니다
     *
     * @param boardUpdateRequestDto 게시물 수정 요청 DTO
     * @param message               화면에 보여질 Id
     * @param boardId               게시글 Id
     * @param boardSearchCondition  예외 처리 시 랜더링에 필요한 검색 조건
     */
    public BoardCanNotUpdate(BoardUpdateRequestDto boardUpdateRequestDto,
                             String message, int boardId,
                             BoardSearchCondition boardSearchCondition
    ) {
        super();

        this.boardUpdateRequestDto = boardUpdateRequestDto;
        this.message = message;
        this.boardId = boardId;
        this.boardSearchCondition = boardSearchCondition;
    }
}
