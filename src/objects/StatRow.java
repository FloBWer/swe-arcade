package objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shakreo on 18.12.2016.
 */
public class StatRow {
  private List<StatEntry> statsEntries;

  public StatRow(List<User> users) {
    statsEntries = new ArrayList<>();
    for(User user : users) {
      statsEntries.add(new StatEntry(user.getName(), 0, 0));
    }
  }
}
