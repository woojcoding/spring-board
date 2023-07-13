package com.study.springboard.repositories;

import com.study.springboard.models.File;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * The interface File mapper.
 */
@Mapper
public interface FileMapper {

    /**
     * 파일 목록을 반환해주는 메서드
     *
     * @param boardId 게시글 Id
     * @return List<File> 파일 List
     */
    List<File> getFiles(int boardId);
}
