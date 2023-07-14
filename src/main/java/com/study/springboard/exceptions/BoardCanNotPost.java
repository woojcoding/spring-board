package com.study.springboard.exceptions;

import com.study.springboard.dtos.BoardPostRequestDto;
import com.study.springboard.repositories.BoardSearchCondition;
import lombok.Getter;

/**
 * 게시물을 작성할 수 없는 예외 클래스입니다.
 */
@Getter
public class BoardCanNotPost extends RuntimeException {

    private BoardPostRequestDto boardPostRequestDto;

    private String message;

    private BoardSearchCondition boardSearchCondition;

    /**
     * 새로운 게시물을 작성할 수 없는 예외를 생성합니다
     *
     * @param boardPostRequestDto  게시물 작성 요청 DTO
     * @param boardSearchCondition 예외 처리 시 랜더링에 필요한 검색 조건
     * @param message              화면에 보여질 메세지
     */
    public BoardCanNotPost(BoardPostRequestDto boardPostRequestDto,
                           BoardSearchCondition boardSearchCondition,
                           String message
    ) {
        super();

        this.boardPostRequestDto = boardPostRequestDto;
        this.message = message;
        this.boardSearchCondition = boardSearchCondition;
    }
}
