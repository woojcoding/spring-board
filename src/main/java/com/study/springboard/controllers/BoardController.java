package com.study.springboard.controllers;

import com.study.springboard.dtos.BoardDetailResponseDto;
import com.study.springboard.dtos.BoardListDto;
import com.study.springboard.dtos.BoardPostRequestDto;
import com.study.springboard.dtos.BoardUpdateRequestDto;
import com.study.springboard.dtos.CommentRequestDto;
import com.study.springboard.dtos.CommentResponseDto;
import com.study.springboard.dtos.FileDto;
import com.study.springboard.models.Category;
import com.study.springboard.repositories.BoardSearchCondition;
import com.study.springboard.services.BoardService;
import com.study.springboard.services.CategoryService;
import com.study.springboard.services.CommentService;
import com.study.springboard.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * The type Board controller.
 */
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    private final CategoryService categoryService;

    private final CommentService commentService;

    private final FileService fileService;

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
        // 게시글 정보
        BoardDetailResponseDto boardDetailResponseDto =
                boardService.getBoard(boardId, true);

        // 댓글 정보
        List<CommentResponseDto> commentList =
                commentService.getComments(boardId);

        boardDetailResponseDto.setCommentList(commentList);

        // 파일 정보
        List<FileDto> fileDtoList = fileService.getFiles(boardId);

        boardDetailResponseDto.setFileDtoList(fileDtoList);

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
     * @param files                the files
     * @param boardPostRequestDto  게시글 등록에 필요한 Dto
     * @param boardSearchCondition 검색 조건
     * @param model                the model
     * @return the string
     * @throws IOException the io exception
     */
    @PostMapping("/board/free/write")
    public String postBoard(
            @RequestParam("file") MultipartFile[] files,
            @ModelAttribute("boardPostRequestDto")
            BoardPostRequestDto boardPostRequestDto,
            @ModelAttribute("boardSearch")
            BoardSearchCondition boardSearchCondition,
            Model model
    ) throws IOException {
        boardService.postBoard(boardPostRequestDto, boardSearchCondition);

        int boardId = boardPostRequestDto.getBoardId();

        fileService.uploadFiles(files, boardId);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromPath("/boards/free/view/{boardId}")
                .queryParam("pageNum", boardSearchCondition.getPageNum())
                .queryParam("startDate", boardSearchCondition.getStartDate())
                .queryParam("endDate", boardSearchCondition.getEndDate())
                .queryParam("category", boardSearchCondition.getCategory())
                .queryParam("keyword", boardSearchCondition.getKeyword());

        return "redirect:" + builder.buildAndExpand(boardId).toUriString();
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

        List<FileDto> fileDtoList = fileService.getFiles(boardId);

        boardDetailResponseDto.setFileDtoList(fileDtoList);

        model.addAttribute("boardSearch",boardSearchCondition);
        model.addAttribute("boardDetailResponseDto", boardDetailResponseDto);
        model.addAttribute("boardUpdateRequestDto",new BoardUpdateRequestDto());

        return "board/boardUpdateForm";
    }

    /**
     * 게시글을 수정하는 메서드
     *
     * @param files                 파일들
     * @param boardId               게시글 Id
     * @param boardSearchCondition  검색 조건
     * @param boardUpdateRequestDto 게시글 수정에 필요한 Dto
     * @param model                 the model
     * @return 수정 후 게시글 보기 페이지로 이동
     */
    @PostMapping("/board/free/modify/{boardId}")
    public String updateBoard(
            @RequestParam("file") MultipartFile[] files,
            @PathVariable("boardId") int boardId,
            @ModelAttribute("boardSearch")
            BoardSearchCondition boardSearchCondition,
            @ModelAttribute("boardUpdateRequestDto")
            BoardUpdateRequestDto boardUpdateRequestDto,
            Model model
    ) throws IOException {
        // 게시글 부분 업데이트 적용
        boardService.updateBoard(boardId, boardUpdateRequestDto,
                boardSearchCondition);

        // 파일 삭제 적용
        fileService.deleteFiles(boardUpdateRequestDto.getDeleteFileIdList());

        // 파일 업로드 적용
        fileService.uploadFiles(files, boardId);

        // 리다이렉트 주소 생성 후 리다이렉트
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromPath("/boards/free/view/{boardId}")
                .queryParam("pageNum", boardSearchCondition.getPageNum())
                .queryParam("startDate", boardSearchCondition.getStartDate())
                .queryParam("endDate", boardSearchCondition.getEndDate())
                .queryParam("category", boardSearchCondition.getCategory())
                .queryParam("keyword", boardSearchCondition.getKeyword());

        return "redirect:" + builder.buildAndExpand(boardId).toUriString();
    }

    /**
     * 게시글을 삭제하는 메서드
     *
     * @param boardId              게시글 Id
     * @param boardSearchCondition 검색 조건
     * @param password             비밀번호
     * @return 삭제 후 게시글 목록으로 이동
     */
    @PostMapping("/board/free/delete/{boardId}")
    public String deleteBoard(
            @PathVariable("boardId") int boardId,
            @ModelAttribute("boardSearch")
            BoardSearchCondition boardSearchCondition,
            @RequestParam("password") String password
    ) {
        boardService.deleteBoard(password, boardId, boardSearchCondition);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromPath("/boards/free/list")
                .queryParam("pageNum", boardSearchCondition.getPageNum())
                .queryParam("startDate", boardSearchCondition.getStartDate())
                .queryParam("endDate", boardSearchCondition.getEndDate())
                .queryParam("category", boardSearchCondition.getCategory())
                .queryParam("keyword", boardSearchCondition.getKeyword());

        return "redirect:" + builder.build().toUriString();
    }

    /**
     * 파일 다운로드
     *
     * @param fileId 파일 Id
     * @return 파일 file
     * @throws MalformedURLException the malformed url exception
     */
    @GetMapping("/boards/free/view/file/{fileId}")
    public ResponseEntity<Resource> getFile(@PathVariable("fileId") int fileId)
            throws MalformedURLException {
        FileDto fileDto = fileService.getFile(fileId);

        String originalName = fileDto.getOriginalName();

        String savedName = fileDto.getSavedName();

        UrlResource urlResource =
                new UrlResource("file:" + fileService.getFullPath(savedName));

        // 다운로드 파일명 인코딩
        String encodedOriginalName =
                UriUtils.encode(originalName, StandardCharsets.UTF_8);

        String contentDisposition = "attachment; filename=\""
                + encodedOriginalName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }
}
