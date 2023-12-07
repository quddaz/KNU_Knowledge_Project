package BoardProject.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GptAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gpt_answer_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(name = "gpt_answer_content",length = 50000)
    private String content;

    public GptAnswer() {
    }

    public GptAnswer(Board board, String content) {
        this.board = board;
        this.content = content;
    }
}
