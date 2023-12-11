package BoardProject.demo.repository;

import BoardProject.demo.domain.Board;
import BoardProject.demo.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
  Page<Board> findByTitleContaining(String title, Pageable pageable);
  Page<Board> findByMember_Name(String name, Pageable pageable);
}
