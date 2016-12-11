package handler;

import objects.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shakreo on 06.12.2016.
 */
public class UserHandler {

  private List<User> users;

  public UserHandler() {
    users = new ArrayList<User>();
    init();
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

  //Methods

  public User newUser(String name) {
    User newUser = new User(name);
    if(!users.contains(newUser)) {
      users.add(newUser);
    }
    return newUser;
  }

  private void init() {

  }
}
