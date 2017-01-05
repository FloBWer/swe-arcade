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
  
  public void initialize(Stage parent) {
    this.parent = parent;
    revanche = false;
  }

  @FXML
  public void clickRevancheJa(ActionEvent event) {
    revanche = true;
    Stage stage = (Stage) revancheJa.getScene().getWindow();
    stage.close();
  }

  @FXML
  public void clickRevancheNein(ActionEvent event) {
    revanche = false;
    Stage stage = (Stage) revancheNein.getScene().getWindow();
    stage.close();
    parent.show();
  }

  public boolean getRevanche() {
    return revanche;
  }
}
