package com.study.springboard.controllers;

import com.study.springboard.dtos.BoardDetailResponseDto;
import com.study.springboard.dtos.BoardListDto;
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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * The type Board controller.
 */
@Controller
@RequestMapping("/boards/free")
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
    @GetMapping("/list")
    public String getBoards(
            @ModelAttribute("boardSearch")
            BoardSearchCondition boardSearchCondition,
            Model model
    ) {
        BoardListDto boardListDto = boardService.getBoards(boardSearchCondition);

        model.addAttribute("boardListDto", boardListDto);

        List<Category> categoryList = categoryService.getCategories();

        model.addAttribute("categoryList", categoryList);

        return "board/boardList";
    }

    @GetMapping("/view/{boardId}")
    public String getBoard(@PathVariable("boardId") int boardId,
                           @ModelAttribute("boardSearch")
                           BoardSearchCondition boardSearchCondition,
                           Model model
    ) {
        BoardDetailResponseDto boardDetailResponseDto =
                boardService.getBoard(boardId, true);

        model.addAttribute("boardDetailResponseDto", boardDetailResponseDto);

        return "board/boardDetail";
    }
}
