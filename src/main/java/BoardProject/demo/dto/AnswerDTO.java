package BoardProject.demo.dto;

import BoardProject.demo.domain.Answer;
import BoardProject.demo.domain.Board;
import BoardProject.demo.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnswerDTO {
    private Long id;

    private Long sequence;

    private Board board;

    private Member member;

    private String content;

    @Enumerated(EnumType.STRING)
    private BoardProject.demo.domain.enumSet.isCheckedSolution isCheckedSolution;

    public static AnswerDTO answerDTO(Answer answer) {
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setId(answer.getId());
        answerDTO.setSequence(answer.getSequence());
        answerDTO.setBoard(answer.getBoard());
        answerDTO.setMember(answer.getMember());
        answerDTO.setContent(answer.getContent());
        answerDTO.setIsCheckedSolution(answer.getIsCheckedSolution());
        return answerDTO;
    }
}
