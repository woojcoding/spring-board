package com.study.springboard.exceptions;

import com.study.springboard.dtos.BoardDetailResponseDto;
import com.study.springboard.dtos.BoardPostRequestDto;
import com.study.springboard.dtos.BoardUpdateRequestDto;
import com.study.springboard.models.Category;
import com.study.springboard.services.BoardService;
import com.study.springboard.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 전역 예외 처리를 담당하는 클래스입니다.
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final CategoryService categoryService;

    private final BoardService boardService;

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

        String message = ex.getMessage();

        modelAndView.addObject("categoryList", categoryList);
        modelAndView.addObject("errorMessage", message);
        modelAndView.addObject("boardPostRequestDto", boardPostRequestDto);

        return modelAndView;
    }

    /**
     * BoardCanNotUpdate 예외가 발생하였을 떄 예외처리하는 메서드
     *
     * @param ex 예외
     * @return the model and view
     */
    @ExceptionHandler(BoardCanNotUpdate.class)
    public ModelAndView handleBoardCanNotUpdate(BoardCanNotUpdate ex) {
        ModelAndView modelAndView = new ModelAndView("board/boardUpdateForm");

        int boardId = ex.getBoardId();

        BoardDetailResponseDto boardDetailResponseDto =
                boardService.getBoard(boardId, false);

        BoardUpdateRequestDto boardUpdateRequestDto =
                ex.getBoardUpdateRequestDto();

        String message = ex.getMessage();

        boardDetailResponseDto.setWriter(boardUpdateRequestDto.getWriter());
        boardDetailResponseDto.setContent(boardUpdateRequestDto.getContent());
        boardDetailResponseDto.setTitle(boardUpdateRequestDto.getTitle());

        modelAndView.addObject("boardDetailResponseDto", boardDetailResponseDto);
        modelAndView.addObject("errorMessage", message);
        modelAndView.addObject("boardUpdateRequestDto", boardUpdateRequestDto);

        return modelAndView;
    }

    @ExceptionHandler(BoardCanNotDelete.class)
    public ModelAndView handleBoardCanNotDelete(BoardCanNotDelete ex) {
        int boardId = ex.getBoardId();
        String message = ex.getMessage();

        // 리디렉션 URL 생성
        String encodedErrorMessage =
                URLEncoder.encode(message, StandardCharsets.UTF_8);

        String redirectUrl =
                String.format("redirect:/boards/free/view/%d?errorMessage=%s",
                        boardId, encodedErrorMessage);

        // 리디렉션 URL을 사용하여 새로운 ModelAndView 생성
        ModelAndView modelAndView = new ModelAndView(redirectUrl);

        return modelAndView;
    }
}
