package BoardProject.demo.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Attachment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String originFilename;

  @Column(nullable = false)
  private String storeFilename;

  @Column(nullable = false)
  private String filePath;

  @Column(nullable = false)
  private String fileExtension;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id")
  private Board board;


  @Builder
  public Attachment(String originFilename, String storePath, String filePath, String fileExtension, Board board) {
    this.originFilename = originFilename;
    this.storeFilename = storePath;
    this.filePath = filePath;
    this.fileExtension = fileExtension;
    this.board = board;
  }
  public Attachment(){

  }
}