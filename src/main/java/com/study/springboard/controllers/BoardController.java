package com.study.springboard.controllers;

import com.study.springboard.dtos.BoardDetailResponseDto;
import com.study.springboard.dtos.BoardListDto;
import com.study.springboard.dtos.BoardPostRequestDto;
import com.study.springboard.dtos.BoardUpdateRequestDto;
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
import org.springframework.web.bind.annotation.PostMapping;

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
        model.addAttribute("boardSearch", boardSearchCondition);

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
    public String getBoard(
            @PathVariable("boardId") int boardId,
            @ModelAttribute("boardSearch")
            BoardSearchCondition boardSearchCondition,
            Model model
    ) {
        BoardDetailResponseDto boardDetailResponseDto =
                boardService.getBoard(boardId, true);

        model.addAttribute("boardDetailResponseDto", boardDetailResponseDto);
        model.addAttribute("commentRequestDto", new CommentRequestDto());
        model.addAttribute("boardSearch", boardSearchCondition);

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
    public String getWriteForm(
            @ModelAttribute("boardSearch")
            BoardSearchCondition boardSearchCondition,
            Model model
    ) {
        List<Category> categoryList = categoryService.getCategories();

        model.addAttribute("boardPostRequestDto", new BoardPostRequestDto());
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("boardSearch", boardSearchCondition);

        return "board/boardWriteForm";
    }

    /**
     * 게시글을 등록 요청하는 메서드
     *
     * @param boardSearchCondition 검색 조건
     * @param boardPostRequestDto  게시글 등록에 필요한 Dto
     * @param model                the model
     * @return the string
     */
    @PostMapping("/board/free/write")
    public String postBoard(
            @ModelAttribute("boardSearch")
            BoardSearchCondition boardSearchCondition,
            @ModelAttribute("boardPostRequestDto")
            BoardPostRequestDto boardPostRequestDto,
            Model model
    ) {
        boardService.postBoard(boardPostRequestDto);

        int boardId = boardPostRequestDto.getBoardId();

        model.addAttribute("boardSearch", boardSearchCondition);

        return "redirect:/boards/free/view/" + boardId;
    }

    /**
     * 게시글 수정 폼을 가져오는 메서드
     *
     * @param boardId              게시글 Id
     * @param boardSearchCondition 검색 조건
     * @param model                the model
     * @return 업데이트 폼 반환
     */
    @GetMapping("/board/free/modify/{boardId}")
    public String getUpdateForm(
            @PathVariable("boardId") int boardId,
            @ModelAttribute("boardSearch")
            BoardSearchCondition boardSearchCondition,
            Model model
    ) {
        BoardDetailResponseDto boardDetailResponseDto =
                boardService.getBoard(boardId, false);

        model.addAttribute("boardSearch",boardSearchCondition);
        model.addAttribute("boardDetailResponseDto", boardDetailResponseDto);
        model.addAttribute("boardUpdateRequestDto",new BoardUpdateRequestDto());

        return "board/boardUpdateForm";
    }

    /**
     * 게시글을 수정하는 메서드
     *
     * @param boardId               게시글 Id
     * @param boardSearchCondition  검색 조건
     * @param boardUpdateRequestDto 게시글 수정에 필요한 Dto
     * @param model                 the model
     * @return 수정 후 게시글 보기 페이지로 이동
     */
    @PostMapping("/board/free/modify/{boardId}")
    public String updateBoard(
            @PathVariable("boardId") int boardId,
            @ModelAttribute("boardSearch")
            BoardSearchCondition boardSearchCondition,
            @ModelAttribute("boardUpdateRequestDto")
            BoardUpdateRequestDto boardUpdateRequestDto,
            Model model
    ) {
        boardService.updateBoard(boardId, boardUpdateRequestDto);

        return "redirect:/boards/free/view/" + boardId;
    }
}
