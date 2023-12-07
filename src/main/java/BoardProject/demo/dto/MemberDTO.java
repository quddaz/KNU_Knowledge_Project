package BoardProject.demo.dto;

import BoardProject.demo.domain.Answer;
import BoardProject.demo.domain.Board;
import BoardProject.demo.domain.Member;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MemberDTO {

    private String id;


    private String pw;


    private String tel;


    private String name;


    private Long usingToken;


    private Long totalToken;


    private List<Board> boards = new ArrayList<>();


    private List<Answer> answers = new ArrayList<>();

    public static MemberDTO toMemberDTO(Member member) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(member.getId());
        memberDTO.setPw(member.getPw());
        memberDTO.setTel(member.getTel());
        memberDTO.setName(member.getName());
        memberDTO.setUsingToken(member.getUsingToken());
        memberDTO.setTotalToken(member.getTotalToken());
        memberDTO.setBoards(member.getBoards());
        memberDTO.setAnswers(member.getAnswers());
        return memberDTO;
    }
}
