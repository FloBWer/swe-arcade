package objects;

/**
 * Created by Shakreo on 06.12.2016.
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
