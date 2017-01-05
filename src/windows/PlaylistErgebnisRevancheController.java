package windows;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import objects.Game;
import objects.GameReturn;
import objects.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**<h2>Klasse</h2>
 * <h3>Name: PlaylistErgebnisRevancheController</h3>
 * <p>
 *     Beschreibung: Controller für Fenster welches Ergebnis der Playlist anzeigt
 *     und die Möglichkeit bietet eine Revanche der Playlist zu spielen.
 * </p>
 *<p>Erstellt von Daniel und Florian</p>
 */
public class PlaylistErgebnisRevancheController {
  @FXML
  Button playlistRevancheJa;
  @FXML
  Button playlistRevancheNein;
  @FXML
  TableView<ObservableList<String>> tabelleErgebnis;
  private HashMap<String, Integer> gameIds;
  private boolean revanche;
  private Stage parent;

  public void initialize(List<GameReturn> returnList, String player1, String player2, Stage parent) {
    this.parent = parent;
    revanche = false;
    gameIds = new HashMap<>();
    HashMap<String, Integer> gameIds = new HashMap<>();
    List<String> p1 = new ArrayList<>();
    List<String> p2 = new ArrayList<>();
    p1.add(player1);
    p2.add(player2);
    tabelleErgebnis.getColumns().clear();
    tabelleErgebnis.getItems().clear();
    final int playerId = 0;
    TableColumn<ObservableList<String>, String> playerColumn = new TableColumn<>("Spieler");
    //Test für Gesamt
    int winPEins=0;
    int winPZwei=0;
    int losPEins=0;
    int losPZwei=0;
    p1.add("");
    p1.add("");
    p2.add("");
    p2.add("");
    TableColumn<ObservableList<String>, String> gesamtColumn = new TableColumn<>("Gesamt");
    TableColumn<ObservableList<String>, String> winGes = new TableColumn<>("Wins");
    TableColumn<ObservableList<String>, String> losGes = new TableColumn<>("Loses");
    gesamtColumn.getColumns().add(winGes);
    gesamtColumn.getColumns().add(losGes);
    //

    playerColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(playerId)));
    tabelleErgebnis.getColumns().add(playerColumn);
    //Test für Gesamt
    tabelleErgebnis.getColumns().add(gesamtColumn);

    int gameId = 1;
    int columnId = 1;
    final int wgesId=columnId;
    columnId++;
    final int lgesId=columnId;
    columnId++;
    winGes.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(wgesId)));
    //
    for (GameReturn returnEntry : returnList) {
      TableColumn<ObservableList<String>, String> gameColumn = getColumn(returnEntry.getGame());
      if(gameColumn == null) {
        gameIds.put(returnEntry.getGame(), columnId);
        final int wId = columnId;
        columnId += 1;
        final int lId = columnId;
        columnId += 1;

        gameColumn = new TableColumn<>(returnEntry.getGame());
        TableColumn<ObservableList<String>, String> wCol = new TableColumn<>("Wins");
        TableColumn<ObservableList<String>, String> lCol = new TableColumn<>("Loses");

        wCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(wId)));
        lCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(lId)));
        gameColumn.getColumns().add(wCol);
        gameColumn.getColumns().add(lCol);

        tabelleErgebnis.getColumns().add(gameColumn);
        p1.add("0"); // wins
        p1.add("0"); // loses
        p2.add("0");
        p2.add("0");
      }

      int wId = getWinnerColumnId(returnEntry.getGame(), gameIds);
      int lId = getLoserColumnId(returnEntry.getGame(), gameIds);
      if(returnEntry.getWinner().equals(p1.get(0))) {
        p1.set(wId, String.valueOf(Integer.parseInt(p1.get(wId)) + 1));
        winPEins+=1;
        p2.set(lId, String.valueOf(Integer.parseInt(p2.get(lId)) + 1));
        losPZwei+=1;
      }
      else {
        p1.set(lId, String.valueOf(Integer.parseInt(p1.get(lId)) + 1));
        winPZwei+=1;
        p2.set(wId, String.valueOf(Integer.parseInt(p2.get(wId)) + 1));
        losPEins+=1;
      }

    }
    tabelleErgebnis.getItems().addAll(FXCollections.observableList(p1));
    tabelleErgebnis.getItems().addAll(FXCollections.observableList(p2));
    p1.set(1,""+winPEins);
    p1.set(2,""+losPEins);
    p2.set(1,""+winPZwei);
    p2.set(2,""+losPZwei);

    winGes.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(wgesId)));
    losGes.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(lgesId)));


  }

  @FXML
  public void clickPlaylistRevancheJa(ActionEvent event) {
    revanche = true;
    Stage stage = (Stage) playlistRevancheJa.getScene().getWindow();
    stage.close();
  }

  @FXML
  public void clickPlaylistRevancheNein(ActionEvent event) {
    revanche = false;
    Stage stage = (Stage) playlistRevancheNein.getScene().getWindow();
    stage.close();
    parent.show();
  }

  private TableColumn getColumn(String game) {
    for(Object o : tabelleErgebnis.getColumns()) {
      TableColumn<ObservableList<String>, String> column = (TableColumn)o;
      if(column.getText().equals(game)) {
        return column;
      }
    }
    return null;
  }

  private int getWinnerColumnId(String game, HashMap<String, Integer> gameIds) {
    System.out.println(gameIds.toString());
    return gameIds.get(game);

  }

  private int getLoserColumnId(String game, HashMap<String, Integer> gameIds) {
    return gameIds.get(game) + 1;
  }

  public boolean getRevanche() {
    return revanche;
  }
}

