package com.study.springboard.services;

import com.study.springboard.dtos.BoardDetailResponseDto;
import com.study.springboard.dtos.BoardListDto;
import com.study.springboard.dtos.BoardResponseDto;
import com.study.springboard.models.Comment;
import com.study.springboard.models.File;
import com.study.springboard.repositories.BoardRepository;
import com.study.springboard.repositories.BoardSearchCondition;
import com.study.springboard.repositories.CommentRepository;
import com.study.springboard.repositories.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Board service.
 */
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    private final CommentRepository commentRepository;

    private final FileRepository fileRepository;

    /**
     * 게시글 목록 조회에서 검색 조건에 따라 게시글 정보들을 List로 받도록
     * Repository에 요청하기 위해 사용하는 메서드
     *
     * @param boardSearchCondition 검색 조건
     * @return List<BoardResponseDto>  게시글 정보 List
     */
    public BoardListDto getBoards(BoardSearchCondition boardSearchCondition) {
        List<BoardResponseDto> boardResponseDtoList =
                boardRepository.findAll(boardSearchCondition);

        int totalBoardCount = boardRepository.findBoardCount(boardSearchCondition);

        return BoardListDto.builder()
                .boardResponseDtoList(boardResponseDtoList)
                .totalBoardCount(totalBoardCount)
                .build();
    }

    /**
     * 게시글의 정보를 가져오는 메서드
     *
     * @param boardId     조회할 게시글의 Id
     * @param updateViews 조회수를 증가시킬지의 여부
     * @return BoardDetailResponseDto 게시글 정보
     */
    public BoardDetailResponseDto getBoard(int boardId, boolean updateViews) {

        /*
        *  조회수 증가가 true일 경우에만 조회수를 증가시킴
        * */
        if (updateViews) {
            boardRepository.updateViews(boardId);
        }

        BoardDetailResponseDto boardDetailResponseDto =
                boardRepository.findOne(boardId);

        List<Comment> commentList = commentRepository.findComments(boardId);

        List<File> fileList = fileRepository.findFiles(boardId);

        boardDetailResponseDto.setCommentList(commentList);
        boardDetailResponseDto.setFileList(fileList);

        return boardDetailResponseDto;
    }
}
