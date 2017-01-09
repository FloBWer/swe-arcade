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
import javafx.stage.Modality;
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
/**<h2>Klasse</h2>
 * <h3>Name: MainScreenController</h3>
 * <p>
 *     Beschriebung: Klasse zur Kontrolle des Hauptfensters, mit den drei Reitern:
 *     <ul>
 *         <li>Games</li>
 *         <li>Statistik</li>
 *         <li>Benutzer</li>
 *     </ul>
 * </p>
 *<p>Erstellt von Daniel und Florian</p>
 */

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
  @FXML
  SplitPane split1;

  private List<Game> gamesUebergeben;
  private List<Game> playList;
  private UserHandler userHandler;
  private StatHandler statHandler;

  /**<h3>Name: initialize</h3>
   * <p>
   *     Initialisiert die Anzeige der Fenster samt verfügbarer Spiele,
   *     Statistiken und User.
   *     Enthält listener um verfügbare Buttons bei Erfüllung aller Bedingungen zu aktivieren.
   * </p>
   *<p>Erstellt von Daniel und Florian</p>
   */
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
        bedingungenSpielstartPruefen();

      }
    });

    gamesSpielerZwei.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        playlistAktivieren();
        bedingungenSpielstartPruefen();

      }
    });

  }

  /**<h3>Name: bedingungenSpielstartPruefen</h3>
   * <p>Beschreibung:Methode überprüft ob ein Spiel gestartet werden darf und aktiviert oder deaktiviert dann
   * den entsprechenden Button und liefert true oder false zurück.
   * Überprüft wird:
   * <ul>
   * <li>Ob ein richtiges Spiel angewählt ist</li>
   * <li>Ob Spieler Eins und Zwei ausgewählt sind</li>
   * <li>Spieler Eins und Spieler Zwei nicht identisch sind</li>
   * </ul>
   * </p>
   *<p>Erstellt von Florian</p>
   */
  boolean bedingungenSpielstartPruefen(){
    if(gamesVerfuegbareSpiele.getSelectionModel().getSelectedItem()!=null&&gamesUebergeben.size()!=0) {
      if( gamesSpielerEins.getSelectionModel().getSelectedItem()!=null&&
              gamesSpielerZwei.getSelectionModel().getSelectedItem()!=null) {
        Object spielerEins=gamesSpielerEins.getSelectionModel().getSelectedItem();
        Object spielerZwei=gamesSpielerZwei.getSelectionModel().getSelectedItem();
        if(!spielerEins.equals(spielerZwei)){
          gamesSpielen.setDisable(false);
          return true;
        }else{
          gamesSpielen.setDisable(true);
        }
      }
    }
    return false;
  }


  private void updateStatsTable() {
    statsTable.getColumns().clear();
    statsTable.getItems().clear();
    final int playerId = 0;
    final int gesWins=1;
    final int gesLoses=2;
    TableColumn<ObservableList<String>, String> playerColumn = new TableColumn<>("Spieler");
    TableColumn<ObservableList<String>, String> gesamtColumn = new TableColumn<>("Gesamt");
    TableColumn<ObservableList<String>, Integer> winGes = new TableColumn<>("Wins");
    TableColumn<ObservableList<String>, Integer> losGes = new TableColumn<>("Loses");
    gesamtColumn.getColumns().add(winGes);
    gesamtColumn.getColumns().add(losGes);
    playerColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(playerId)));
    statsTable.getColumns().add(playerColumn);
    statsTable.getColumns().add(gesamtColumn);
    winGes.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(Integer.parseInt(param.getValue().get(gesWins))));
    losGes.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(Integer.parseInt(param.getValue().get(gesLoses))));
    int columnId = 3;
    for (Game game : gamesUebergeben) {
      final int wId = columnId;
      columnId += 1;
      final int lId = columnId;
      columnId += 1;

      TableColumn<ObservableList<String>, String> gameColumn = new TableColumn<>(game.getName());
      TableColumn<ObservableList<String>, Integer> wCol = new TableColumn<>("Wins");
      TableColumn<ObservableList<String>, Integer> lCol = new TableColumn<>("Loses");

      wCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(Integer.parseInt(param.getValue().get(wId))));
      lCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(Integer.parseInt(param.getValue().get(lId))));
      gameColumn.getColumns().add(wCol);
      gameColumn.getColumns().add(lCol);

      statsTable.getColumns().add(gameColumn);
    }
    // add data
    for(User user : userHandler.getUsers()) {
      statsTable.getItems().addAll(FXCollections.observableList(statHandler.getUserRow(user.getName())));
    }
    winGes.setSortType(TableColumn.SortType.DESCENDING);
    statsTable.getSortOrder().add(winGes);

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

  /**
   * ClickEvent für den Button Benutzer Erstellen
   * @param event
   */
  @FXML
  public void clickBenutzerErstellen(ActionEvent event) {
    User newUser = userHandler.newUser(benutzerTextBoxAnlegen.getText());
    if(newUser != null) {
      addUserToComboBoxes(newUser);
      statHandler.addUser(newUser.getName());
      updateStatsTable();
      ConfigFileReader.saveStats(statHandler);
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
      stage.setMaxHeight(173);
      stage.setMinHeight(173);
      stage.setMaxWidth(288);
      stage.setMinWidth(288);
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

  /**
   * Click Event für den Button Benutzer löschen
   * @param event
   */
  @FXML
  public void clickBenutzerLoeschen(ActionEvent event) {
    String name = benutzerAuswahlZuAendern.getSelectionModel().getSelectedItem().toString();
    userHandler.removeUser(name);
    removeUserFromComboBoxes(name);
    statHandler.removeUser(name);
    updateStatsTable();
    ConfigFileReader.saveStats(statHandler);
  }

  /**
   * Click Event für den Button Benutzer Speichern
   * @param event
   */
  @FXML
  public void clickBenutzerAendernSpeichern(ActionEvent event) {
    String name = benutzerAuswahlZuAendern.getSelectionModel().getSelectedItem().toString();
    String newName = benutzerTextBoxAendern.getText();
    User newUser = userHandler.changeUser(name, newName);
    if(newUser != null) {
      removeUserFromComboBoxes(name);
      addUserToComboBoxes(newUser);
      statHandler.renameUser(name, newName);
      updateStatsTable();
      ConfigFileReader.saveStats(statHandler);
    }
    else {
      showBenutzerBereitsVorhanden();
    }
  }

  /**<h3>Name: clickZurPlaylistHinzufuegen</h3>
   * <p>
   *     Beschreibung: Bei Klick auf Button "zur Playlist hinzufügen" wird das angewählte Spiel zur Playlist
   *     hinzugefügt und in der Anzeige "Playlist" ein Eintrag mit Button zum Löschen hinzugefügt (CellFactory).
   * </p>
   *<p>Erstellt von Florian</p>
   */
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

  /**<h3>Name: clickGamesSpielen</h3>
   * <p>
   *     Berschreibung: Startet Spiel über startGame Methode
   * </p>
   *<p>Erstellt von Florian</p>
   */
  @FXML
  public void clickGamesSpielen(ActionEvent event) {

    startGame();
  }

  /**<h3>Name: startGame</h3>
   * <p>
   *     Beschreibung: Öffnet die jar des markierten Spiels, nach beendigung werden die Statistiken aktuallisiert und eine
   *     Revancheabfrage angezeigt.
   * </p>
   *<p>Erstellt von Florian und Daniel</p>
   */
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
      if (gameReturn != null) {
        gameReturn.setGame(selected);
        statHandler.updateStats(gameReturn);
        updateStatsTable();
        ConfigFileReader.saveStats(statHandler);
        showRevanche();
      }
      } catch(Exception e){
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
      newStage.getIcons().add(new Image(getClass().getResourceAsStream("images/pong.png")));
      newStage.setTitle("Revanche?");
      newStage.setScene(newScene);
      newStage.setMaxWidth(270);
      newStage.setMinWidth(270);
      newStage.setMaxHeight(172);
      newStage.setMinHeight(172);
      newStage.initModality(Modality.APPLICATION_MODAL);
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

  /**<h3>Name: clickPlaylistStarten</h3>
   * <p>
   *     Startet Playlist über startPlaylist Methode beim Klick auf
   *     Playlist starten Button
   * </p>
   *<p>Erstellt von Florian</p>
   */
  @FXML
  public void clickPlaylistStarten(ActionEvent event) {
    startPlaylist();
  }

  /**
   * Versteckt die Instanz des Hauptfensters
   */
  public void hide() {
    Stage stage = (Stage) gamesPlaylistStarten.getScene().getWindow();
    stage.hide();
  }

  /**
   * Zeigt die Instanz des Hauptfensters
   */
  public void show() {
    Stage stage = (Stage) gamesPlaylistStarten.getScene().getWindow();
    stage.show();
  }

  /**<h3>Name: startPlaylist</h3>
   * <p>
   *     Beschreibung: Startet hintereinander die in der Playlist hinterlegten Spiele und Speichert die Ergebnisse
   *     in den Statistiken, nach Abschluss wird ein Fenster mit einer Revancheanfrage angezeigt.
   * </p>
   *<p>Erstellt von Daniel und Florian</p>
   */
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
        if(gameReturn!=null) {
            gameReturn.setGame((String) test);
            returnList.add(gameReturn);
        }
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
      newStage.getIcons().add(new Image(getClass().getResourceAsStream("images/pong.png")));
      newStage.setTitle("Playlist beendet");
      newStage.setMaxWidth(444);
      newStage.setMinWidth(444);
      newStage.setMaxHeight(308);
      newStage.setMinHeight(308);
      newStage.requestFocus();
      newStage.initModality(Modality.APPLICATION_MODAL);
      newStage.showAndWait();
      if(ctrl.getRevanche()) {
        startPlaylist();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**<h3>Name: playlistAktivieren</h3>
   * <p>
   *     Beschreibung: Überprüft ob der Playlist starten Button aktiviert werden darf
   * </p>
   *<p>Erstellt von Florian</p>
   */
  public void playlistAktivieren(){
    if (gamesAktuellePlaylist.getItems().size() != 0
        &&bedingungenSpielstartPruefen()) {
      gamesPlaylistStarten.setDisable(false);
    }else{
      gamesPlaylistStarten.setDisable(true);
    }
  }


  /**<h2>Klasse</h2>
   * <h3>Name: ButtonListCell</h3>
   * <p>
   *     Beschreibung: Unterklasse mit nur einer Methode, Button zum Löschen wird in den
   *     Elementen der Playlist hinzugefügt.
   * </p>
   *<p>Erstellt von Florian</p>
   */
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

