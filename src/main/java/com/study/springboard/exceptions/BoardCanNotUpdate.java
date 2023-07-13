package com.study.springboard.exceptions;

import com.study.springboard.dtos.BoardUpdateRequestDto;
import lombok.Getter;

/**
 * 게시물을 수정할 수 없는 예외 클래스입니다.
 */
@Getter
public class BoardCanNotUpdate extends RuntimeException {

    private BoardUpdateRequestDto boardUpdateRequestDto;

    private String message;

    private int boardId;

    /**
     * 게시물을 수정할 수 없는 예외를 생성합니다
     *
     * @param boardUpdateRequestDto the board update request dto
     */
    public BoardCanNotUpdate(BoardUpdateRequestDto boardUpdateRequestDto,
                             String message, int boardId
    ) {
        super();

        this.boardUpdateRequestDto = boardUpdateRequestDto;
        this.message = message;
        this.boardId = boardId;
    }
}
