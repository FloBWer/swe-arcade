package windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**<h2>Klasse</h2>
 * <h3>Name:Revanche Controller </h3>
 * <p>
 *     Beschreibung: Controller der Revanche Abfrage
 * </p>
 *<p>Erstellt von Daniel und Florian</p>
 */
public class RevancheController {
  @FXML
  Button revancheJa;
  @FXML
  Button revancheNein;
  private boolean revanche;
  private Stage parent;

  /**
   * Initialisiert die Revanche-Abfrage bei einem Einzelspiel
   * @param parent
   */
  public void initialize(Stage parent) {
    this.parent = parent;
    revanche = false;
  }

  /**
   * Click Event für den Button für Revanche - Ja
   * @param event
   */
  @FXML
  public void clickRevancheJa(ActionEvent event) {
    revanche = true;
    Stage stage = (Stage) revancheJa.getScene().getWindow();
    stage.close();
  }

  /**
   * Click Event für den Button für Revanche - Nein
   * @param event
   */
  @FXML
  public void clickRevancheNein(ActionEvent event) {
    revanche = false;
    Stage stage = (Stage) revancheNein.getScene().getWindow();
    stage.close();
    parent.show();
  }

  /**
   * Rückgabe Methode für den MainController ob eine Revanche gewünscht ist
   * @return boolean
   */
  public boolean getRevanche() {
    return revanche;
  }
}
