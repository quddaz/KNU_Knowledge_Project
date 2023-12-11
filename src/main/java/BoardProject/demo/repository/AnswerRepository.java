package BoardProject.demo.repository;

import BoardProject.demo.domain.Answer;
import BoardProject.demo.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByBoard(Board board);

    @Query("SELECT MAX(a.sequence) FROM Answer a WHERE a.board.id = :boardId")
    Long findMaxSequenceByBoardId(@Param("boardId") Long boardId);
}
