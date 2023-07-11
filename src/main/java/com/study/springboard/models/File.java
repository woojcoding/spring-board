package com.study.springboard.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class File {

    private Integer fileId; // 파일 ID

    private String originalName; // 원본 파일명

    private String savedName; // 저장된 파일명

    private boolean isDeleted; // 파일 삭제 여부

    private Integer boardId; // 연관된 게시물 ID
}
