package BoardProject.demo.repository;


import BoardProject.demo.domain.Attachment;
import BoardProject.demo.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    Attachment findByBoard(Board board);

    Attachment findByBoardId(Long id);

    void deleteByBoardId(Long id);
}