package windows;

import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import objects.Game;


public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
    primaryStage.setTitle("ArcadeBox");
    primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("images/pong.png")));
    primaryStage.setScene(new Scene(root, 600, 400));
    primaryStage.show();
  }

  public static class HBoxCell extends HBox {
    Label label = new Label();
    Button button = new Button();

    HBoxCell(String labelText, String buttonText) {
      super();

      label.setText(labelText);
      label.setMaxWidth(Double.MAX_VALUE);
      HBox.setHgrow(label, Priority.ALWAYS);

      button.setText(buttonText);

      this.getChildren().addAll(label, button);
    }
  }


  public static void main(String[] args) {

    launch(args);
  }


}
