package com.study.springboard.exceptions;

import com.study.springboard.dtos.BoardPostRequestDto;
import lombok.Getter;

/**
 * 게시물을 작성할 수 없는 예외 클래스입니다.
 */
@Getter
public class BoardCanNotPost extends RuntimeException {

    private BoardPostRequestDto boardPostRequestDto;

    /**
     * 새로운 "게시물을 작성할 수 없음" 예외를 생성합니다
     *
     * @param boardPostRequestDto 게시물 작성 요청 DTO
     */
    public BoardCanNotPost(BoardPostRequestDto boardPostRequestDto) {
        super();
        this.boardPostRequestDto = boardPostRequestDto;
    }

    /**
     * 새로운 "게시물을 작성할 수 없음" 예외를 생성합니다.
     *
     * @param boardPostRequestDto 게시물 작성 요청 DTO
     * @param cause               원인이 되는 Throwable 객체
     */
    public BoardCanNotPost(BoardPostRequestDto boardPostRequestDto, Throwable cause) {
        super(cause);
        this.boardPostRequestDto = boardPostRequestDto;
    }
}
