package objects;

/** GameReturn
 * Beschriebung: DatenKlasse zum Bearbeiten der Rückgabe-Werte eines Spiels
 * Erstellt von Daniel
 */
public class GameReturn {
  private String game;
  private String winner;
  private String loser;

  public void setGame(String game) {
    this.game = game;
  }

  public String getGame() {
    return game;
  }
  public String getWinner() {
    return winner;
  }

  public String getLoser() {
    return loser;
  }
}
