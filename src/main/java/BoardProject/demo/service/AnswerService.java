package BoardProject.demo.service;

import BoardProject.demo.domain.Answer;
import BoardProject.demo.domain.Board;
import BoardProject.demo.dto.AnswerDTO;
import BoardProject.demo.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static BoardProject.demo.domain.enumSet.isCheckedSolution.ON;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    @Autowired
    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public List<Answer> findByBoard(Board board) {
        return answerRepository.findByBoard(board);
    }
    //문답글 작성 메소드
    public void write(AnswerDTO answerDTO) {
        Answer answer = Answer.toAnswer(answerDTO);
        answerRepository.save(answer);
    }

    //answer sequence 반환(게시글당 sequence)
    public Long getNextSequenceForBoard(Board board) {
        Long boardId = board.getId();
        Long currentMaxSequence = answerRepository.findMaxSequenceByBoardId(boardId);
        if (currentMaxSequence == null) {
            return 1L;
        }
        return currentMaxSequence + 1;
    }


    //특정 게시글 번호에 맞는 문답글 리스트 전체를 반환
    public List<Answer> viewAnswerList(AnswerDTO answerDTO) {
        Answer answer=Answer.toAnswer(answerDTO);
        System.out.println("viewAnswerList테스트: "+answer.getBoard().getId());
        List<Answer> byBoard = answerRepository.findByBoard(answer.getBoard());
        return byBoard;
    }

    //특정 문답글 삭제
    public void deleteAnswer(AnswerDTO answerDTO) {
        answerRepository.deleteById(answerDTO.getId());
    }

    //특정 문답글 반환
    public Answer findSpecificAnswer(Long id) {
        Answer answer = answerRepository.findById(id).get();
        System.out.println(answer.getId());
        System.out.println(answer.getContent());
        return answer;
    }
    //채택 답변 업데이트
    public void adoptionWithUpdateAnswer(AnswerDTO answerDTO){
        answerDTO.setIsCheckedSolution(ON);
        write(answerDTO);
    }
    //자신이 답변한 것이 있는지 확인
    public boolean chackHasWrittenAnswer(Board board,String memberId){
        List<Answer> findAnswers = findByBoard(board);
        return findAnswers.stream().anyMatch(answer -> answer.getMember().getId().equals(memberId));
    }
}
