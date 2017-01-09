package objects;

/** User
 * Beschriebung: DatenKlasse zum Speichern eines Benutzers
 * Erstellt von Daniel
 */
public class User {
  private String name;

  public User(String name) {
    this.name = name;
  }

  //Getter
  public String getName() {
    return this.name;
  }

  //Setter
  public void setName(String name) {
    this.name = name;
  }
}
