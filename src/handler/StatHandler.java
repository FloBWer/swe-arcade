package handler;

import com.google.gson.Gson;
import objects.*;
import utils.ConfigFileReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** StatHandler
 * Beschriebung: Klasse zur Kontrolle der Stat-Einträge
 * Erstellt von Daniel
 */

public class StatHandler {
  private HashMap<String, StatColumn> stats;

  public StatHandler() {
    stats = new HashMap();
  }

  public HashMap<String, StatColumn> getStats() {
    return stats;
  }


  /**
   * Methode zum Hinzufügen der bestehenden Stat-Einträge eines Spiels im StatHandler
   * @param game
   * @param statString
   */
  public void addGame(String game, String statString) {
    Gson g = new Gson();
    try {
      StatColumn row = g.fromJson(statString, StatColumn.class);
      stats.put(game, row);
    }catch (Exception e){
        //Hier direkt neues Jason file für unlesbare Statistiken?
    }
  }

  /**
   * Erstellen eines neuen Spiels mit leeren StatEinträgen
   * @param game
   * @param users
   */
  public void newGame(String game, List<User> users) {
    stats.put(game, new StatColumn(users));
  }

  /**
   * Prüft ob das übergebene Spiel im Handler existiert
   * @param game
   * @return boolean
   */
  public boolean containsGame(String game) {
    return stats.containsKey(game);
  }

  /**
   * Updated die Stats am Ende eines Spiels
   * @param gameReturn
   */
  public void updateStats(GameReturn gameReturn) {
    StatColumn statColumn = (StatColumn)stats.get(gameReturn.getGame());
    statColumn.getUser(gameReturn.getWinner()).addWin();
    statColumn.getUser(gameReturn.getLoser()).addLose();
  }

  /**
   * Gibt eine formatierte ArrayList mit den Stat-Einträgen eines Benutzers zurück.
   * @param userName
   * @return ArrayList
   */
  public List<String> getUserRow(String userName, List<Game> gamesUebergeben) {
    List<String> row = new ArrayList<>();
    row.add(userName);
    row.add("0");      //Gesamt wins
    row.add("0");      //Gesamt loses
    for(Game game:gamesUebergeben){
      StatColumn column = (StatColumn)stats.get(game.getName());
      StatEntry userEntry = column.getUser(userName);
      if(userEntry == null) {
        addUser(userName);
        ConfigFileReader.saveStats(this);
        userEntry = column.getUser(userName);
      }
      row.add(String.valueOf(userEntry.getWins()));
      row.set(1,String.valueOf(Integer.parseInt(row.get(1))+userEntry.getWins()));
      row.add(String.valueOf(userEntry.getLoses()));
      row.set(2,String.valueOf(Integer.parseInt(row.get(2))+userEntry.getLoses()));
      stats.keySet();
    }
    return row;
  }

  /**
   * Ändert den Benutzernamen im StatHandler
   * @param userName
   * @param newName
   */
  public void renameUser(String userName, String newName) {
    stats.keySet().forEach(game -> {
      StatColumn column = (StatColumn)stats.get(game);
      StatEntry userEntry = column.getUser(userName);
      userEntry.setUser(newName);
    });
  }

  /**
   * Updated die Stats am Ende eines Spiels
   * @param returnList
   */
  public void updateStats(List<GameReturn> returnList) {
    returnList.forEach(returnEntry -> {
      updateStats(returnEntry);
    });
  }

  /**
   * Entfernt den übergebenen User aus dem StatHandler
   * @param name
   */
  public void removeUser(String name) {
    stats.keySet().forEach(game -> {
      StatColumn column = (StatColumn)stats.get(game);
      column.removeUser(name);
    });
  }

  /**
   * Fügt einen neuen User im StatHandler hinzu
   * @param name
   */
  public void addUser(String name) {
    stats.keySet().forEach(game -> {
      StatColumn column = (StatColumn)stats.get(game);
      column.addUser(name);
    });
  }
}
