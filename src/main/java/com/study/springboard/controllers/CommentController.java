package com.study.springboard.controllers;

import com.study.springboard.dtos.CommentRequestDto;
import com.study.springboard.repositories.BoardSearchCondition;
import com.study.springboard.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("/boards/free/view/{boardId}")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public String postComment(
            @PathVariable("boardId") int boardId,
            @ModelAttribute("boardSearch")
            BoardSearchCondition boardSearchCondition,
            @ModelAttribute("commentRequestDto")
            CommentRequestDto commentRequestDto,
            Model model
    ) {
        // 댓글을 post
        commentService.postComment(boardId, commentRequestDto);

        // model에 값 지정
        model.addAttribute("boardSearch", boardSearchCondition);

        // 검색 조건을 유지시켜 작성된 글 상세보기 페이지로 리다이렉트
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromPath("/boards/free/view/{boardId}")
                .queryParam("pageNum", boardSearchCondition.getPageNum())
                .queryParam("startDate", boardSearchCondition.getStartDate())
                .queryParam("endDate", boardSearchCondition.getEndDate())
                .queryParam("category", boardSearchCondition.getCategory())
                .queryParam("keyword", boardSearchCondition.getKeyword());

        return "redirect:" + builder.buildAndExpand(boardId).toUriString();
    }
}
