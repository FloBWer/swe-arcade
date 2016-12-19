package windows;

import handler.StatHandler;
import handler.UserHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;
import objects.Game;
import objects.User;
import utils.ConfigFileReader;

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
  ComboBox benutzerAuswahlZuAendern;
  @FXML
  Button benutzerLoeschen;
  @FXML
  TextField benutzerTextBoxAendern;
  @FXML
  Button benutzerAendernSpeichern;
  @FXML
  TreeTableView statsTable;

  private List<Game> gamesUebergeben;
  private List<Game> playList;
  private UserHandler userHandler;
  private StatHandler statHandler;

  public void initialize() {
    initUsers();
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
    initStats();

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

    //Listener für selektierte Spieler

    gamesSpielerEins.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        playlistAktivieren();
        if(gamesVerfuegbareSpiele.getSelectionModel().getSelectedItem()!=null&&gamesUebergeben.size()!=0&&
            gamesSpielerZwei.getSelectionModel().getSelectedItem()!=null){
          gamesSpielen.setDisable(false);
        }

      }
    });

    gamesSpielerZwei.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        playlistAktivieren();
        if(gamesVerfuegbareSpiele.getSelectionModel().getSelectedItem()!=null&&gamesUebergeben.size()!=0&&
            gamesSpielerEins.getSelectionModel().getSelectedItem()!=null) {
          gamesSpielen.setDisable(false);
        }

      }
    });

  }

  private void initStats() {
    statHandler = ConfigFileReader.buildStatsHandler(gamesUebergeben, userHandler);
    statHandler.updateStats();


  }

  private void initUsers() {
    userHandler = ConfigFileReader.buildUserHandler();
    userHandler.getUsers().forEach(user -> {
      addUserToComboBoxes(user);
    });
  }


  @FXML
  public void clickBenutzerErstellen(ActionEvent event) {
    User newUser = userHandler.newUser(benutzerTextBoxAnlegen.getText());
    if(newUser != null) {
      addUserToComboBoxes(newUser);
    }
    else {
      showBenutzerBereitsVorhanden();
    }
  }

  private void showBenutzerBereitsVorhanden() {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("benutzerBereitsVorhanden.fxml"));
      Stage stage = new Stage();
      stage.setTitle("Fehler!");
      stage.getIcons().add(new Image(getClass().getResourceAsStream("images/pong.png")));
      stage.setScene(new Scene(root));
      stage.setAlwaysOnTop(true);
      stage.requestFocus();
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void addUserToComboBoxes(User newUser) {
    benutzerAuswahlZuAendern.getItems().add(newUser.getName());
    gamesSpielerEins.getItems().add(newUser.getName());
    gamesSpielerZwei.getItems().add(newUser.getName());
    benutzerTextBoxAnlegen.clear();
    benutzerTextBoxAendern.clear();
  }

  private void removeUserFromComboBoxes(String name) {
    benutzerAuswahlZuAendern.getItems().remove(name);
    gamesSpielerEins.getItems().remove(name);
    gamesSpielerZwei.getItems().remove(name);
  }

  @FXML
  public void clickBenutzerLoeschen(ActionEvent event) {
    String name = benutzerAuswahlZuAendern.getSelectionModel().getSelectedItem().toString();
    userHandler.removeUser(name);
    removeUserFromComboBoxes(name);
  }

  @FXML
  public void clickBenutzerAendernSpeichern(ActionEvent event) {
    String name = benutzerAuswahlZuAendern.getSelectionModel().getSelectedItem().toString();
    String newName = benutzerTextBoxAendern.getText();
    User newUser = userHandler.changeUser(name, newName);
    if(newUser != null) {
      removeUserFromComboBoxes(name);
      addUserToComboBoxes(newUser);
    }
    else {
      showBenutzerBereitsVorhanden();
    }
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
          "java -jar " + pfad + " " +
              gamesSpielerEins.getSelectionModel().getSelectedItem() + " " +
              gamesSpielerZwei.getSelectionModel().getSelectedItem());
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

