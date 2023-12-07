package BoardProject.demo.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class Quest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //board:member=>n:1관계
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  private LocalDate day;

  private boolean adoption;

  private boolean creatBoard;

  private boolean creatAnswer;
  @Builder
  public Quest(Member member){
    this.member = member;
    this.day = LocalDate.now();
    this.adoption = false;
    this.creatBoard = false;
    this.creatAnswer = false;
  }

  public Quest() {
  }
}
