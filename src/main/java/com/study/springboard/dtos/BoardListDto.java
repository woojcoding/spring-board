package com.study.springboard.dtos;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 게시글 목록 조회에서 사용되는 Dto
 */
@Getter
@Builder
public class BoardListDto {

    private List<BoardResponseDto> boardResponseDtoList; // 게시글의 정보를 담은 List

    private int totalBoardCount; // 게시물 총 수
}
