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
public class QuestService {
  private final QuestRepository questRepository;
  private  final MemberRepository memberRepository;
  @Autowired
  public QuestService(QuestRepository questRepository, MemberRepository memberRepository) {
    this.questRepository = questRepository;
    this.memberRepository = memberRepository;
  }
  public Quest findQuestByMemberIdAndDay(String memberId) {
    LocalDate today = LocalDate.now();
    return questRepository.findByMemberIdAndDay(memberId, today).orElseGet(() -> createQuestForMember(memberId));
  }


  // memberId에 해당하는 멤버의 퀘스트를 가져와 클리어 여부를 체크
  @Transactional
  public boolean isAdoptionClearedForMember(String memberId) {
    Quest quest = findQuestByMemberIdAndDay(memberId);

    if (!quest.isAdoption()) {
      quest.setAdoption(true);
      questRepository.save(quest);
      return false;
    }
    return true;
  }
  @Transactional
  public boolean isCreatBoardClearedForMember(String memberId) {
    Quest quest = findQuestByMemberIdAndDay(memberId);

    if (!quest.isCreatBoard()) {
      quest.setCreatBoard(true);
      questRepository.save(quest);
      return false;
    }

    return true;
  }

  @Transactional
  public boolean isCreatAnswerClearedForMember(String memberId) {
    Quest quest = findQuestByMemberIdAndDay(memberId);

    if (!quest.isCreatAnswer()) {
      quest.setCreatAnswer(true);
      questRepository.save(quest);
      return false;
    }

    return true;
  }
  //Optinal 예외처리
  public Quest createQuestForMember(String memberId) {
    Member member = memberRepository.findMemberById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));

    Quest newQuest = Quest.builder()
        .member(member)
        .build();

    return questRepository.save(newQuest);
  }


}
