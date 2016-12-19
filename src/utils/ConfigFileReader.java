package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import handler.StatHandler;
import handler.UserHandler;
import objects.Game;

import java.io.*;
import java.util.List;

/**
 * Created by Shakreo on 11.12.2016.
 */
public class ConfigFileReader {
  private static String USERS_FILE = "users.json";
  private static String STATS_FOLDER = "stats";

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

  public static StatHandler buildStatsHandler(List<Game> gameList, UserHandler userHandler) {
    StatHandler statHandler = new StatHandler();//ConfigFileReader.buildStatsHandler(gameList);
    File statFolder = new File(STATS_FOLDER);
    if(!statFolder.exists()) {
      statFolder.mkdir();
    }
    if(!statFolder.isDirectory()) {

    }

    if(statFolder.isDirectory()) {
      File[] statFiles = statFolder.listFiles();
      for(File game : statFiles){
        if(containsGameList(game, gameList)) {
          String statString = readGameFile(game);
          statHandler.addGame(game.getName(), statString);
        }
      }
      for(Game game : gameList){
        if(!statHandler.containsGame(game.getName())) {
          statHandler.newGame(game.getName(), userHandler.getUsers());
        }
      }
    }

    return statHandler;
  }

  private static String readGameFile(File game) {
    String fileString = "";
    BufferedReader input = null;
    try {
      input = new BufferedReader(new FileReader(game));
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

  private static boolean containsGameList(File game, List<Game> gameList) {
    for(Game entry : gameList) {
      if(entry.getName().equals(game.getName())) {
        return true;
      }
    }
    game.delete();
    return false;
  }

}
