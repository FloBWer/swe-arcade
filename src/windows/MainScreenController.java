package windows;

import handler.UserHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import objects.Game;
import objects.User;

import java.io.File;
import java.util.List;

public class MainScreenController {


    @FXML
    Button gamesSpielen;
    @FXML
    Button gamesZurPlaylistHinzufügen;
    @FXML
    SplitMenuButton gamesSpielerEins;
    @FXML
    SplitMenuButton gamesSpielerZwei;
    @FXML
    Button gamesPlaylistStarten;
    @FXML
    ListView gamesAktuellePlaylist;
    @FXML
    ListView gamesVerfuegbareSpiele;
    @FXML
    TextField benutzerTextBoxAnlegen;
    @FXML
    Button benutzerErstellen;
    @FXML
    SplitMenuButton benutzerAuswahlZuAendern;
    @FXML
    Button benutzerLoeschen;
    @FXML
    TextField benutzerTextBoxAendern;
    @FXML
    Button benutzerAendernSpeichern;

    List<Game> gamesUebergeben;
    UserHandler userHandler;

    public void initialize(){
        userHandler = new UserHandler();
        gamesUebergeben= Game.readGamesFolder();
        for (Game uebergeben : gamesUebergeben) {
            gamesVerfuegbareSpiele.getItems().add(uebergeben.getName());
        }
    }


    @FXML
    public void clickBenutzerErstellen(ActionEvent event){
        User newUser = userHandler.newUser(benutzerTextBoxAnlegen.getText());
        benutzerAuswahlZuAendern.getItems().add(new MenuItem(newUser.getName()));
    }

    @FXML
    public void clickBenutzerLoeschen(ActionEvent event){

    }
    @FXML
    public void clickBenutzerAendernSpeichern(ActionEvent event){

    }

    @FXML
    public void clickZurPlaylistHinzufuegen(ActionEvent event){

    }

    @FXML
    public void clickGamesSpielen(ActionEvent event){
        String selected=(String)gamesVerfuegbareSpiele.getSelectionModel().getSelectedItem();
        String pfad="";
        for (Game uebergeben : gamesUebergeben) {
            if(uebergeben.getName().equals(selected)){
                pfad=uebergeben.getPfad();
            }
        }

        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec(
                    "java -jar " + pfad + " " + "Hans" + " " + "Wurst");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void clickPlaylistStarten(ActionEvent event){

    }
}

