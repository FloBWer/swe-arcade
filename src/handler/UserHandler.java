package handler;

import objects.User;
import utils.ConfigFileReader;

import java.util.List;

/**
 * Created by Shakreo on 06.12.2016.
 */
public class UserHandler {

  private List<User> users;

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

  public User newUser(String name) {
    if(getUser(name) == null) {
      User newUser = new User(name);
      users.add(newUser);
      saveUsers();
      return newUser;
    }
    return null;
  }

  private void saveUsers() {
    ConfigFileReader.saveUsers(this);
  }

  public void removeUser(String name) {
    users.remove(getUser(name));
    saveUsers();
  }

  public User changeUser(String name, String newName) {
    User changedUser = newUser(newName);
    if(changedUser != null) {
      removeUser(name);
    }
    return changedUser;
  }
}
