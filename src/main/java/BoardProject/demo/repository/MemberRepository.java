package BoardProject.demo.repository;

import BoardProject.demo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
  List<Member> findTop10ByOrderByTotalTokenDesc();

  Optional<Member> findMemberById(String memberId);

  boolean existsByName(String name);
}
