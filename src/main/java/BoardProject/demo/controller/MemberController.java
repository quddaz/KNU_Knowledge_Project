package BoardProject.demo.controller;

import BoardProject.demo.domain.Member;
import BoardProject.demo.domain.Quest;
import BoardProject.demo.dto.MemberDTO;
import BoardProject.demo.service.MemberService;
import BoardProject.demo.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.function.Consumer;

@Controller
public class MemberController {
    private final QuestService questService;
    private final MemberService memberService;
    @Autowired
    public MemberController(QuestService questService, MemberService memberService) {
        this.questService = questService;
        this.memberService = memberService;
    }

    //로그인 페이지
    @GetMapping("/loginPage")
    public String loginPage() {
        return "login";
    }

    //회원가입 버튼 눌렀을 때
    @GetMapping("/addMember")
    public String addMember() {
        return "addMember";
    }

    //회원추가 눌렀을 때
    @PostMapping("/addMember")
    public String addMemberSave(@ModelAttribute MemberDTO memberDTO, Model model) {
        memberDTO.setUsingToken(200L);
        memberDTO.setTotalToken(0L);
        boolean flag = memberService.validateDuplicateMember(Member.toMember(memberDTO));
        if (flag) {
            memberService.write(memberDTO);
            model.addAttribute("message", "회원가입이 완료되었습니다.");
            model.addAttribute("searchUrl", "/loginPage");
        } else {
            model.addAttribute("message", "이미 존재하는 회원입니다.");
            model.addAttribute("searchUrl", "/addMember");
        }
        return "message";

    }

    @GetMapping("/failLogin")
    public String failLogin(Model model) {
        model.addAttribute("message", "아이디 또는 비밀번호 오류");
        model.addAttribute("searchUrl", "/loginPage");
        return "message";
    }

    @GetMapping("/successLogin")
    public String successLogin() {
        return "redirect:/board/list";
    }
}

