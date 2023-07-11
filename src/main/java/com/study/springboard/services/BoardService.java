package com.study.springboard.services;

import com.study.springboard.dtos.BoardListDto;
import com.study.springboard.dtos.BoardResponseDto;
import com.study.springboard.repositories.BoardRepository;
import com.study.springboard.repositories.BoardSearchCondition;
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

    /**
     * 게시글 목록 조회에서 검색 조건에 따라 게시글 정보들을 List로 받도록
     * Repository에 요청하기 위해 사용하는 메서드
     *
     * @param boardSearchCondition 검색 조건
     * @return List<BoardResponseDto> 게시글 정보 List
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
}
