package BoardProject.demo.domain;

import BoardProject.demo.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @Column(name = "member_id")
    private String id;

    @Column(name = "member_pw")
    private String pw;

    @Column(name = "member_tel")
    private String tel;

    @Column(name = "member_name",unique = true)
    private String name;

    @Column(name = "member_using_token")
    private Long usingToken;

    @Column(name = "member_total_token")
    private Long totalToken;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Quest> quests = new ArrayList<>();




    public static Member toMember(MemberDTO memberDTO) {
        Member member = new Member();
        member.setId(memberDTO.getId());
        member.setPw(memberDTO.getPw());
        member.setTel(memberDTO.getTel());
        member.setName(memberDTO.getName());
        member.setUsingToken(memberDTO.getUsingToken());
        member.setTotalToken(memberDTO.getTotalToken());
        member.setBoards(memberDTO.getBoards());
        member.setAnswers(memberDTO.getAnswers());
        return member;
    }
}
