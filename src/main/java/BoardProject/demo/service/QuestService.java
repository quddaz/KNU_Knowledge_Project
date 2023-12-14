package BoardProject.demo.service;

import BoardProject.demo.domain.Member;
import BoardProject.demo.domain.Quest;
import BoardProject.demo.repository.MemberRepository;
import BoardProject.demo.repository.QuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class QuestService {
  private final QuestRepository questRepository;
  private  final MemberRepository memberRepository;
  @Autowired
  public QuestService(QuestRepository questRepository, MemberRepository memberRepository) {
    this.questRepository = questRepository;
    this.memberRepository = memberRepository;
  }

  /**
   * 주어진 회원 ID에 해당하는 퀘스트를 찾아 반환합니다. 만약 해당 날짜의 퀘스트가 없다면 새로운 퀘스트를 생성합니다.
   *
   * @param memberId 조회할 회원의 ID
   * @return 조회된 퀘스트 또는 새로 생성된 퀘스트
   */
  public Quest findQuestByMemberIdAndDay(String memberId) {
    LocalDate today = LocalDate.now();
    return questRepository.findByMemberIdAndDay(memberId, today).orElseGet(() -> createQuestForMember(memberId));
  }


  /**
   * 주어진 회원 ID에 해당하는 회원의 입양 퀘스트를 확인하고 클리어 여부를 체크합니다.
   * 클리어되지 않은 경우 클리어 처리를 하고 true를 반환하며, 이미 클리어된 경우 false를 반환합니다.
   *
   * @param memberId 클리어 여부를 확인할 회원의 ID
   * @return 클리어 여부 (클리어된 경우 true, 클리어되지 않은 경우 false)
   */
  public boolean isAdoptionClearedForMember(String memberId) {
    Quest quest = findQuestByMemberIdAndDay(memberId);

    if (!quest.isAdoption()) {
      quest.setAdoption(true);
      questRepository.save(quest);
      return false;
    }
    return true;
  }

  /**
   * 주어진 회원 ID에 해당하는 회원의 게시글 작성 퀘스트를 확인하고 클리어 여부를 체크합니다.
   * 클리어되지 않은 경우 클리어 처리를 하고 true를 반환하며, 이미 클리어된 경우 false를 반환합니다.
   *
   * @param memberId 클리어 여부를 확인할 회원의 ID
   * @return 클리어 여부 (클리어된 경우 true, 클리어되지 않은 경우 false)
   */
  public boolean isCreatBoardClearedForMember(String memberId) {
    Quest quest = findQuestByMemberIdAndDay(memberId);

    if (!quest.isCreatBoard()) {
      quest.setCreatBoard(true);
      questRepository.save(quest);
      return false;
    }

    return true;
  }
  /**
   * 주어진 회원 ID에 해당하는 회원의 답변 작성 퀘스트를 확인하고 클리어 여부를 체크합니다.
   * 클리어되지 않은 경우 클리어 처리를 하고 true를 반환하며, 이미 클리어된 경우 false를 반환합니다.
   *
   * @param memberId 클리어 여부를 확인할 회원의 ID
   * @return 클리어 여부 (클리어된 경우 true, 클리어되지 않은 경우 false)
   */
  public boolean isCreatAnswerClearedForMember(String memberId) {
    Quest quest = findQuestByMemberIdAndDay(memberId);

    if (!quest.isCreatAnswer()) {
      quest.setCreatAnswer(true);
      questRepository.save(quest);
      return false;
    }

    return true;
  }

  /**
   * 주어진 회원 ID에 해당하는 퀘스트를 생성하고 저장합니다.
   * 만약 주어진 ID에 해당하는 회원이 존재하지 않으면 RuntimeException을 던집니다.
   * 
   * @param memberId 생성할 퀘스트의 회원 ID
   * @return 생성된 퀘스트
   * @throws RuntimeException 주어진 ID에 해당하는 회원이 존재하지 않는 경우
   */
  public Quest createQuestForMember(String memberId) {
    Member member = memberRepository.findMemberById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));

    Quest newQuest = Quest.builder()
        .member(member)
        .build();

    return questRepository.save(newQuest);
  }
}
