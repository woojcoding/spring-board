package com.study.springboard.controllers;

import com.study.springboard.dtos.BoardDetailResponseDto;
import com.study.springboard.dtos.BoardListDto;
import com.study.springboard.dtos.BoardPostRequestDto;
import com.study.springboard.dtos.BoardUpdateRequestDto;
import com.study.springboard.dtos.CategoryDto;
import com.study.springboard.dtos.CommentRequestDto;
import com.study.springboard.dtos.CommentResponseDto;
import com.study.springboard.dtos.FileDto;
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
        // 게시글 정보 조회
        BoardListDto boardListDto = boardService.getBoardList(boardSearchCondition);

        // 검색 기능에 필요한 카테고리
        List<CategoryDto> categoryDtoList = categoryService.getCategoryList();

        // model에 값 지정
        model.addAttribute("boardListDto", boardListDto);
        model.addAttribute("categoryList", categoryDtoList);
        model.addAttribute("boardSearch", boardSearchCondition);

        // 게시글 리스트 페이지 리턴
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
        // 게시글 정보 조회
        boardService.updateViews(boardId);

        BoardDetailResponseDto boardDetailResponseDto =
                boardService.getBoard(boardId);

        // 댓글 정보 조회
        List<CommentResponseDto> commentList =
                commentService.getCommentList(boardId);

        boardDetailResponseDto.setCommentList(commentList);

        // 파일 정보 조회
        List<FileDto> fileDtoList = fileService.getFileList(boardId);

        boardDetailResponseDto.setFileDtoList(fileDtoList);

        // model에 값 지정
        model.addAttribute("boardDetailResponseDto", boardDetailResponseDto);
        model.addAttribute("commentRequestDto", new CommentRequestDto());
        model.addAttribute("boardSearch", boardSearchCondition);

        // 게시글 보기 페이지 리턴
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
        // 카테고리 지정을 위한 카테고리 조회
        List<CategoryDto> categoryDtoList = categoryService.getCategoryList();

        // model에 값 지정
        model.addAttribute("boardPostRequestDto", new BoardPostRequestDto());
        model.addAttribute("categoryList", categoryDtoList);
        model.addAttribute("boardSearch", boardSearchCondition);

        // 게시글 작성 페이지 리턴
        return "board/boardWriteForm";
    }

    /**
     * 게시글을 등록 요청하는 메서드
     *
     * @param boardPostRequestDto  게시글 등록에 필요한 Dto
     * @param boardSearchCondition 검색 조건
     * @param model                the model
     * @return the string
     * @throws IOException the io exception
     */
    @PostMapping("/board/free/write")
    public String postBoard(
            BoardPostRequestDto boardPostRequestDto,
            @ModelAttribute("boardSearch")
            BoardSearchCondition boardSearchCondition,
            Model model
    ) throws IOException {
        // 게시글을 post
        boardService.postBoard(boardPostRequestDto, boardSearchCondition);

        // 게시글에 첨부된 파일 업로드
        int boardId = boardPostRequestDto.getBoardId();

        MultipartFile[] files = boardPostRequestDto.getFiles();

        List<FileDto> fileDtoList = fileService.saveFiles(files, boardId);

        fileService.uploadFiles(fileDtoList);

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
        // 게시글 정보를 조회
        BoardDetailResponseDto boardDetailResponseDto =
                boardService.getBoard(boardId);

        // 게시글 Id로 파일 조회
        List<FileDto> fileDtoList = fileService.getFileList(boardId);

        boardDetailResponseDto.setFileDtoList(fileDtoList);

        // model에 값 지정
        model.addAttribute("boardSearch",boardSearchCondition);
        model.addAttribute("boardDetailResponseDto", boardDetailResponseDto);
        model.addAttribute("boardUpdateRequestDto",new BoardUpdateRequestDto());

        // 게시글 수정 페이지  리턴
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
    ) throws IOException {
        // 게시글 부분 업데이트 적용
        boardService.updateBoard(boardId, boardUpdateRequestDto,
                boardSearchCondition);

        // 파일 삭제 적용
        fileService.deleteFiles(boardUpdateRequestDto.getDeleteFileIdList());

        // 파일 업로드 적용
        MultipartFile[] files = boardUpdateRequestDto.getFiles();

        List<FileDto> fileDtoList = fileService.saveFiles(files, boardId);

        fileService.uploadFiles(fileDtoList);

        // 검색 조건을 유지시켜  수정된 게시글 상세보기 페이지로 리다이렉트
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
        // 게시글을 삭제
        boardService.deleteBoard(password, boardId, boardSearchCondition);

        // 파일 삭제
        fileService.deleteFilesByBoardId(boardId);

        // 검색 조건을 유지시켜 게시글 리스트 페이지로 리다이렉트
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
        // 파일 조회
        FileDto fileDto = fileService.getFile(fileId);

        // 원본 이름과 저장된 이름 가져옴
        String originalName = fileDto.getOriginalName();

        String savedName = fileDto.getSavedName();

        //파일의 경로를 생성하여 UrlResource 생성
        UrlResource urlResource =
                new UrlResource("file:" + fileService.getFullPath(savedName));

        // 다운로드 파일명 인코딩
        String encodedOriginalName =
                UriUtils.encode(originalName, StandardCharsets.UTF_8);

        // Content-Disposition 헤더를 설정하여 다운로드할 파일의 이름을 지정
        String contentDisposition = "attachment; filename=\""
                + encodedOriginalName + "\"";

        // ResponseEntity를 사용하여 파일을 응답으로 반환
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }
}
