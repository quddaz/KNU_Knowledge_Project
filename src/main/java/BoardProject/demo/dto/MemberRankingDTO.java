package BoardProject.demo.dto;

import BoardProject.demo.domain.Member;
import BoardProject.demo.dto.enumSet.MemberGrade;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
public class MemberRankingDTO {
  private String name;

  private Long totalToken;

  //디폴트:ordinal ==> 리스트의 인덱스와 유사한 구조로 인식해서 enum타입 순서 변경하면 원하지 않는 값이 나올 수 있음.
  @Enumerated(EnumType.STRING)
  private MemberGrade memberGrade;


  @Builder
  public MemberRankingDTO(String name, Long totalToken){
    this.name = name;
    this.totalToken =totalToken;
    this.memberGrade = setGrade(totalToken);
  }

  public static MemberRankingDTO toMemberRankingDTO(Member member){
    return new MemberRankingDTO(member.getName(), member.getTotalToken());
  }

  public MemberGrade setGrade(Long totalToken) {
    if (totalToken >= 1000) {
      return MemberGrade.GOLD;
    } else if (totalToken >= 500) {
      return MemberGrade.SILVER;
    } else {
      return MemberGrade.BRONZE;
    }
  }
}
