package handler;

import com.google.gson.Gson;
import objects.StatRow;
import objects.User;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Shakreo on 18.12.2016.
 */
public class StatHandler {
  private HashMap stats;

  public StatHandler() {
    stats = new HashMap<String, StatRow>();
  }

  public void addGame(String game, String statString) {
    Gson g = new Gson();
    StatRow row = g.fromJson(statString, StatRow.class);
    stats.put(game, row);
  }

  public void newGame(String game, List<User> users) {
    stats.put(game, new StatRow(users));
  }

  public boolean containsGame(String game) {
    return stats.containsKey(game);
  }

  public void updateStats() {
  }
}
