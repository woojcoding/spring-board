package com.study.springboard.services;

import com.study.springboard.dtos.BoardDetailResponseDto;
import com.study.springboard.dtos.BoardListDto;
import com.study.springboard.dtos.BoardPostRequestDto;
import com.study.springboard.dtos.BoardResponseDto;
import com.study.springboard.dtos.BoardUpdateRequestDto;
import com.study.springboard.dtos.CommentResponseDto;
import com.study.springboard.exceptions.BoardCanNotDelete;
import com.study.springboard.exceptions.BoardCanNotPost;
import com.study.springboard.exceptions.BoardCanNotUpdate;
import com.study.springboard.models.File;
import com.study.springboard.repositories.BoardRepository;
import com.study.springboard.repositories.BoardSearchCondition;
import com.study.springboard.repositories.CommentRepository;
import com.study.springboard.repositories.FileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Board service.
 */
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    private final CommentRepository commentRepository;

    private final FileRepository fileRepository;

    /**
     * 게시글 목록 조회에서 검색 조건에 따라 게시글 정보들을 List로 받도록
     * Repository에 요청하기 위해 사용하는 메서드
     *
     * @param boardSearchCondition 검색 조건
     * @return List<BoardResponseDto>             게시글 정보 List
     */
    public BoardListDto getBoards(BoardSearchCondition boardSearchCondition) {

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
                boardRepository.getBoards(boardSearchCondition, rowBounds);

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
     * @param boardId     조회할 게시글의 Id
     * @param updateViews 조회수를 증가시킬지의 여부
     * @return BoardDetailResponseDto 게시글 정보
     */
    public BoardDetailResponseDto getBoard(int boardId, boolean updateViews) {

        /*
        *  조회수 증가가 true일 경우에만 조회수를 증가
        * */
        if (updateViews) {
            boardRepository.updateViews(boardId);
        }

        /**
         * db에서 게시글, 댓글, 파일들을 조회
         */
        BoardDetailResponseDto boardDetailResponseDto =
                boardRepository.getBoard(boardId);

        List<CommentResponseDto> commentList = commentRepository.getComments(boardId);

        List<File> fileList = fileRepository.getFiles(boardId);

        boardDetailResponseDto.setCommentList(commentList);
        boardDetailResponseDto.setFileList(fileList);

        return boardDetailResponseDto;
    }

    /**
     * 게시물을 작성하는 메서드
     *
     * @param boardPostRequestDto  게시물 작성 요청 DTO
     * @param boardSearchCondition 예외 처리시 사용하기 위한 검색 조건
     */
    public void postBoard(BoardPostRequestDto boardPostRequestDto,
                          BoardSearchCondition boardSearchCondition
    ) {
        validateRequestDto(boardPostRequestDto, 0, boardSearchCondition);

        boardRepository.postBoard(boardPostRequestDto);
    }

    /**
     * 게시글을 수정하는 메서드
     *
     * @param boardId               게시글 Id
     * @param boardUpdateRequestDto 게시글을 수정하는데 필요한 Dto
     * @param boardSearchCondition  예외 처리시 사용하기 위한 검색 조건
     */
    public void updateBoard(int boardId,
                            BoardUpdateRequestDto boardUpdateRequestDto,
                            BoardSearchCondition boardSearchCondition
    ) {
        validateRequestDto(boardUpdateRequestDto, boardId, boardSearchCondition);

        boardRepository.updateBoard(boardId, boardUpdateRequestDto);
    }

    /**
     * 게시물 작성 요청 DTO의 유효성을 검사하는 메서드
     *
     * @param dto     게시물 작성 요청 DTO
     * @param boardId
     * @throws BoardCanNotPost 게시물 작성 불가능 예외
     */
    private void validateRequestDto(Object dto, int boardId,
                                    BoardSearchCondition boardSearchCondition
    ) throws BoardCanNotPost {
        StringBuilder message = new StringBuilder();

        String categoryId = null;

        String writer = null;

        String password = null;

        String password2 = null;

        String title = null;

        String content = null;

        if (dto instanceof BoardPostRequestDto) {
            BoardPostRequestDto boardPostRequestDto = (BoardPostRequestDto) dto;

            categoryId = boardPostRequestDto.getCategoryId();

            writer = boardPostRequestDto.getWriter();

            password = boardPostRequestDto.getPassword();

            password2 = boardPostRequestDto.getPassword2();

            title = boardPostRequestDto.getTitle();

            content = boardPostRequestDto.getContent();
        } else if (dto instanceof BoardUpdateRequestDto) {
            BoardUpdateRequestDto boardUpdateRequestDto =
                    (BoardUpdateRequestDto) dto;

            writer = boardUpdateRequestDto.getWriter();

            password = boardUpdateRequestDto.getPassword();

            title = boardUpdateRequestDto.getTitle();

            content = boardUpdateRequestDto.getContent();
        } else {
            throw new IllegalArgumentException("Invalid DTO type.");
        }

        // 카테고리 필수 선택 검증
        if (dto instanceof BoardPostRequestDto
                && ( categoryId == null || categoryId.equals("all"))) {
            message.append("카테고리를 선택해주세요.\n");
        }

        // 작성자 필수, 글자 수 검증
        if (writer == null || writer.length() < 3 || writer.length() >= 5) {
            message.append("작성자는 3글자 이상, 5글자 미만으로 입력해주세요.\n");
        }

        // 비밀번호 필수, 글자 수, 패턴 검증
        if (dto instanceof BoardPostRequestDto
                && (password == null
                || password.length() < 4
                || password.length() >= 16
                || !password.matches("^(?=.*[a-zA-Z])(?=.*\\d)"
                + "(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]+$"))) {
            message.append("비밀번호는 4글자 이상, 16글자 미만의 영문,"
                    + " 숫자, 특수문자 조합으로 입력해주세요.\n");
        } else if (dto instanceof BoardPostRequestDto
                && !password.equals(password2)) {
            message.append("비밀번호와 비밀번호 확인이 일치하지 않습니다.\n");
        } else if (dto instanceof  BoardUpdateRequestDto
                && !validatePassword(password, boardId)) {
            message.append("비밀번호가 일치하지 않습니다.\n");
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
            if (dto instanceof BoardPostRequestDto) {
                throw new BoardCanNotPost((BoardPostRequestDto) dto,
                        boardSearchCondition, message.toString()
                );
            } else if (dto instanceof BoardUpdateRequestDto) {
                throw new BoardCanNotUpdate((BoardUpdateRequestDto) dto,
                        message.toString(), boardId, boardSearchCondition
                );
            }
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
    public void deleteBoard(String password, int boardId,
                            BoardSearchCondition boardSearchCondition
    ) {
        if (validatePassword(password, boardId)) {
            boardRepository.deleteBoard(boardId);
        } else {
            throw new BoardCanNotDelete("비밀번호가 일치하지 않습니다.", boardId,
                    boardSearchCondition);
        }
    }
}
