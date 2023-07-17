package com.study.springboard.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 파일 업로드와 다운로드를 위해 사용되는 Dto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDto {

    private Integer fileId; // 파일 ID

    private String originalName; // 원본 파일명

    private String savedName; // 저장된 파일명

    private int boardId; // 게시글 Id
}
