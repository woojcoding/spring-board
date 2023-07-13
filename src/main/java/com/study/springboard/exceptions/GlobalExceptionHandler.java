package com.study.springboard.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 전역 예외 처리를 담당하는 클래스입니다.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 게시물 작성 예외를 처리하고 작성 페이지로 리다이렉트합니다.
     *
     * @param exception the exception
     * @param model     the model
     * @return 작성 페이지로의 리다이렉트 경로
     */
    @ExceptionHandler(BoardCanNotPost.class)
    public String handleBoardCanNotPost(
            BoardCanNotPost exception,
            Model model
    ) {
        return "redirect:/board/free/write?errorMessage=Board Can Not Post";
    }
}
