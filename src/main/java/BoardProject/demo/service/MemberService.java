package BoardProject.demo.service;

import BoardProject.demo.domain.Member;
import BoardProject.demo.domain.enumSet.QuestReward;
import BoardProject.demo.dto.MemberDTO;
import BoardProject.demo.dto.MemberRankingDTO;
import BoardProject.demo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public Member findMemberById(String id) {
        return memberRepository.findById(id).get();
    }

    //맴버 랭킹 출력
    public List<MemberRankingDTO> getTop10MemberByTotalToken() {
        List<Member> memberlist = memberRepository.findTop10ByOrderByTotalTokenDesc();
        return memberlist.stream()
            .map(MemberRankingDTO::toMemberRankingDTO)
            .collect(Collectors.toList());
    }

    //회원가입기능
    public void write(MemberDTO memberDTO) {
        Member member = Member.toMember(memberDTO);
        String passWord = passwordEncoder.encode(member.getPw());
        member.setPw(passWord);
        memberRepository.save(member);
    }
    //토큰 업데이트
    public void updateToken(MemberDTO memberDTO) {
        Member member = Member.toMember(memberDTO);
        memberRepository.save(member);
    }

    public boolean validateDuplicateMember(Member member) {
        return !memberRepository.findById(member.getId()).isPresent();
    }
    //게시글로 인한 회원 토큰 업데이트
    @Transactional
    public void BoardWithMemberUpdateToken(MemberDTO memberDTO, long rewardToken) {
        try {
            memberDTO.setUsingToken(memberDTO.getUsingToken() + rewardToken);
            updateToken(memberDTO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("회원 정보 업데이트 중 에러 발생");
        }
    }
    //퀘스트 회원 토큰 업데이트
    @Transactional
    public void QuestWithMemberUpdateToken(String memberId, QuestReward questReward) {
        MemberDTO memberDTO = MemberDTO.toMemberDTO(findMemberById(memberId));

        if (questReward != null) {
            memberDTO.setUsingToken(memberDTO.getUsingToken() + questReward.getValue());
            updateToken(memberDTO);
        } else {
            System.out.println("Invalid quest input");
        }
    }
    //채택으로 인한 회원 토큰 업데이트
    @Transactional
    public void adoptionWithMemberUpdateToken(MemberDTO memberDTO, long rewardToken) {
        memberDTO.setUsingToken(memberDTO.getUsingToken() + rewardToken);
        memberDTO.setTotalToken(memberDTO.getTotalToken() + rewardToken);
        updateToken(memberDTO);
    }
    //게시글 작성시 토큰 값 확인
    public boolean chackWithUsingTokenAndRewardToken(MemberDTO memberDTO, long rewardToken){
        return memberDTO.getUsingToken() - rewardToken < 0;
    }
    public boolean isExistMemberById(String id) {
        return memberRepository.existsById(id);
    }

    public boolean isExistMemberByName(String name) {
        return memberRepository.existsByName(name);
    }
}
