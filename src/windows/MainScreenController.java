package windows;

import handler.UserHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
  ChoiceBox gamesSpielerEins;
  @FXML
  ChoiceBox gamesSpielerZwei;
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
  List<Game> playList;
  UserHandler userHandler;

  public void initialize() {
    userHandler = new UserHandler();
    gamesUebergeben = Game.readGamesFolder();
    gamesSpielen.setDisable(true);
    gamesZurPlaylistHinzufügen.setDisable(true);
    gamesPlaylistStarten.setDisable(true);

    if (gamesUebergeben.size() == 0) {
      gamesVerfuegbareSpiele.getItems().add("Kein Spiel vorhanden!");

    } else {
      for (Game uebergeben : gamesUebergeben) {
        gamesVerfuegbareSpiele.getItems().add(uebergeben.getName());
      }
    }

    //Listener für Markierte Spiele

    gamesVerfuegbareSpiele.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (gamesUebergeben.size() != 0)
          gamesZurPlaylistHinzufügen.setDisable(false);
        gamesSpielen.setDisable(false);
      }
    });

  }

   /* @FXML
    public void clickVerfuegbareSpiele(ActionEvent event){
        if(gamesVerfuegbareSpiele.getSelectionModel().getSelectedItem()!=null
                &&gamesUebergeben.size()!=0){
            gamesZurPlaylistHinzufügen
        }
    }*/


  @FXML
  public void clickBenutzerErstellen(ActionEvent event) {
    User newUser = userHandler.newUser(benutzerTextBoxAnlegen.getText());
    benutzerAuswahlZuAendern.getItems().add(new MenuItem(newUser.getName()));
  }

  @FXML
  public void clickBenutzerLoeschen(ActionEvent event) {

  }

  @FXML
  public void clickBenutzerAendernSpeichern(ActionEvent event) {

  }

  @FXML
  public void clickZurPlaylistHinzufuegen(ActionEvent event) {
    String selected = (String) gamesVerfuegbareSpiele.getSelectionModel().getSelectedItem();
    for (Game uebergeben : gamesUebergeben) {
      if (uebergeben.getName().equals(selected)) {
        gamesAktuellePlaylist.getItems().add(uebergeben.getName());
      }
    }
  }

  @FXML
  public void clickGamesSpielen(ActionEvent event) {
    String selected = (String) gamesVerfuegbareSpiele.getSelectionModel().getSelectedItem();
    String pfad = "";
    for (Game uebergeben : gamesUebergeben) {
      if (uebergeben.getName().equals(selected)) {
        pfad = uebergeben.getPfad();
      }
    }

    Process proc = null;
    try {
      proc = Runtime.getRuntime().exec(
          "java -jar " + pfad + " " + "Hans" + " " + "Wurst");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void clickPlaylistStarten(ActionEvent event) {

  }
}

