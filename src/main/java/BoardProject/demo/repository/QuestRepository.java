package BoardProject.demo.repository;

import BoardProject.demo.domain.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface QuestRepository extends JpaRepository<Quest, String> {
  @Query("SELECT q FROM Quest q WHERE q.member.id = :memberId AND q.day = :today")
  Optional<Quest> findByMemberIdAndDay(@Param("memberId") String memberId, @Param("today") LocalDate today);

}
