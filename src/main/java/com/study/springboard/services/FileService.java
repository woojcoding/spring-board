package com.study.springboard.services;

import com.study.springboard.dtos.FileDto;
import com.study.springboard.repositories.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The type File service.
 */
@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${file.dir}")
    private String fileDir;

    private final FileRepository fileRepository;

    /**
     * 파일을 업로드 후 db에 정보를 저장하는 메서드
     *
     * @param files   파일들
     * @param boardId 게시글 Id
     * @throws IOException the io exception
     */
    public void uploadFiles(MultipartFile[] files, int boardId)
            throws IOException {
        List<FileDto> fileDtoList = saveFiles(files, boardId);

        for (FileDto fileDto : fileDtoList) {
            fileRepository.postFile(fileDto);
        }
    }

    /**
     * 파일을 지정된 폴더에 저장하고 정보를 List로 반환해주는 메서드
     *
     * @param files   파일들
     * @param boardId 게시글 Id
     * @return FileDto List
     * @throws IOException the io exception
     */
    public List<FileDto> saveFiles(MultipartFile[] files, int boardId) throws
            IOException {
        List<FileDto> savedFileList = new ArrayList<>();

        for (MultipartFile multipartFile : files) {
            if (multipartFile.getSize() != 0) {
                String originalName = multipartFile.getOriginalFilename();

                String savedName = getSavedName(originalName);

                multipartFile.transferTo(new File(getFullPath(savedName)));

                FileDto fileDto = new FileDto(originalName, savedName, boardId);

                savedFileList.add(fileDto);
            }
        }

        return savedFileList;
    }

    /**
     * 파일 이름을 포함한 저장 경로를 가져오는 메서드
     *
     * @param fileName 저장 파일명
     * @return 저장 경로
     */
    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    /**
     * 원본 파일명에서 확장자를 얻어 UUID형태의 저장 파일명을 지정하주는 메서드
     *
     * @param originalName  원본 파일명
     * @return 저장시킬 파일명
     */
    private String getSavedName(String originalName) {
        String extension = extractExtension(originalName);

        return UUID.randomUUID().toString() + "." + extension;
    }

    /**
     * 원본 파일명에서 확장자를 추출하는 메서드
     *
     * @param originalName  원본 파일명
     * @return 확장자명
     */
    private String extractExtension(String originalName) {
        int index = originalName.lastIndexOf(".");

        return originalName.substring(index + 1);
    }

    /**
     * 게시글Id로 파일들을 가져오는 메서드
     *
     * @param boardId 게시글Id
     * @return 파일 List
     */
    public List<FileDto> getFiles(int boardId) {
        return fileRepository.getFiles(boardId);
    }
}
