package BoardProject.demo.dto;

import BoardProject.demo.domain.Answer;
import BoardProject.demo.domain.Attachment;
import BoardProject.demo.domain.Board;
import BoardProject.demo.domain.Member;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class BoardDTO {

    private Long id;

    private Member member;

    private List<Answer> answers = new ArrayList<>();

    private String title;

    private String content;

    private Long rewardToken;

    private Long view;

    private Timestamp updateTime;

    public static BoardDTO toBoardDTO(Board board) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(board.getId());
        boardDTO.setMember(board.getMember());
        boardDTO.setAnswers(board.getAnswers());
        boardDTO.setTitle(board.getTitle());
        boardDTO.setContent(board.getContent());
        boardDTO.setRewardToken(board.getRewardToken());
        boardDTO.setView(board.getView());
        boardDTO.setUpdateTime(board.getUpdateTime());
        return boardDTO;
    }
}
