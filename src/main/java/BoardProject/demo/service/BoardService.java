package BoardProject.demo.service;

import BoardProject.demo.domain.Answer;
import BoardProject.demo.domain.Board;
import BoardProject.demo.domain.enumSet.isCheckedSolution;
import BoardProject.demo.dto.BoardDTO;
import BoardProject.demo.dto.MemberDTO;
import BoardProject.demo.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class BoardService {
    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    /**
     * DB에 게시글 객체를 저장하는 메소드
     *
     * @param boardDTO 게시글 DTO
     * @return board
     * @throws IllegalArgumentException 만약 게시글이 null이면 예외를 던집니다.
     */
    public Board write(BoardDTO boardDTO) {
        if (boardDTO == null) {
            throw new IllegalArgumentException("write : 게시글DTO가 null입니다.");
        }
        Board board = Board.toBoard(boardDTO);
        boardRepository.save(board);
        return board;
    }

    /**
     * 검색 기능을 사용 중인지 체크하는 메소드
     *
     * @param type : 검색 타입
     * @param keyword : 검색 키워드
     * @param pageable : 페이지 관련 파라미터
     * @return 필더링된 게시글
     */
    public Page<Board> viewBoardList(String type, String keyword, Pageable pageable) {
        if (type != null && keyword != null && !type.isEmpty() && !keyword.isEmpty()) {
            // 검색이 있는 경우
            return searchBoardList(type, keyword, pageable);
        } else {
            // 일반적인 목록 조회
            return allBoardList(pageable);
        }
    }

    /**
     * 전체 게시글을 가져오는 메소드
     *
     * @param pageable : 페이지 관련 파라미터
     * @return 모든 게시글
     * @throws
     */
    public Page<Board> allBoardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    /**
     * 게시글을 검색으로 필터링해서 가져오는 메소드
     *
     * @param type : 검색 타입
     * @param keyword : 검색 키워드
     * @param pageable : 페이지 관련 파라미터
     * @return 키워드에 따른 검색 글
     */
    public Page<Board> searchBoardList(String type, String keyword, Pageable pageable) {

        if ("title".equals(type)) {
            return boardRepository.findByTitleContaining(keyword, pageable);
        } else if ("writer".equals(type)) {
            return boardRepository.findByMember_Name(keyword, pageable);
        } else {
            // 다른 검색 유형에 대한 처리 추가
            return allBoardList(pageable);
        }
    }

    /**
     * 특정 게시글을 가져오는 메소드
     *
     * @param id 게시글의 기본키.
     * @return Board 객체의 DTO
     * @throws  IllegalArgumentException 만약 ID가 NULL일 경우 예외를 던집니다.
     */
    public BoardDTO boardView(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("boardView : id가 null입니다.");
        }
        Board board = boardRepository.findById(id).get();

        BoardDTO boardDTO = BoardDTO.toBoardDTO(board);
        return boardDTO;
    }

    /**
     * 게시글 삭제 메소드
     *
     * @param id 게시글의 기본키
     * @return
     * @throws IllegalArgumentException 만약 ID가 NULL일 경우 예외를 던집니다.
     */
    public void boardDelete(Long id) {
        boardRepository.deleteById(id);
    }

    /**
     * 게시글 조회수 초기화 메소드
     *
     * @param id 게시글의 기본키
     * @return
     * @throws IllegalArgumentException 만약 ID가 NULL일 경우 예외를 던집니다.
     */
    public void addView(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("addView : id가 null입니다.");
        }
        Board board = boardRepository.findById(id).get();
        Long view = board.getView();
        view+=1;
        board.setView(view);
        boardRepository.save(board);
    }

    /**
     * 특정 게시글을 기본키로 찾는 메소드
     *
     * @param id 게시글의 기본키
     * @return Board 객체
     * @throws
     */
    public Board findById(Long id) {
        return boardRepository.findById(id).get();
    }

    /**
     * 특정 회원이 작성한 게시글만 필터링하여 새로운 Page 객체를 생성하는 메소드입니다.
     *
     * @param page      원본 Page 객체
     * @param memberId  게시글 작성 회원의 ID
     * @return 특정 회원이 작성한 게시글만 필터링한 새로운 Page 객체
     */
    public Page<Board> isMyBoard(Page<Board> page, String memberId) {
        List<Board> filteredList = page.getContent().stream()
            .filter(board -> Objects.equals(board.getMember().getId(), memberId))
            .collect(Collectors.toList());

        return new PageImpl<>(filteredList, page.getPageable(), page.getTotalElements());
    }



}
