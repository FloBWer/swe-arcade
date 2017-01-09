package objects;

/** StatEntry
 * Beschriebung: DatenKlasse zum Speichern der Stat-Einträge eines Benutzers in einem Spiel
 * Erstellt von Daniel
 */
public class StatEntry {
  private String user;
  private int wins;
  private int loses;

  public StatEntry(String user, int wins, int loses) {
    this.user = user;
    this.wins = wins;
    this.loses = loses;
  }

  public String getUser() {
    return user;
  }

  public int getLoses() {
    return loses;
  }

  public int getWins() {
    return wins;
  }

  public void addWin() {
    wins += 1;
  }

  public void addLose() {
    loses += 1;
  }

  public void setUser(String user) {
    this.user = user;
  }
}
