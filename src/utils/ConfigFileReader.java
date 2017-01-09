package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import handler.StatHandler;
import handler.UserHandler;
import objects.Game;
import objects.StatColumn;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/** ConfigFileReader
 * Beschreibung: Util-Klasse zum Arbeiten mit ConfigFiles
 * Erstellt von Daniel
 */
public class ConfigFileReader {
  private static String USERS_FILE = "users.json";
  private static String STATS_FOLDER = "stats";

  /**
   * Erstellt eine Instanz des UserHandlers
   * @return UserHandler
   */
  public static UserHandler buildUserHandler() {
    UserHandler userHandler;
    String usersString = readFile(USERS_FILE);
    Gson g = new Gson();
    userHandler = g.fromJson(usersString, UserHandler.class);
    if(userHandler == null) {
      userHandler = new UserHandler();
    }
    return userHandler;
  }

  /**
   * Speichert Benutzer in Configurations-Datei
   * @param userHandler
   */
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

  /**
   * Erstellt eine neue Instanz des StatHandlers aus den Stat-Dateien
   * @param gameList
   * @param userHandler
   * @return StatHandler
   */
  public static StatHandler buildStatsHandler(List<Game> gameList, UserHandler userHandler) {
    StatHandler statHandler = new StatHandler();
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
          statHandler.addGame(game.getName().substring(0, game.getName().length() - 5), statString);
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
      if((entry.getName() + ".json").equals(game.getName())) {
        return true;
      }
    }
    game.delete();
    return false;
  }

  /**
   * Speichert die Stats in den Stat-Dateien
   * @param statHandler
   */
  public static void saveStats(StatHandler statHandler) {
    HashMap<String, StatColumn> stats = statHandler.getStats();
    Set<String> keys = stats.keySet();
    keys.forEach(game -> {
      writeGameStats(stats.get(game), game);
    });

  }

  private static void writeGameStats(StatColumn stats, String game) {
    try (Writer writer = new FileWriter(STATS_FOLDER + "/" + game + ".json")) {
      Gson gson = new GsonBuilder().create();
      gson.toJson(stats, writer);
    } catch (Exception e) {}
  }
}
