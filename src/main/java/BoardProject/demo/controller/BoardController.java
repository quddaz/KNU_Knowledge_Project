package BoardProject.demo.controller;

import BoardProject.demo.Component.FileStore;
import BoardProject.demo.domain.*;
import BoardProject.demo.domain.enumSet.QuestReward;
import BoardProject.demo.domain.enumSet.isCheckedSolution;
import BoardProject.demo.dto.AnswerDTO;
import BoardProject.demo.dto.BoardDTO;
import BoardProject.demo.dto.MemberDTO;
import BoardProject.demo.dto.MemberRankingDTO;
import BoardProject.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class BoardController {
    private final BoardService boardService;
    private final AnswerService answerService;
    private final MemberService memberService;
    private final AttachmentService attachmentService;
    private final GptAnswerService gptAnswerService;
    private final QuestService questService;
    @Autowired
    public BoardController(BoardService boardService, AnswerService answerService, MemberService memberService, AttachmentService attachmentService, GptAnswerService gptAnswerService, QuestService questService) {
        this.boardService = boardService;
        this.answerService = answerService;
        this.memberService = memberService;
        this.attachmentService = attachmentService;
        this.gptAnswerService = gptAnswerService;
        this.questService = questService;
    }

    //글 작성페이지 url 입력
    @GetMapping("/addBoard")
    public String boardWriteForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        Member findMember = memberService.findMemberById(id);
        model.addAttribute("name", findMember.getName());
        return "board-write";
    }

    @PostMapping("/addBoard")
    public String boardCreate(@ModelAttribute BoardDTO boardDTO, @RequestParam MultipartFile multipartFile, Model model) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        Member findMember = memberService.findMemberById(id);
        Long rewardToken = boardDTO.getRewardToken();

        // 사용자가 게시글을 쓸만큼의 토큰이 있는지 확인
        if (memberService.checkWithUsingTokenAndRewardToken(MemberDTO.toMemberDTO(findMember),rewardToken)) {
            model.addAttribute("message", "보유하신 내공이 부족해 글을 작성할 수 없습니다.");
            model.addAttribute("searchUrl", "/addBoard");
            return "message";
        }

        boardDTO.setMember(findMember);
        boardDTO.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
        boardDTO.setView(0L);
        Board board = boardService.write(boardDTO);
        memberService.BoardWithMemberUpdateToken(MemberDTO.toMemberDTO(findMember), -rewardToken);

        // 파일이 null이 아닌 경우에만 저장
        attachmentService.checkNullByAttachments(multipartFile,board);

        if(!questService.isCreatBoardClearedForMember(board.getMember().getId()))
            memberService.QuestWithMemberUpdateToken(board.getMember().getId(), QuestReward.creatBoard);

        return "redirect:/board/list";
    }

    //게시글 목록 보여주기
    @GetMapping("/board/list")
    public String boardList(
            Model model,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "type", required = false) String type, //검색 타입
            @RequestParam(name = "keyword", required = false) String keyword // 검색 키워드
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName();
        Page<Board> list;
        list = boardService.viewBoardList(type, keyword, pageable); //검색 여부 검사 및 리스트 반환
        List<MemberRankingDTO> memberRankingDTO = memberService.getTop10MemberByTotalToken();

        Quest quest = questService.findQuestByMemberIdAndDay(memberId);

        int nowPage = 1; // 기본값 1로 설정
        if (!list.isEmpty()) {
            nowPage = list.getPageable().getPageNumber() + 1;
        }
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("memberRankingDTO", memberRankingDTO);
        model.addAttribute("quest",quest);

        return "boardList";
    }

    //특정 게시글을 선택했을 때
    @GetMapping("/board/view")
    public String boardView(Long id, Model model) {

        Board board = boardService.findById(id);
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setBoard(board);
        boardService.addView(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName();

        Attachment attachment = attachmentService.findAttachmentsByBoard(board);
        GptAnswer findGptAnswer = gptAnswerService.getGptAnswerByBoardId(id);

        model.addAttribute("gptAnswer", findGptAnswer);
        model.addAttribute("memberId", memberId);
        model.addAttribute("name", memberService.findMemberById(memberId).getName());
        model.addAttribute("boardWriter", board.getMember().getName());
        model.addAttribute("boardDTO", boardService.boardView(id));
        model.addAttribute("list", answerService.viewAnswerList(answerDTO));
        model.addAttribute("rewardToken", board.getRewardToken());
        model.addAttribute("attachment", attachment);
        return "boardView";
    }

    @GetMapping("/board/image")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(Long id) {
        Board board = boardService.findById(id);
        Attachment attachment = attachmentService.findAttachmentsByBoard(board);

        if (attachment != null) {
            try {
                Path file = Paths.get(attachment.getFilePath());
                Resource resource = new UrlResource(file.toUri());

                if (resource.exists() || resource.isReadable()) {
                    HttpHeaders headers = new HttpHeaders();

                    // 확장자에 따라 Content Type 설정
                    String fileExtension = attachment.getFileExtension();
                    switch (fileExtension) {
                        case "jpg":
                        case "jpeg":
                            headers.setContentType(MediaType.IMAGE_JPEG);
                            break;
                        case "png":
                            headers.setContentType(MediaType.IMAGE_PNG);
                            break;
                    }

                    return ResponseEntity.ok().headers(headers).body(resource);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
            } catch (MalformedURLException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //다운로드
    @GetMapping("/board/download")
    public ResponseEntity<Resource> downloadFile(Long id) {
        Board board = boardService.findById(id);
        Attachment attachment = attachmentService.findAttachmentsByBoard(board);
        if (attachment != null) {
            try {
                Path file = Paths.get(attachment.getFilePath());
                Resource resource = new UrlResource(file.toUri());

                if (resource.exists() || resource.isReadable()) {
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                            .body(resource);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
            } catch (MalformedURLException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //특정 게시글에서 게시글삭제 클릭시
    //현재 로그인중인 회원과 게시글을 작성한 회원이 동일할 때만 이루어져야됨
    @GetMapping("/board/delete")
    public String boardDelete(Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName();
        Board findBoard = boardService.findById(id);
        List<Answer> findAnswers = answerService.findByBoard(findBoard);

        //채택한 답변이 있는지 확인 후 채택 안했다면 게시글을 작성한 회원의 usingToken 증가
        boolean isChecked = findAnswers.stream()
            .anyMatch(answer -> answer.getIsCheckedSolution() == isCheckedSolution.ON);
        //채택을 하지 않았다면
        if (!isChecked) {
            memberService.BoardWithMemberUpdateToken(MemberDTO.toMemberDTO(findBoard.getMember()), findBoard.getRewardToken());
        }

        boardService.boardDelete(id);
        model.addAttribute("message", "글이 정상적으로 삭제되었습니다.");
        model.addAttribute("searchUrl", "/board/list");
        return "message";

    }

    //특정 게시글에서 게시글 수정 클릭시
    @GetMapping("/board/update")
    public String boardUpdate(Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("boardDTO", boardService.boardView(id));
        return "board-update";
    }

    //게시글 수정페이지에서 완료버튼을 눌렀을 시
    @PostMapping("/board/updatepro")
    public String boardUpdatePro(Model model, Long id, @ModelAttribute BoardDTO boardDTO,@RequestParam MultipartFile multipartFile) throws IOException {
        Timestamp current = new Timestamp(System.currentTimeMillis());
        BoardDTO boardDTOGet = boardService.boardView(id);
        //이전에 제공한 내공을 다시 돌려받고, 수정한 내공을 다시 차감함
        Member member = boardDTOGet.getMember();
        long currentUsingToken=0;
        if (member != null) {
            currentUsingToken = member.getUsingToken();
        }
        member.setUsingToken(currentUsingToken + boardDTOGet.getRewardToken() - boardDTO.getRewardToken());

        boardDTOGet.setUpdateTime(current);
        boardDTOGet.setTitle(boardDTO.getTitle());
        boardDTOGet.setContent(boardDTO.getContent());
        boardDTOGet.setRewardToken(boardDTO.getRewardToken());
        boardService.write(boardDTOGet);

        // 파일이 null이 아닌 경우에만 저장
        attachmentService.checkNullByAttachments(multipartFile,Board.toBoard(boardDTOGet));

        model.addAttribute("message", "글 수정 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");
        return "message";
    }


}
