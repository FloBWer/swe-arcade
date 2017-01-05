package windows;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
/**<h2>Klasse</h2>
 * <h3>Name: Main</h3>
 * <p>
 *     Beschreibung: Aufruf des Hauptprogramms
 * </p>
 *<p>Erstellt von Dennis</p>
 */

public class Main extends Application {


  /**
   *  <h3>Name: start</h3>
   * <p>
   *     Beschreibung: Anzeige des MainScreens
   * </p>
   *<p>Erstellt von Florian</p>
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
    primaryStage.setTitle("ArcadeBox");
    primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("images/pong.png")));
    primaryStage.setScene(new Scene(root, 600, 400));
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

