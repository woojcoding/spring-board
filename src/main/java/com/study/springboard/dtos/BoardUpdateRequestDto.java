package com.study.springboard.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class BoardUpdateRequestDto {

    private Integer boardId; // 게시글 Id

    private String writer; // 작성자

    private String password; // 비밀번호

    private String title; // 제목

    private String content; // 내용

    /**
     * 파일 List
     */
    List<MultipartFile> fileList;
}
