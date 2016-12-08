package windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Created by Florian on 06.12.2016.
 */
public class BenutzerBereitsVorhandenController {


    @FXML
    Button bereitsVorhandenOK;

    public void clickBereitsVorhandenOK(ActionEvent event){
        Stage stage = (Stage) bereitsVorhandenOK.getScene().getWindow();
        stage.close();
    }
}
