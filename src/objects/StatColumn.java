package objects;

import java.util.ArrayList;
import java.util.List;

/** StatColumn
 * Beschriebung: Klasse zum Speichern der Stat-Einträge eines Spiels
 * Erstellt von Daniel
 */
public class StatColumn {
  private List<StatEntry> statsEntries;

  public StatColumn(List<User> users) {
    statsEntries = new ArrayList<>();
    for(User user : users) {
      statsEntries.add(new StatEntry(user.getName(), 0, 0));
    }
  }

  /**
   * Gibt die Siege dieses Spiels zurück
   * @return
   */
  public List<String> getWinColumn() {
    List<String> wins = new ArrayList();

    return wins;
  }

  /**
   * Gibt die Niederlagen dieses Spiels zurück
   * @return
   */
  public List<String> getLooseColumn() {
    List<String> wins = new ArrayList();

    return wins;
  }

  /**
   * Gibt die Stat Einträge zum angegebenen Benutzer zurück
   * @param userName
   * @return
   */
  public StatEntry getUser(String userName) {
    for(StatEntry entry : statsEntries) {
      if(entry.getUser().equals(userName)) {
        return entry;
      }
    }
    return null;
  }

  /**
   * Entfernt den Benutzer aus den Stats des Spiels
   * @param name
   */
  public void removeUser(String name) {
    for(StatEntry entry : statsEntries) {
      if(entry.getUser().equals(name)) {
        statsEntries.remove(entry);
        return;
      }
    }
  }

  /**
   * Fügt einen neuen Benutzer zu den Spiele-Stats hinzu
   * @param name
   */
  public void addUser(String name) {
    statsEntries.add(new StatEntry(name, 0, 0));
  }
}
