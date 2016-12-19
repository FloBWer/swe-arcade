package objects;

/**
 * Created by Shakreo on 18.12.2016.
 */
public class StatEntry {
  private String user;
  private int wins;
  private int looses;

  public StatEntry(String user, int wins, int looses) {
    this.user = user;
    this.wins = wins;
    this.looses = looses;
  }
}
