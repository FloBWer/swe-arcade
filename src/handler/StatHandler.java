package handler;

import com.google.gson.Gson;
import objects.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shakreo on 18.12.2016.
 */
public class StatHandler {
  private HashMap<String, StatColumn> stats;

  public StatHandler() {
    stats = new HashMap();
  }

  public HashMap<String, StatColumn> getStats() {
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

  public void updateStats(GameReturn gameReturn) {
    StatColumn statColumn = (StatColumn)stats.get(gameReturn.getGame());
    statColumn.getUser(gameReturn.getWinner()).addWin();
    statColumn.getUser(gameReturn.getLoser()).addLose();
  }

  public List<String> getUserRow(String userName) {
    List<String> row = new ArrayList<>();
    row.add(userName);
    stats.keySet().forEach(game -> {
      StatColumn column = (StatColumn)stats.get(game);
      StatEntry userEntry = column.getUser(userName);
      row.add(String.valueOf(userEntry.getWins()));
      row.add(String.valueOf(userEntry.getLoses()));
    });
    return row;
  }

  public void updateStats(List<GameReturn> returnList) {
    returnList.forEach(returnEntry -> {
      updateStats(returnEntry);
    });
  }
}
