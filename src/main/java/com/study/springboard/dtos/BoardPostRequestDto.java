package com.study.springboard.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 게시글 등록에 사용되는 Dto
 */
@Getter
@Setter
public class BoardPostRequestDto {

    private Integer boardId; // 반환되는 게시글 Id

    private String categoryId; // 카테고리 ID

    private String writer; // 작성자

    private String password; // 비밀번호

    private String password2; // 재확인용 비밀번호

    private String title; // 제목

    private String content; // 내용

    /**
     * 파일 List
     */
    List<MultipartFile> fileList;
}
