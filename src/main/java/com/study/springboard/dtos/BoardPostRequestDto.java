package com.study.springboard.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * 게시글 등록에 사용되는 DTO 클래스입니다.
 *
 * 이 DTO 클래스는 게시글 등록에 필요한 정보를 담고 있으며, 다음과 같은 필드들을 가지고 있습니다:
 * - Integer boardId: 반환되는 게시글 ID
 * - String categoryId: 카테고리 ID
 * - String writer: 작성자
 * - String password: 비밀번호
 * - String title: 제목
 * - String content: 내용
 * - MultipartFile[] files: 파일들
 */
@Getter
@Setter
@NoArgsConstructor
public class BoardPostRequestDto {

    private Integer boardId; // 반환되는 게시글 Id

    private String categoryId; // 카테고리 ID

    private String writer; // 작성자

    private String password; // 비밀번호

    private String title; // 제목

    private String content; // 내용

    private  MultipartFile[] files; // 파일들
}
