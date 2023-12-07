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
@Transactional
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

    /*
     * 맴버 랭킹에서 상위 10명의 회원 정보를 가져와 MemberRankingDTO로 변환하여 반환합니다.
     *
     * @return 상위 10명의 회원 랭킹 정보 목록
     */
    public List<MemberRankingDTO> getTop10MemberByTotalToken() {
        List<Member> memberList = memberRepository.findTop10ByOrderByTotalTokenDesc();
        return memberList.stream()
            .map(MemberRankingDTO::toMemberRankingDTO)
            .collect(Collectors.toList());
    }

    /*
     * 주어진 MemberDTO를 사용하여 회원을 등록합니다.
     *
     * @param memberDTO 등록할 회원 정보를 담은 DTO
     */
    public void write(MemberDTO memberDTO) {
        Member member = Member.toMember(memberDTO);
        String hashedPassword = passwordEncoder.encode(member.getPw());
        member.setPw(hashedPassword);
        memberRepository.save(member);
    }

    /*
     * 주어진 MemberDTO를 사용하여 회원의 토큰을 업데이트합니다.
     *
     * @param memberDTO 토큰을 업데이트할 회원 정보를 담은 DTO
     */
    public void updateToken(MemberDTO memberDTO) {
        Member member = Member.toMember(memberDTO);
        memberRepository.save(member);
    }

    /*
     * 주어진 회원 정보가 중복되지 않는지 확인합니다.
     *
     * @param member 회원 엔터티
     * @return 중복되지 않으면 true, 중복되면 false
     */
    public boolean validateDuplicateMember(Member member) {
        return !memberRepository.findById(member.getId()).isPresent();
    }

    /*
     * 주어진 회원 정보를 사용하여 게시글로 인한 토큰 업데이트를 수행합니다.
     *
     * @param memberDTO   토큰을 업데이트할 회원 정보를 담은 DTO
     * @param rewardToken 업데이트할 토큰 양
     */
    public void BoardWithMemberUpdateToken(MemberDTO memberDTO, long rewardToken) {
        try {
            memberDTO.setUsingToken(memberDTO.getUsingToken() + rewardToken);
            updateToken(memberDTO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("회원 정보 업데이트 중 에러 발생");
        }
    }

    /*
     * 주어진 회원 ID와 퀘스트 보상을 사용하여 퀘스트로 인한 토큰 업데이트를 수행합니다.
     *
     * @param memberId     업데이트할 회원의 ID
     * @param questReward  퀘스트 보상 정보
     */
    public void QuestWithMemberUpdateToken(String memberId, QuestReward questReward) {
        MemberDTO memberDTO = MemberDTO.toMemberDTO(findMemberById(memberId));

        if (questReward != null) {
            memberDTO.setUsingToken(memberDTO.getUsingToken() + questReward.getValue());
            updateToken(memberDTO);
        } else {
            System.out.println("Invalid quest input");
        }
    }

    /*
     * 주어진 MemberDTO를 사용하여 채택으로 인한 토큰 업데이트를 수행합니다.
     *
     * @param memberDTO   토큰을 업데이트할 회원 정보를 담은 DTO
     * @param rewardToken 업데이트할 토큰 양
     */
    public void adoptionWithMemberUpdateToken(MemberDTO memberDTO, long rewardToken) {
        memberDTO.setUsingToken(memberDTO.getUsingToken() + rewardToken);
        memberDTO.setTotalToken(memberDTO.getTotalToken() + rewardToken);
        updateToken(memberDTO);
    }

    /*
     * 주어진 MemberDTO와 토큰 보상을 사용하여 게시글 작성 시 토큰이 충분한지 확인합니다.
     *
     * @param memberDTO   토큰을 확인할 회원 정보를 담은 DTO
     * @param rewardToken 작성된 게시글에 대한 토큰 보상 양
     * @return 토큰이 충분하면 true, 부족하면 false
     */
    public boolean checkWithUsingTokenAndRewardToken(MemberDTO memberDTO, long rewardToken) {
        return memberDTO.getUsingToken() - rewardToken < 0;
    }

    /*
     * 주어진 회원 ID가 존재하는지 확인합니다.
     *
     * @param id 확인할 회원의 ID
     * @return 존재하면 true, 그렇지 않으면 false
     */
    public boolean isExistMemberById(String id) {
        return memberRepository.existsById(id);
    }

    /*
     * 주어진 회원 이름이 존재하는지 확인합니다.
     *
     * @param name 확인할 회원의 이름
     * @return 존재하면 true, 그렇지 않으면 false
     */
    public boolean isExistMemberByName(String name) {
        return memberRepository.existsByName(name);
    }
}
