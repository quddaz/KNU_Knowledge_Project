package BoardProject.demo.domain;

import BoardProject.demo.dto.AnswerDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @Column(name = "answer_sequence")
    private Long sequence;

    //answer:board=>n:1관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    //answer:member=>n:1관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @Column(name = "content", length = 1000)
    private String content;


    @Enumerated(EnumType.STRING)
    private BoardProject.demo.domain.enumSet.isCheckedSolution isCheckedSolution;

    public static Answer toAnswer(AnswerDTO answerDTO) {
        Answer answer = new Answer();
        answer.setId(answerDTO.getId());
        answer.setBoard(answerDTO.getBoard());
        answer.setSequence(answerDTO.getSequence());
        answer.setMember(answerDTO.getMember());
        answer.setContent(answerDTO.getContent());
        answer.setIsCheckedSolution(answerDTO.getIsCheckedSolution());
        return answer;
    }
}
