package objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shakreo on 18.12.2016.
 */
public class StatColumn {
  private List<StatEntry> statsEntries;

  public StatColumn(List<User> users) {
    statsEntries = new ArrayList<>();
    for(User user : users) {
      statsEntries.add(new StatEntry(user.getName(), 0, 0));
    }
  }

  public List<String> getWinColumn() {
    List<String> wins = new ArrayList();

    return wins;
  }

  public List<String> getLooseColumn() {
    List<String> wins = new ArrayList();

    return wins;
  }

  public StatEntry getUser(String userName) {
    for(StatEntry entry : statsEntries) {
      if(entry.getUser().equals(userName)) {
        return entry;
      }
    }
    return null;
  }

  public void removeUser(String name) {
    for(StatEntry entry : statsEntries) {
      if(entry.getUser().equals(name)) {
        statsEntries.remove(entry);
        return;
      }
    }
  }

  public void addUser(String name) {
    statsEntries.add(new StatEntry(name, 0, 0));
  }
}
