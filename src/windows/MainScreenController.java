package windows;

import com.google.gson.Gson;
import handler.StatHandler;
import handler.UserHandler;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import objects.GameReturn;
import objects.User;
import utils.ConfigFileReader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
  TableView<ObservableList<String>> statsTable;

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

  private void updateStatsTable() {
    statsTable.getColumns().clear();
    statsTable.getItems().clear();
    final int playerId = 0;
    TableColumn<ObservableList<String>, String> playerColumn = new TableColumn<>("Spieler");
    playerColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(playerId)));
    statsTable.getColumns().add(playerColumn);
    int columnId = 1;
    for (Game game : gamesUebergeben) {
      final int wId = columnId;
      columnId += 1;
      final int lId = columnId;
      columnId += 1;

      TableColumn<ObservableList<String>, String> gameColumn = new TableColumn<>(game.getName());
      TableColumn<ObservableList<String>, String> wCol = new TableColumn<>("Wins");
      TableColumn<ObservableList<String>, String> lCol = new TableColumn<>("Loses");

      wCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(wId)));
      lCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(lId)));
      gameColumn.getColumns().add(wCol);
      gameColumn.getColumns().add(lCol);

      statsTable.getColumns().add(gameColumn);
    }
    // add data
    for(User user : userHandler.getUsers()) {
      statsTable.getItems().addAll(FXCollections.observableList(statHandler.getUserRow(user.getName())));
    }
  }

  private void initStats() {
    statHandler = ConfigFileReader.buildStatsHandler(gamesUebergeben, userHandler);
    updateStatsTable();
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
    hide();
    startGame();
  }

  private void startGame() {
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
              "java -jar \"" + pfad + "\" " +
                      gamesSpielerEins.getSelectionModel().getSelectedItem() + " " +
                      gamesSpielerZwei.getSelectionModel().getSelectedItem());

      GameReturn gameReturn = handleReturn(proc.getInputStream());
      gameReturn.setGame(selected);
      statHandler.updateStats(gameReturn);
      updateStatsTable();
      ConfigFileReader.saveStats(statHandler);
      showRevanche();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void showRevanche() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(
              "revanche.fxml"));
      Parent root = (Parent) loader.load();
      RevancheController ctrl = loader.getController();
      ctrl.initialize((Stage) gamesPlaylistStarten.getScene().getWindow());
      Scene newScene = new Scene(root);
      Stage newStage = new Stage();
      newStage.setScene(newScene);
      newStage.requestFocus();
      newStage.showAndWait();

      if(ctrl.getRevanche()) {
        startGame();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private GameReturn handleReturn(InputStream inputStream) throws Exception{
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    String returnString = reader.readLine();

    Gson g = new Gson();
    return g.fromJson(returnString, GameReturn.class);
  }

  @FXML
  public void clickPlaylistStarten(ActionEvent event) {
    hide();
    startPlaylist();
  }

  public void hide() {
    Stage stage = (Stage) gamesPlaylistStarten.getScene().getWindow();
    stage.hide();
  }

  public void show() {
    Stage stage = (Stage) gamesPlaylistStarten.getScene().getWindow();
    stage.show();
  }

  private void startPlaylist() {
    Process proc = null;
    String pfad="";
    List<GameReturn> returnList = new ArrayList<>();
    for (Object test: gamesAktuellePlaylist.getItems()) {
      for (Game uebergeben : gamesUebergeben) {
        if (uebergeben.getName().equals(test)) {
          pfad = uebergeben.getPfad();
        }
      }

      try {
        proc = Runtime.getRuntime().exec(
                "java -jar \"" + pfad + "\" " + gamesSpielerEins.getSelectionModel().getSelectedItem() + " " + gamesSpielerZwei.getSelectionModel().getSelectedItem());
        proc.waitFor();

        GameReturn gameReturn = handleReturn(proc.getInputStream());
        gameReturn.setGame((String)test);
        returnList.add(gameReturn);

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    statHandler.updateStats(returnList);
    updateStatsTable();
    ConfigFileReader.saveStats(statHandler);
    showPlaylistRevanche(returnList);
  }

  private void showPlaylistRevanche(List<GameReturn> returnList) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(
              "playlistErgebnisRevanche.fxml"));
      Parent root = (Parent) loader.load();
      PlaylistErgebnisRevancheController ctrl = loader.getController();
      ctrl.initialize(returnList,
              gamesSpielerEins.getSelectionModel().getSelectedItem().toString(),
              gamesSpielerZwei.getSelectionModel().getSelectedItem().toString(),
              (Stage) gamesPlaylistStarten.getScene().getWindow());

      Scene newScene = new Scene(root);
      Stage newStage = new Stage();
      newStage.setScene(newScene);
      newStage.requestFocus();
      newStage.showAndWait();
      if(ctrl.getRevanche()) {
        startPlaylist();
      }
    } catch (Exception e) {
      e.printStackTrace();
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

