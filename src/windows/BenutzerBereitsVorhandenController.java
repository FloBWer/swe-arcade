package windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**<h2>Klasse</h2>
 * <h3>Name: BenutzerBereitsVorhandenController</h3>
 * <p>
 *     Beschreibung: Controller für Warnmeldung falls ein Benutzername schon vorhanden ist
 * </p>
 *<p>Erstellt von Florian</p>
 */
public class BenutzerBereitsVorhandenController {

  @FXML
  Button bereitsVorhandenOK;

  /** <h3>Name: clickBereitsVorhandenOK</h3>
   *  <p>
   *     Beschreibung: Schließt Warnmeldung bei klick auf ok
   * </p>
   *<p>Erstellt von Florian</p>
   */
  public void clickBereitsVorhandenOK(ActionEvent event) {
    Stage stage = (Stage) bereitsVorhandenOK.getScene().getWindow();
    stage.close();
  }
}
