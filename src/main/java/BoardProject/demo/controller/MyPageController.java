package BoardProject.demo.controller;

import BoardProject.demo.domain.Board;
import BoardProject.demo.domain.Member;
import BoardProject.demo.dto.MemberDTO;
import BoardProject.demo.service.BoardService;
import BoardProject.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MyPageController {
    private final MemberService memberService;
    private final BoardService boardService;

    @GetMapping("/view/memberInfo")
    public String getMemberInfo(Model model,
                                @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName();
        Member member = memberService.findMemberById(memberId);

        Page<Board> list = boardService.allBoardList(pageable);
        list = boardService.isMyBoard(list, memberId);

        int nowPage = 1; // 기본값 1로 설정
        if (!list.isEmpty()) {
            nowPage = list.getPageable().getPageNumber() + 1;
        }

        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("memberDTO", member);
        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "memberInfo";
    }

    @GetMapping("/member/update")
    public String memberUpdate(@ModelAttribute(name = "id") String memberId, Model model) {
        model.addAttribute("id", memberId);
        return "updateMember";
    }

    @PostMapping("/member/updateComplete")
    public String memberUpdateComplete(MemberDTO memberDTO) {
        Member findMember = memberService.findMemberById(memberDTO.getId());
        findMember.setPw(memberDTO.getPw());
        findMember.setTel(memberDTO.getTel());
        findMember.setName(memberDTO.getName());
        memberService.write(MemberDTO.toMemberDTO(findMember));
        return "redirect:/view/memberInfo";
    }
}
