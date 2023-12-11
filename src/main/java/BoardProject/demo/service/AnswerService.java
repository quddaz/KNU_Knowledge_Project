package BoardProject.demo.service;

import BoardProject.demo.domain.Answer;
import BoardProject.demo.domain.Board;
import BoardProject.demo.dto.AnswerDTO;
import BoardProject.demo.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import static BoardProject.demo.domain.enumSet.isCheckedSolution.ON;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    @Autowired
    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    /**
    *Board 객체를 받아 Answer들을 반환하는 메소드
    *
    *@Param Board 게시글 객체
    *@return 해당 게시글에 대한 답변들을 반환합니다.
     */
    public List<Answer> findByBoard(Board board) {
        return answerRepository.findByBoard(board);
    }

    /**
     *AnswerDTO를 받아 Answer 정보를 수정하는 메소드
     *
     *@Param AnswerDTO 답글의 DTO
     *@return
     */
    public void write(AnswerDTO answerDTO) {
        Answer answer = Answer.toAnswer(answerDTO);
        answerRepository.save(answer);
    }

    /**
     * Board의 답변 글을 생성할 때 초기화할 답변글 번호를 반환하는 메소드
     *
     * @param board 게시글 객체
     * @return 답변의 번호를 반환합니다.
     * @throws IllegalArgumentException 게시글이 null인 경우 예외를 던집니다.
     */
    public Long getNextSequenceForBoard(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("게시글이 null입니다.");
        }
        Long boardId = board.getId();
        Long currentMaxSequence = answerRepository.findMaxSequenceByBoardId(boardId);
        if (currentMaxSequence == null) {
            return 1L;
        }

        return currentMaxSequence + 1;
    }


    /**
     * 특정 게시글에 대한 답변 목록을 반환하는 메소드
     *
     * @param answerDTO 답변 조회에 필요한 정보를 담은 DTO (Data Transfer Object)
     * @return 해당 게시글의 답변 목록을 반환합니다.
     * @throws IllegalArgumentException 만약 게시글이 null이면 예외를 던집니다.
     */
    public List<Answer> viewAnswerList(AnswerDTO answerDTO) {
        Answer answer = Answer.toAnswer(answerDTO);

        if (answer.getBoard() == null) {
            throw new IllegalArgumentException("게시글이 null입니다.");
        }

        try {
            List<Answer> answerList = answerRepository.findByBoard(answer.getBoard());
            return answerList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("답변 조회 중 오류가 발생했습니다.");
        }
    }

    /**
     * 특정 문답글을 삭제하는 메소드
     *
     * @param answerDTO 답변 조회에 필요한 정보를 담은 DTO (Data Transfer Object)
     * @return
     */
    public void deleteAnswer(AnswerDTO answerDTO) {
        answerRepository.deleteById(answerDTO.getId());
    }

    /**
     * 특정 답변 ID에 해당하는 답변을 반환하는 메소드
     *
     * @param id 조회하고자 하는 답변의 ID
     * @return 조회된 답변 객체를 반환합니다.
     * @throws IllegalArgumentException 만약 ID가 null이면 예외를 던집니다.
     * @throws NoSuchElementException 만약 ID에 해당하는 답변이 없으면 예외를 던집니다.
     */
    public Answer findSpecificAnswer(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID가 null입니다.");
        }

        try {
            Answer answer = answerRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("ID에 해당하는 답변이 없습니다.")
            );

            return answer;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("답변 조회 중 오류가 발생했습니다.");
        }
    }

    /**
     * 채택한 답변을 업데이트하는 메소드
     *
     * @param answerDTO 답변 조회에 필요한 정보를 담은 DTO (Data Transfer Object)
     * @return
     * @throws IllegalArgumentException 만약 answerDTO가 null이면 예외를 던집니다.
     */
    public void adoptionWithUpdateAnswer(AnswerDTO answerDTO){
        if (answerDTO == null) {
            throw new IllegalArgumentException("채택 업데이트 메소드의 답글이 비어있습니다.");
        }
        answerDTO.setIsCheckedSolution(ON);
        write(answerDTO);
    }

    /**
     * 특정 게시글에 대해 특정 회원이 답변을 작성했는지 여부를 확인하는 메소드
     *
     * @param board 게시글 객체
     * @param memberId 회원 ID
     * @return 답변을 작성한 경우 true, 그렇지 않으면 false를 반환합니다.
     * @throws IllegalArgumentException 만약 board가 null이거나 memberId가 null이면 예외를 던집니다.
     */
    public boolean checkHasWrittenAnswer(Board board, String memberId) {
        if (board == null || memberId == null) {
            throw new IllegalArgumentException("게시글 또는 회원 ID가 null입니다.");
        }
        List<Answer> findAnswers = findByBoard(board);

        return findAnswers.stream().anyMatch(answer -> answer.getMember().getId().equals(memberId));
    }
}
