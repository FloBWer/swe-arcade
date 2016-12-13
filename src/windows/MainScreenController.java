package windows;

import handler.UserHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
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
  ComboBox gamesSpielerEins;
  @FXML
  ComboBox gamesSpielerZwei;
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
        if (gamesUebergeben.size() != 0) {
          gamesZurPlaylistHinzufügen.setDisable(false);
        }
        if(gamesUebergeben.size()!=0
            &&gamesSpielerEins.getSelectionModel().getSelectedItem()!=null
            &&gamesSpielerZwei.getSelectionModel().getSelectedItem()!=null){
          gamesSpielen.setDisable(false);
        }
      }
    });

    //Listener für Playlist

    gamesVerfuegbareSpiele.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {


      }
    });

  }



  @FXML
  public void clickBenutzerErstellen(ActionEvent event) {
    User newUser = userHandler.newUser(benutzerTextBoxAnlegen.getText());
    benutzerAuswahlZuAendern.getItems().add(new MenuItem(newUser.getName()));
    //Test im Bezug auf Namensübergabe
    gamesSpielerEins.getItems().add(newUser.getName());
    gamesSpielerZwei.getItems().add(newUser.getName());

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
        gamesAktuellePlaylist.setCellFactory(new Callback<ListView<String>, ListCell>() {

          public ListCell call(ListView<String> param) {
            return new ButtonListCell();
          }

        });
        playlistAktivieren();
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
    Process proc = null;
    String pfad="";
    for (Object test: gamesAktuellePlaylist.getItems()) {
      for (Game uebergeben : gamesUebergeben) {
        if (uebergeben.getName().equals(test)) {
          pfad = uebergeben.getPfad();
        }
      }

      try {
        proc = Runtime.getRuntime().exec(
            "java -jar " + pfad + " " + gamesSpielerEins.getSelectionModel().getSelectedItem() + " " + gamesSpielerZwei.getSelectionModel().getSelectedItem());
        proc.waitFor();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  //Darf Playlist gestartet werdern?

  public void playlistAktivieren(){
    if (gamesAktuellePlaylist.getItems().size() != 0
        &&gamesSpielerEins.getSelectionModel().getSelectedItem()!=null
        &&gamesSpielerZwei.getSelectionModel().getSelectedItem()!=null) {
      gamesPlaylistStarten.setDisable(false);
    }else{
      gamesPlaylistStarten.setDisable(true);
    }
  }


  //Button zum löschen der Spiele aus der Playlist

  class ButtonListCell extends ListCell<String> {

    public void updateItem(String obj, boolean empty) {
      super.updateItem(obj, empty);
      if (empty) {
        setText(null);
        setGraphic(null);
      } else {
        setText(obj);

        Button butt = new Button();
        butt.setText("<");
        butt.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            gamesAktuellePlaylist.getItems().remove(obj);
            playlistAktivieren();
          }
        });
        setGraphic(butt);
      }
    }
  }
}

