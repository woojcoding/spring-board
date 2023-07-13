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

@Controller
@RequestMapping("/boards/free/view/{boardId}")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public String postComment(@PathVariable("boardId") int boardId,
                              @ModelAttribute("boardSearch")
                              BoardSearchCondition boardSearchCondition,
                              @ModelAttribute("commentRequestDto")
                                  CommentRequestDto commentRequestDto,
                              Model model
    ) {
        commentService.postComment(boardId, commentRequestDto);

        return "redirect:/boards/free/view/{boardId}";
    }
}
