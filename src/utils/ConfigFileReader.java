package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import handler.UserHandler;

import java.io.*;

/**
 * Created by Shakreo on 11.12.2016.
 */
public class ConfigFileReader {
  private static String USERS_FILE = "users.json";

  public static UserHandler buildUserHandler() {
    UserHandler userHandler = new UserHandler();
    String usersString = readFile(USERS_FILE);
    Gson g = new Gson();
    userHandler = g.fromJson(usersString, UserHandler.class);



    System.out.println(userHandler); //John
    System.out.println(g.toJson(userHandler)); // {"name":"John"}
    return userHandler;
  }

  public static void saveUsers(UserHandler userHandler) {
    try (Writer writer = new FileWriter(USERS_FILE)) {
      Gson gson = new GsonBuilder().create();
      gson.toJson(userHandler, writer);
    } catch (Exception e) {}
  }

  private static String readFile(String fileName) {
    String fileString = "";
    BufferedReader input = null;
    try {
      File file = new File(fileName);
      file.createNewFile();

      input = new BufferedReader(new FileReader(file));
      String line;
      while((line = input.readLine()) != null) {
        fileString += line;
      }
      input.close();
    } catch (Exception e) {}
    finally {
      if (input != null)
        try {
          input.close();
        } catch (IOException e) {
        }
    }
    return fileString;
  }
}
