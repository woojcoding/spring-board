package com.study.springboard.exceptions;

import com.study.springboard.dtos.BoardDetailResponseDto;
import com.study.springboard.dtos.BoardPostRequestDto;
import com.study.springboard.dtos.BoardUpdateRequestDto;
import com.study.springboard.dtos.CategoryDto;
import com.study.springboard.dtos.FileDto;
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

        // 카테고리 선택을 위한 카테고리 List
        List<CategoryDto> categoryDtoList = categoryService.getCategoryList();

        // 작성 폼 유지를 위한 Dto
        BoardPostRequestDto boardPostRequestDto = ex.getBoardPostRequestDto();

        // alert 할 메세지
        String message = ex.getMessage();

        // 검색 조건 유지
        BoardSearchCondition boardSearchCondition = ex.getBoardSearchCondition();

        // 랜더링을 위한 ModelAndView 객체를 생성하여 반환
        modelAndView.addObject("categoryList", categoryDtoList);
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

        // 게시글 보기 페이지를 위한 게시글 정보
        BoardDetailResponseDto boardDetailResponseDto =
                boardService.getBoard(boardId);

        // 보여줄 파일 List
        List<FileDto> fileDtoList = fileService.getFileList(boardId);

        boardDetailResponseDto.setFileDtoList(fileDtoList);

        // 작성중이던 폼 유지를 위한 Dto
        BoardUpdateRequestDto boardUpdateRequestDto =
                ex.getBoardUpdateRequestDto();

        boardDetailResponseDto.setWriter(boardUpdateRequestDto.getWriter());
        boardDetailResponseDto.setContent(boardUpdateRequestDto.getContent());
        boardDetailResponseDto.setTitle(boardUpdateRequestDto.getTitle());

        // alert 해줄 메세지
        String message = ex.getMessage();

        // 검색 조건 유지
        BoardSearchCondition boardSearchCondition = ex.getBoardSearchCondition();

        // 랜더링을 위한 ModelAndView 객체를 생성하여 반환
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

        // alert 할 메세지
        String message = ex.getMessage();

        // 검색조건 유지
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

        // 필요한 경우 슬래시 인코딩을 원래대로 변경
        redirectUrl.replace("%2F", "/");

        // 리디렉션을 위한 ModelAndView 객체를 생성하여 반환
        ModelAndView modelAndView = new ModelAndView("redirect:" + redirectUrl);

        return modelAndView;
    }
}
