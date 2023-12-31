package com.study.springboard.services;

import com.study.springboard.dtos.BoardDetailResponseDto;
import com.study.springboard.dtos.BoardListDto;
import com.study.springboard.dtos.BoardPostRequestDto;
import com.study.springboard.dtos.BoardResponseDto;
import com.study.springboard.dtos.BoardUpdateRequestDto;
import com.study.springboard.exceptions.BoardCanNotDelete;
import com.study.springboard.exceptions.BoardCanNotPost;
import com.study.springboard.exceptions.BoardCanNotUpdate;
import com.study.springboard.repositories.BoardRepository;
import com.study.springboard.repositories.BoardSearchCondition;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * The type Board service.
 */
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * 게시글 목록 조회에서 검색 조건에 따라 게시글 정보들을 List로 받도록
     * Repository에 요청하기 위해 사용하는 메서드
     *
     * @param boardSearchCondition 검색 조건
     * @return List<BoardResponseDto>               게시글 정보 List
     */
    public BoardListDto getBoardList(BoardSearchCondition boardSearchCondition) {

        /**
         * 페이지 네이션을 위한 rowBounds 인스턴스 생성
         */
        int pageNum = boardSearchCondition.getPageNum();

        int pageSize = 10;

        int offset = (pageNum - 1) * pageSize;

        RowBounds rowBounds = new RowBounds(offset, pageSize);

        /**
         *  db에서 페이지네이션을 적용한 게시글 조회
         */
        List<BoardResponseDto> boardResponseDtoList =
                boardRepository.getBoardList(boardSearchCondition, rowBounds);

        int totalBoardCount =
                boardRepository.getBoardCount(boardSearchCondition);

        return BoardListDto.builder()
                .boardResponseDtoList(boardResponseDtoList)
                .totalBoardCount(totalBoardCount)
                .build();
    }

    /**
     * 게시글의 정보를 가져오는 메서드
     *
     * @param boardId 조회할 게시글의 Id
     * @return BoardDetailResponseDto 게시글 정보
     */
    public BoardDetailResponseDto getBoard(int boardId) {
        BoardDetailResponseDto boardDetailResponseDto =
                boardRepository.getBoard(boardId);

        return boardDetailResponseDto;
    }

    /**
     * 조회수 증가하는 메서드
     *
     * @param boardId 게시글 Id
     */
    public void updateViews(int boardId) {
        boardRepository.updateViews(boardId);
    }

    /**
     * 게시물을 작성하는 메서드
     *
     * @param boardPostRequestDto  게시물 작성 요청 DTO
     * @param boardSearchCondition 예외 처리시 사용하기 위한 검색 조건
     * @throws IOException the io exception
     */
    @Transactional
    public void postBoard(
            BoardPostRequestDto boardPostRequestDto,
            BoardSearchCondition boardSearchCondition
    ) throws IOException {
        validatePostRequestDto(boardPostRequestDto, boardSearchCondition);

        boardRepository.postBoard(boardPostRequestDto);
    }

    /**
     * 게시글을 수정하는 메서드
     *
     * @param boardId               게시글 Id
     * @param boardUpdateRequestDto 게시글을 수정하는데 필요한 Dto
     * @param boardSearchCondition  예외 처리시 사용하기 위한 검색 조건
     */
    @Transactional
    public void updateBoard(int boardId,
                            BoardUpdateRequestDto boardUpdateRequestDto,
                            BoardSearchCondition boardSearchCondition
    ) {
        // 게시글 요소들 유효성 검증 후 업데이트 적용
        validateUpdateRequestDto(boardUpdateRequestDto, boardId,
                boardSearchCondition);

        boardRepository.updateBoard(boardId, boardUpdateRequestDto);
    }

    /**
     * 게시물 수정 요청 DTO의 유효성을 검사하는 메서드
     *
     * @param boardUpdateRequestDto     게시물 수정 요청 DTO\
     * @param boardId                   게시글 Id
     * @throws BoardCanNotPost 게시물 작성 불가능 예외
     */
    private void validateUpdateRequestDto(
            BoardUpdateRequestDto boardUpdateRequestDto,
            int boardId,
            BoardSearchCondition boardSearchCondition) {
        StringBuilder message = new StringBuilder();

        String writer = boardUpdateRequestDto.getWriter();

        String password = boardUpdateRequestDto.getPassword();

        String title = boardUpdateRequestDto.getTitle();

        String content = boardUpdateRequestDto.getContent();

        // 작성자 필수, 글자 수 검증
        if (writer == null || writer.length() < 3 || writer.length() >= 5) {
            message.append("작성자는 3글자 이상, 5글자 미만으로 입력해주세요.\n");
        }

        // 비밀번호 필수, 글자 수, 패턴 검증
        if (password == null || password.length() < 4 || password.length() >= 16
                || !password.matches("^(?=.*[a-zA-Z])(?=.*\\d)"
                + "(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]+$")) {
            message.append("비밀번호는 4글자 이상, 16글자 미만의 영문,"
                    + " 숫자, 특수문자 조합으로 입력해주세요.\n");
        }

        // 제목 필수, 글자 수 검증
        if (title == null || title.length() < 4 || title.length() >= 100) {
            message.append("제목은 4글자 이상, 100글자 미만으로 입력해주세요.\n");
        }

        // 내용 필수, 글자 수 검증
        if (content == null || content.length() < 4
                || content.length() >= 2000) {
            message.append("내용은 4글자 이상, 2000글자 미만으로 입력해주세요.\n");
        }

        if (message.length() > 0) {
            throw new BoardCanNotUpdate(boardUpdateRequestDto,
                    message.toString(), boardId, boardSearchCondition);
        }

    }

    /**
     * 게시물 작성 요청 DTO의 유효성을 검사하는 메서드
     *
     * @param boardPostRequestDto     게시물 작성 요청 DTO
     * @throws BoardCanNotPost 게시물 작성 불가능 예외
     */
    private void validatePostRequestDto(
            BoardPostRequestDto boardPostRequestDto,
            BoardSearchCondition boardSearchCondition
    ) throws BoardCanNotPost {
        StringBuilder message = new StringBuilder();

        String categoryId = boardPostRequestDto.getCategoryId();

        String writer = boardPostRequestDto.getWriter();

        String password = boardPostRequestDto.getPassword();

        String title =  boardPostRequestDto.getTitle();

        String content = boardPostRequestDto.getContent();

        // 카테고리 필수 선택 검증
        if ( categoryId == null || categoryId.equals("all")) {
            message.append("카테고리를 선택해주세요.\n");
        }

        // 작성자 필수, 글자 수 검증
        if (writer == null || writer.length() < 3 || writer.length() >= 5) {
            message.append("작성자는 3글자 이상, 5글자 미만으로 입력해주세요.\n");
        }

        // 비밀번호 필수, 글자 수, 패턴 검증
        if (password == null || password.length() < 4 || password.length() >= 16
                || !password.matches("^(?=.*[a-zA-Z])(?=.*\\d)"
                + "(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]+$")) {
            message.append("비밀번호는 4글자 이상, 16글자 미만의 영문,"
                    + " 숫자, 특수문자 조합으로 입력해주세요.\n");
        }

        // 제목 필수, 글자 수 검증
        if (title == null || title.length() < 4 || title.length() >= 100) {
            message.append("제목은 4글자 이상, 100글자 미만으로 입력해주세요.\n");
        }

        // 내용 필수, 글자 수 검증
        if (content == null || content.length() < 4
                || content.length() >= 2000) {
            message.append("내용은 4글자 이상, 2000글자 미만으로 입력해주세요.\n");
        }

        if (message.length() > 0) {
            throw new BoardCanNotPost(boardPostRequestDto,
                    boardSearchCondition, message.toString());

        }
    }

    /**
     * 암호가 일치하는지 검사하는 메서드
     *
     * @param password     입력한 비밀번호
     * @param boardId       게시글 Id
     * @return isValidated  일치하지 않는다면 false를 리턴, 일치한다면 true 리턴
     */
    private boolean validatePassword(String password ,int boardId) {
        boolean isValidated = false;

        String dbPassword = boardRepository.getPassword(boardId);

        if (dbPassword.equals(password)) {
            isValidated = true;
        }

        return isValidated;
    }

    /**
     * 게시글을 삭제하는 메서드
     *
     * @param password             비밀번호
     * @param boardId              게시글 Id
     * @param boardSearchCondition the board search condition
     */
    @Transactional
    public void deleteBoard(String password, int boardId,
                            BoardSearchCondition boardSearchCondition
    ) {
        // 암호를 검증 후 실패 한다면 예외를 throw
        if (validatePassword(password, boardId)) {
            boardRepository.deleteBoard(boardId);
        } else {
            throw new BoardCanNotDelete("비밀번호가 일치하지 않습니다.", boardId,
                    boardSearchCondition);
        }
    }
}
