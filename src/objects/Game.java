package objects;

import java.io.File;
import java.util.*;
import java.nio.file.Paths;

/**<h2>Klasse</h2>
 * <h3>Name: Game</h3>
 * <p>
 *     Beschreibung: Klasse für Spiele mit Attributen  name und pfad
 *
 * </p>
 *<p>Erstellt von Florian</p>
 */
public class Game {
  //Attrbute
  private String name;
  private String pfad;

  //Methoden
  public Game(String name, String pfad) {
    this.name = name;
    this.pfad = pfad;
  }

  public String getName() {
    return this.name;
  }

  public String getPfad() {
    return this.pfad;
  }
  /**<h2>Klasse</h2>
   * <h3>Name: readGamesFolder</h3>
   * <p>
   *     Beschreibung: Statische Methode zum lesen der Spiele aus dem Games ordner,
   *     wenn es sich um passende jar dateien handelt werden Games Objekte für diese Dateien angelegt
   *     und alle Ojekte als Liste zurückgegeben
   *
   * </p>
   *<p>Erstellt von Dennis</p>
   */
  public static List<Game> readGamesFolder() {
    List<Game> gamesInFolder = new ArrayList<>();
    File f = new File("Games");
    try {

      if (!f.exists() || !f.isDirectory()) {
        f.mkdir();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    File[] fileArray = f.listFiles();
    for (File gelesen : fileArray) {
      if (gelesen.getName().endsWith(".jar")) {
        String nameAktuell = gelesen.getName().substring(0, (gelesen.getName().length() - 4));
        String pfadAktuell = gelesen.getPath();
        Game aktuellesSpiel = new Game(nameAktuell, pfadAktuell);
        gamesInFolder.add(aktuellesSpiel);
      }
    }
    return gamesInFolder;
  }

}
