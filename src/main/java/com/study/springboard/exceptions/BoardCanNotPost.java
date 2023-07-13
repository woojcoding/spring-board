package com.study.springboard.exceptions;

/**
 * 게시물을 작성할 수 없는 예외 클래스입니다.
 */
public class BoardCanNotPost extends RuntimeException {

    /**
     * 새로운 "게시물을 작성할 수 없음" 예외를 생성합니다
     */
    public BoardCanNotPost() {
        super();
    }

    /**
     * 새로운 "게시물을 작성할 수 없음" 예외를 생성합니다.
     *
     * @param cause  원인이 되는 Throwable 객체
     */
    public BoardCanNotPost(Throwable cause) {
        super(cause);
    }
}
