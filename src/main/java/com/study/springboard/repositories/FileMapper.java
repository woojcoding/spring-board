package com.study.springboard.repositories;

import com.study.springboard.dtos.FileDto;
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
     * @return List<File>  파일 List
     */
    List<FileDto> getFiles(int boardId);

    /**
     * 파일 정보를 db에 저장하는 메서드
     *
     * @param fileDto 파일 Dto
     */
    void postFile(FileDto fileDto);
}
