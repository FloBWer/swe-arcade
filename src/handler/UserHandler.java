package handler;

import objects.User;
import utils.ConfigFileReader;

import java.util.ArrayList;
import java.util.List;

/** UserHandler
 * Beschriebung: Klasse zur Kontrolle der bestehenden Benutzer
 * Erstellt von Daniel
 */
public class UserHandler {

  private List<User> users;

  public UserHandler() {
    users = new ArrayList();
  }

  //Getter

  public User getUser(String name) {
    for (User user : users) {
      if(user.getName().equals(name)) {
        return user;
      }
    }
    return null;
  }

  public List<User> getUsers() {
    return users;
  }

  //Methods

  /**
   * Erstellt einen neuen User
   * @param name
   * @return User oder null wenn der Benutzer bereits existiert
   */
  public User newUser(String name) {
    name = limitUserName(name);
    if(getUser(name) == null) {
      User newUser = new User(name);
      users.add(newUser);
      saveUsers();
      return newUser;
    }
    return null;
  }

  /**
   * Entfernt Leerzeichen im Benutzernamen und limitiert den Benutzernamen auf 10 Zeichen
   * @param name
   * @return String
   */
  private String limitUserName(String name) {
    String limitedName = name;
    limitedName = limitedName.replaceAll(" ", "");
    if(limitedName.length() > 10) {
      limitedName = limitedName.substring(0, 10);
    }
    return limitedName;
  }

  private void saveUsers() {
    ConfigFileReader.saveUsers(this);
  }

  /**
   * Entfernt den Benutzer aus dem UserHandler
   * @param name
   */
  public void removeUser(String name) {
    users.remove(getUser(name));
    saveUsers();
  }

  /**
   * Ändert den Benutzernamen eines bestehenden Benutzers
   * @param name
   * @param newName
   * @return
   */
  public User changeUser(String name, String newName) {
    User changedUser = newUser(newName);
    if(changedUser != null) {
      removeUser(name);
    }
    return changedUser;
  }
}
