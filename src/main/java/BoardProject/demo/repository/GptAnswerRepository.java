package BoardProject.demo.repository;

import BoardProject.demo.domain.GptAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GptAnswerRepository extends JpaRepository<GptAnswer, Long> {
    Optional<GptAnswer> findByBoardId(Long id);

}
