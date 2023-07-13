package com.study.springboard.exceptions;

import com.study.springboard.dtos.BoardPostRequestDto;
import com.study.springboard.models.Category;
import com.study.springboard.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 전역 예외 처리를 담당하는 클래스입니다.
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final CategoryService categoryService;

    /**
     * BoardCanNotPost 예외가 발생하였을 떄 예외처리하는 메서드
     *
     * @param ex 예외
     * @return the model and view
     */
    @ExceptionHandler(BoardCanNotPost.class)
    public ModelAndView handleBoardCanNotPost(BoardCanNotPost ex) {
        ModelAndView modelAndView = new ModelAndView("board/boardWriteForm");

        List<Category> categoryList = categoryService.getCategories();

        BoardPostRequestDto boardPostRequestDto = ex.getBoardPostRequestDto();

        modelAndView.addObject("categoryList", categoryList);
        modelAndView.addObject("errorMessage", "게시물 작성에 실패했습니다.");
        modelAndView.addObject("boardPostRequestDto", boardPostRequestDto);

        return modelAndView;
    }
}
