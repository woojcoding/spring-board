package com.study.springboard.controllers;

import com.study.springboard.dtos.BoardDetailResponseDto;
import com.study.springboard.dtos.BoardListDto;
import com.study.springboard.dtos.BoardPostRequestDto;
import com.study.springboard.dtos.CommentRequestDto;
import com.study.springboard.models.Category;
import com.study.springboard.repositories.BoardSearchCondition;
import com.study.springboard.services.BoardService;
import com.study.springboard.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * The type Board controller.
 */
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    private final CategoryService categoryService;

    /**
     * 게시글 목록을 조회하는데 사용되는 메서드.
     *
     * @param boardSearchCondition 검색 조건
     * @param model                the model
     * @return boardList 페이지 반환
     */
    @GetMapping("/boards/free/list")
    public String getBoards(
            @ModelAttribute("boardSearch")
            BoardSearchCondition boardSearchCondition,
            Model model
    ) {
        BoardListDto boardListDto = boardService.getBoards(boardSearchCondition);

        List<Category> categoryList = categoryService.getCategories();

        model.addAttribute("boardListDto", boardListDto);
        model.addAttribute("categoryList", categoryList);

        return "board/boardList";
    }

    /**
     * 게시글 보기 페이지로 이동
     *
     * @param boardId              조회할 게시글 Id
     * @param boardSearchCondition 검색 조건
     * @param model                the model
     * @return the board
     */
    @GetMapping("/boards/free/view/{boardId}")
    public String getBoard(@PathVariable("boardId") int boardId,
                           @ModelAttribute("boardSearch")
                           BoardSearchCondition boardSearchCondition,
                           Model model
    ) {
        BoardDetailResponseDto boardDetailResponseDto =
                boardService.getBoard(boardId, true);

        model.addAttribute("boardDetailResponseDto", boardDetailResponseDto);
        model.addAttribute("commentRequestDto", new CommentRequestDto());

        return "board/boardDetail";
    }

    /**
     * 게시글 작성 폼을 가져오는 메서드
     *
     * @param boardSearchCondition 검색조건
     * @param model                the model
     * @return 게시글 작성폼 반환
     */
    @GetMapping("/board/free/write")
    public String getWriteForm(@ModelAttribute("boardSearch")
                                  BoardSearchCondition boardSearchCondition,
                              Model model
    ) {
        List<Category> categoryList = categoryService.getCategories();

        model.addAttribute("boardPostRequestDto", new BoardPostRequestDto());
        model.addAttribute("categoryList",categoryList);

        return "board/boardWriteForm";
    }
}
