package handler;

import com.google.gson.Gson;
import objects.StatColumn;
import objects.StatEntry;
import objects.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shakreo on 18.12.2016.
 */
public class StatHandler {
  private HashMap stats;

  public StatHandler() {
    stats = new HashMap<String, StatColumn>();
  }

  public HashMap getStats() {
    return stats;
  }

  public void addGame(String game, String statString) {
    Gson g = new Gson();
    StatColumn row = g.fromJson(statString, StatColumn.class);
    stats.put(game, row);
  }

  public void newGame(String game, List<User> users) {
    stats.put(game, new StatColumn(users));
  }

  public boolean containsGame(String game) {
    return stats.containsKey(game);
  }

  public void updateStats(String winner, String loser) {
  }

  public List<String> getUserRow(String userName) {
    List<String> row = new ArrayList<>();
    row.add(userName);
    stats.keySet().forEach(oGame -> {
      String game = (String)oGame;
      StatColumn column = (StatColumn)stats.get(game);
      StatEntry userEntry = column.getUser(userName);
      row.add(String.valueOf(userEntry.getWins()));
      row.add(String.valueOf(userEntry.getLoses()));
    });
    return row;
  }
}
