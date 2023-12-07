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
import java.util.stream.Collectors;


@Service
public class BoardService {
    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    //글 작성
    public Board write(BoardDTO boardDTO) {
        Board board = Board.toBoard(boardDTO);
        boardRepository.save(board);
        return board;
    }
    //검색 기능 사용 중인지 체크
    public Page<Board> viewBoardList(String type, String keyword, Pageable pageable) {
        if (type != null && keyword != null && !type.isEmpty() && !keyword.isEmpty()) {
            // 검색이 있는 경우
            return searchBoardList(type, keyword, pageable);
        } else {
            // 일반적인 목록 조회
            return allBoardList(pageable);
        }
    }

    //전체  글리스트 가져오기
    public Page<Board> allBoardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    //검색 리스트 가져오기
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

    //특정 게시글 불러오기
    public BoardDTO boardView(Long id) {
        Board board = boardRepository.findById(id).get();
        System.out.println(board.getContent());
        BoardDTO boardDTO = BoardDTO.toBoardDTO(board);
        return boardDTO;
    }

    //게시글 지우기
    public void boardDelete(Long id) {
        boardRepository.deleteById(id);
    }

    //조회수 추가
    public void addView(Long id) {
        Board board = boardRepository.findById(id).get();
        Long view = board.getView();
        view+=1;
        board.setView(view);
        boardRepository.save(board);
    }

    public Board findById(Long id) {
        return boardRepository.findById(id).get();
    }

    public Page<Board> isMyBoard(Page<Board> list,String memberId) {
        // list의 getContent()로부터 가져온 List<Board>에서 조건에 맞는 항목을 필터링하여 새로운 리스트 생성
        List<Board> filteredList = list.getContent().stream()
                .filter(board -> board.getMember() == null || board.getMember().getId().equals(memberId))
                .collect(Collectors.toList());

        // 필터링된 리스트로 새로운 Page 객체 생성
        Page<Board> updatedPage = new PageImpl<>(filteredList, list.getPageable(), list.getTotalElements());

        return updatedPage;
    }



}
