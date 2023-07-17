package com.study.springboard.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 게시글 수정에 사용되는 DTO 클래스입니다.
 *
 * 이 DTO 클래스는 게시글의 수정에 필요한 정보를 담고 있으며, 다음과 같은 필드들을 가지고 있습니다:
 * - Integer boardId: 게시글 ID
 * - String writer: 작성자
 * - String password: 비밀번호
 * - String title: 제목
 * - String content: 내용
 * - MultipartFile[] files: 파일
 * - List<Integer> deleteFileIdList: 삭제할 파일 ID 목록
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

    private MultipartFile[] files; // 파일

    /**
     * 삭제할 파일 Id들
     */
    List<Integer> deleteFileIdList = new ArrayList<>();
}
