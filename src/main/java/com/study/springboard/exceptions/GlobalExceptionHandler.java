package com.study.springboard.exceptions;

import com.study.springboard.dtos.BoardDetailResponseDto;
import com.study.springboard.dtos.BoardPostRequestDto;
import com.study.springboard.dtos.BoardUpdateRequestDto;
import com.study.springboard.dtos.FileDto;
import com.study.springboard.models.Category;
import com.study.springboard.repositories.BoardSearchCondition;
import com.study.springboard.services.BoardService;
import com.study.springboard.services.CategoryService;
import com.study.springboard.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

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

    private final FileService fileService;

    /**
     * BoardCanNotPost 예외가 발생하였을 떄 예외처리하는 메서드
     *
     * 검색 조건을 포함하여 작성중이던 폼을 다시 랜더링해줌
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

        BoardSearchCondition boardSearchCondition = ex.getBoardSearchCondition();

        modelAndView.addObject("categoryList", categoryList);
        modelAndView.addObject("errorMessage", message);
        modelAndView.addObject("boardPostRequestDto", boardPostRequestDto);
        modelAndView.addObject("boardSearch", boardSearchCondition);

        return modelAndView;
    }

    /**
     * BoardCanNotUpdate 예외가 발생하였을 떄 예외처리하는 메서드
     *
     * 검색 조건을 포함하여 작성중이던 폼을 다시 랜더링해줌
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

        List<FileDto> fileDtoList = fileService.getFiles(boardId);

        boardDetailResponseDto.setFileDtoList(fileDtoList);

        BoardUpdateRequestDto boardUpdateRequestDto =
                ex.getBoardUpdateRequestDto();

        String message = ex.getMessage();

        BoardSearchCondition boardSearchCondition = ex.getBoardSearchCondition();

        boardDetailResponseDto.setWriter(boardUpdateRequestDto.getWriter());
        boardDetailResponseDto.setContent(boardUpdateRequestDto.getContent());
        boardDetailResponseDto.setTitle(boardUpdateRequestDto.getTitle());

        modelAndView.addObject("boardDetailResponseDto", boardDetailResponseDto);
        modelAndView.addObject("errorMessage", message);
        modelAndView.addObject("boardUpdateRequestDto", boardUpdateRequestDto);
        modelAndView.addObject("boardSearch", boardSearchCondition);

        return modelAndView;
    }

    /**
     * BoardCanNotDelete 에러가 발생했을 떄 실행하는 메서드
     *
     * 검색 조건과 에러메세지를 쿼리파라미터로 추가하여 리다이렉트 시켜줌.
     *
     * @param ex 예외
     * @return the model and view
     */
    @ExceptionHandler(BoardCanNotDelete.class)
    public ModelAndView handleBoardCanNotDelete(BoardCanNotDelete ex) {
        int boardId = ex.getBoardId();

        String message = ex.getMessage();

        BoardSearchCondition boardSearchCondition = ex.getBoardSearchCondition();

        // 리디렉션 URL 생성
        String encodedErrorMessage = UriComponentsBuilder
                .fromPath(message)
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        String redirectUrl = UriComponentsBuilder
                .fromPath("/boards/free/view/{boardId}")
                .queryParam("pageNum", boardSearchCondition.getPageNum())
                .queryParam("startDate", boardSearchCondition.getStartDate())
                .queryParam("endDate", boardSearchCondition.getEndDate())
                .queryParam("category", boardSearchCondition.getCategory())
                .queryParam("keyword", boardSearchCondition.getKeyword())
                .queryParam("errorMessage", encodedErrorMessage)
                .buildAndExpand(boardId)
                .toUriString();

        // 필요한 경우 슬래시 인코딩을 원래대로
        redirectUrl.replace("%2F", "/");

        ModelAndView modelAndView = new ModelAndView("redirect:" + redirectUrl);

        return modelAndView;
    }
}
