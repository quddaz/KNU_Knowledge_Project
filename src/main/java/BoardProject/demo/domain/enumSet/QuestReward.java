package BoardProject.demo.domain.enumSet;

import java.util.Arrays;

public enum QuestReward {
  adoption(100),
  creatBoard(100),
  creatAnswer(50);

  private final Long value;

  QuestReward(long value) {
    this.value = value;
  }

  public Long getValue() {
    return value;
  }

  public static QuestReward getQuestRewardByInput(String Input) {
    return Arrays.stream(QuestReward.values())
        .filter(reward -> reward.name().equalsIgnoreCase(Input))
        .findFirst()
        .orElse(null);
  }
}
