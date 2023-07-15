package com.study.springboard.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Board update request dto.
 */
@Getter
@Setter
@NoArgsConstructor
public class BoardUpdateRequestDto {

    private Integer boardId; // 게시글 Id

    private String writer; // 작성자

    private String password; // 비밀번호

    private String title; // 제목

    private String content; // 내용

    /**
     * 삭제할 파일 Id들
     */
    List<Integer> deleteFileIdList = new ArrayList<>();
}
