package BoardProject.demo.controller;

import BoardProject.demo.domain.Answer;
import BoardProject.demo.domain.Board;
import BoardProject.demo.domain.Member;
import BoardProject.demo.domain.enumSet.QuestReward;
import BoardProject.demo.domain.enumSet.isCheckedSolution;
import BoardProject.demo.dto.AnswerDTO;
import BoardProject.demo.dto.MemberDTO;
import BoardProject.demo.service.AnswerService;
import BoardProject.demo.service.BoardService;
import BoardProject.demo.service.MemberService;
import BoardProject.demo.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static BoardProject.demo.domain.enumSet.isCheckedSolution.OFF;

@Controller
public class AnswerController {
    private final AnswerService answerService;
    private final BoardService boardService;
    private final MemberService memberService;
    private final QuestService questService;

    @Autowired
    public AnswerController(AnswerService answerService, BoardService boardService, MemberService memberService, QuestService questService) {
        this.answerService = answerService;
        this.boardService = boardService;
        this.memberService = memberService;
        this.questService = questService;
    }

    //답변추가 눌렀을 때
    @PostMapping("/answer/write")
    public String writeAnswer(Long id, String content, Model model) {
        Board board = boardService.findById(id);
        Long sequence = answerService.getNextSequenceForBoard(board);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName();

        //같은 게시글안의 문답글중 memberId와 같은 id를 가진 회원이 없어야 한다.
        if (answerService.checkHasWrittenAnswer(board, memberId)) {
            model.addAttribute("message", "이미 작성하신 답글이 존재합니다.");
            model.addAttribute("searchUrl", "/board/view?id=" + id);
            return "message";
        }


        if(!questService.isCreatAnswerClearedForMember(memberId))
            memberService.QuestWithMemberUpdateToken(memberId, QuestReward.creatAnswer);

        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setMember(memberService.findMemberById(memberId));
        answerDTO.setBoard(board);
        answerDTO.setContent(content);
        answerDTO.setSequence(sequence);
        answerDTO.setIsCheckedSolution(OFF);
        answerService.write(answerDTO);
        return "redirect:/board/view?id=" + id;
    }

    //문답글 삭제 버튼 눌렀을 때
    //문답글 삭제를 누를때 해당 문답글을 작성한 회원인지 확인해야됨
    @GetMapping("/answer/delete")
    public String deleteAnswer(Long id) {
        Answer findAnswer = answerService.findSpecificAnswer(id);

        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setId(findAnswer.getId());
        answerService.deleteAnswer(answerDTO);
        return "redirect:/board/list";

    }


    //문답글 업데이트 버튼 눌렀을 때
    //동일한 회원이 하나의 게시글에 하나의 답글만 작성해야 됨(기능추가)
    @GetMapping("/answer/update")
    public String updateAnswer(Long id, Model model) {
        Answer findAnswer = answerService.findSpecificAnswer(id);
        model.addAttribute("answerDTO", findAnswer);
        return "answer-update";
    }

    @PostMapping("/answer/updatepro")
    public String updateAnswerPro(Model model, @ModelAttribute AnswerDTO answerDTO, Long id) {
        System.out.println("문답글 수정테스트");
        AnswerDTO answerDTOGet = AnswerDTO.answerDTO(answerService.findSpecificAnswer(id));
        answerDTOGet.setContent(answerDTO.getContent());
        answerService.write(answerDTOGet);
        model.addAttribute("message", "문답글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");
        return "message";
    }

    @GetMapping("/answer/adoption")
    public String adoptAnswer(@RequestParam("answerId") Long answerId,
                              @RequestParam("boardId") Long boardId,
                              @RequestParam("rewardToken") Long rewardToken,
                              Model model) {
        //채택된 답변이 없어야 함
        Board findBoard = boardService.findById(boardId);
        List<Answer> findAnswers = answerService.findByBoard(findBoard);

        if (findAnswers.stream().anyMatch(answer -> answer.getIsCheckedSolution() == isCheckedSolution.ON)) {
            model.addAttribute("message", "이미 답변을 채택하셨습니다.");
            model.addAttribute("searchUrl", "/board/view?id=" + boardId);
            return "message";
        }

        if(!questService.isAdoptionClearedForMember(findBoard.getMember().getId()))
            memberService.QuestWithMemberUpdateToken(findBoard.getMember().getId(),QuestReward.adoption);

        AnswerDTO answerDTO = AnswerDTO.answerDTO(answerService.findSpecificAnswer(answerId));
        Member findMember = answerDTO.getMember();
        memberService.adoptionWithMemberUpdateToken(MemberDTO.toMemberDTO(findMember),rewardToken);
        answerService.adoptionWithUpdateAnswer(answerDTO);
        return "redirect:/board/view?id=" + boardId;
    }
}
