package uk.ac.aber.cs221.group13.gui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author dpv
 * @author mol12
 * @author caf22 version 0.7
 */
public class MainMenu extends Application {

   /*has to be static because the variables are used from the event handler*/
   private final static int MAX_PLAYERS = 4;
   private String[] playerNames = new String[MAX_PLAYERS];
   private int playerCounter = 0;

   //easier swithing between scenes
   private static Scene previousScene;

   @Override
   public void start(Stage primaryStage) {

    //Sets scenes, and collects the CSS Theme for the game.
    //This thus then sets up labels and layout for the game.
      Scene mainScene, creditsScene;
      creditsScene = getCreditsScene(primaryStage);

      mainScene = getMainScene(primaryStage, creditsScene);
      mainScene.getStylesheets().add("uk/ac/aber/cs221/group13/externalfiles/BuccaneerTheme.css");

      creditsScene.getStylesheets().add("uk/ac/aber/cs221/group13/externalfiles/BuccaneerTheme.css");

      primaryStage.setTitle("Buccaneer Online Board Game");
      primaryStage.setScene(mainScene);

      primaryStage.setResizable(false);
      primaryStage.centerOnScreen();

      primaryStage.show();
   }

   private Scene getMainScene(Stage primaryStage, Scene creditsScene) {

      //Setup Menu Scene.
      final Label gameName = new Label("Buccaneer Online Board Game");
      Blend blend = new Blend();
blend.setMode(BlendMode.MULTIPLY);

DropShadow ds = new DropShadow();
ds.setColor(Color.rgb(254, 235, 66, 0.3));
ds.setOffsetX(5);
ds.setOffsetY(5);
ds.setRadius(5);
ds.setSpread(0.2);

blend.setBottomInput(ds);
//Ignores the CSS. Despite this being in the CSS. 
DropShadow ds1 = new DropShadow();
ds1.setColor(Color.web("#25529e"));
ds1.setRadius(20);
ds1.setSpread(0.2);

Blend blend2 = new Blend();
blend2.setMode(BlendMode.MULTIPLY);

InnerShadow is = new InnerShadow();
is.setColor(Color.web("#feeb42"));
is.setRadius(9);
is.setChoke(0.8);
blend2.setBottomInput(is);

InnerShadow is1 = new InnerShadow();
is1.setColor(Color.web("#259e5f"));
is1.setRadius(5);
is1.setChoke(0.4);
blend2.setTopInput(is1);

Blend blend1 = new Blend();
blend1.setMode(BlendMode.MULTIPLY);
blend1.setBottomInput(ds1);
blend1.setTopInput(blend2);

blend.setTopInput(blend1);

      gameName.setEffect(blend);
      
      
      gameName.setId("title");
      final Label displayInfo = new Label();
      StringProperty emptyStringProperty = new SimpleStringProperty("");
      displayInfo.textProperty().bind(emptyStringProperty);

      //Declare button "Start Game"
      final Button startGame = new Button("Start Game");
      //Define action when "Start Game" is clicked
      startGame.setOnAction(e -> {
         for (int x = 0; x < playerNames.length; ++x) {
            if (playerNames[x] == null) {
               switch (x) {
                  case 0:
                     playerNames[0] = "Jack Sparrow";
                     break;
                  case 1:
                     playerNames[1] = "Black Beard";
                     break;
                  case 2:
                     playerNames[2] = "Ferdinand Magellan";
                     break;
                  case 3:
                     playerNames[3] = "Spiderman";
                     break;
               }
            }
         }
         GameLayout game = null;
         try {
            game = new GameLayout(playerNames);
         } catch (IOException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
         }
         Scene gameScene = new Scene(game.getBorderPane());//, 1500, 1000);
         primaryStage.setScene(gameScene);
         primaryStage.show();
         primaryStage.centerOnScreen();
      });
      // startGame.setDisable(true);

      //Declare text fields and buttons for entering player names and place them into GridPane grid.
      TextField[] enterPlNames = new TextField[MAX_PLAYERS];
      Button[] submitPlNames = new Button[MAX_PLAYERS];

      GridPane grid = new GridPane();
      grid.setPadding(new Insets(0, 0, 0, 0));//left padding
      grid.setHgap(20.0);
      grid.setVgap(20.0);
      grid.setAlignment(Pos.CENTER);

      for (int x = 0; x < MAX_PLAYERS; ++x) {
         enterPlNames[x] = new TextField();
         enterPlNames[x].setMinWidth(250.0);
         enterPlNames[x].setAlignment(Pos.CENTER);
         submitPlNames[x] = new Button("Apply");
         enterPlNames[x].setPromptText("Player " + (x + 1));

         submitPlNames[x].setUserData(x);
         btnSetOnAction(submitPlNames[x], enterPlNames[x], displayInfo, emptyStringProperty, startGame);

         grid.add(enterPlNames[x], 0, x);
         grid.add(submitPlNames[x], 1, x);
      }

      //Declare button exit
      final Button exit = new Button("Exit");
      exit.getStyleClass().add("exit");
      exit.setOnAction(e -> Platform.exit());

      HBox buttons = new HBox(40);
      buttons.setAlignment(Pos.CENTER);
      buttons.setPrefWidth(100);
      exit.setMinWidth(buttons.getPrefWidth());
      startGame.setMinWidth(buttons.getPrefWidth());

      buttons.getChildren().addAll(exit, creditsSceneBtn(primaryStage, creditsScene), startGame);

      VBox pane = new VBox(50);
      pane.setAlignment(Pos.TOP_CENTER);
      //WILL BE IN CSS FILE\/\/\/
      pane.setStyle("-fx-padding: 20 0 0 0;");
      pane.getChildren().addAll(
              gameName, grid, buttons);

      return new Scene(pane, 600, 500);
   }

    //acts as a getter and placed the credits information onto the project.
   private Scene getCreditsScene(Stage primaryStage) {//, Scene main) {

      final Label namesList = new Label("Project Members: \n\n"
              + "Brooks,      Jonathan\n"
              + "Fearn,	Tomos\n"
              + "Fillingham-Bathie,	Carrick\n"
              + "Francis,	Joe	Alexander\n"
              + "Lang,	Morgan	Elis\n"
              + "Owen,	Robert	Henry\n"
              + "Taylor,	George\n"
              + "Velikov,	Dimitar	Plamenov\n"
      );
        //Collects the button
      final Button back = new Button("Back");
      back.setOnAction((ActionEvent e) -> {
         primaryStage.setScene(previousScene);
         primaryStage.show();
      });

      VBox x = new VBox(50);
      x.setAlignment(Pos.CENTER);
      x.getChildren().addAll(namesList, back);
      return new Scene(x, 600, 500);
   }

    //Allows the code to display an error message for the uncompatible speical characters
   private boolean textFieldContainsChar(String text) {
      boolean hasCorruptedChar = false;
      if (text.contains("*") || text.contains("/") || text.contains(".") || text.contains("@") || text.contains("!")
              || text.contains("#") || text.contains("%") || text.contains("^") || text.contains("&") || text.contains("_")) {
         hasCorruptedChar = true;
      }
      return hasCorruptedChar;
   }

    //Displays error message if the players username is already used. Compares string to ensure it's non exitent.
   private boolean nameIsAlreadyUsed(String text) {
      boolean isUsed = false;
      for (int x = 0; x < playerCounter; ++x) {
         if (playerNames[x] != null) {
            if (playerNames[x].equals(text)) {
               isUsed = true;
            }
         }
      }
      return isUsed;
   }

    //Provides the credits scene button for the users to move to the credits.
   private Button creditsSceneBtn(Stage primaryStage, Scene creditsScene) {

      final Button credits = new Button("Credits");
      credits.setPrefWidth(100);
      credits.setId("#credits");
      credits.setOnAction(e -> {
         previousScene = primaryStage.getScene();
         primaryStage.setScene(creditsScene);
         primaryStage.show();
      });
      return credits;
   }

    //Allows the user to apply their name, and will collect the errors and messages corresponding if the code detects them.
   private void btnSetOnAction(Button btn, TextField textField, Label displayInfo, StringProperty emptyStringProperty, Button startGame) {
      btn.setOnAction(e -> {
         Alert alert = new Alert(Alert.AlertType.ERROR);
         alert.setTitle("ERROR");
         alert.setContentText("Please enter another username !");
         if (btn.getText().equals("Apply")) {
            if (playerCounter < MAX_PLAYERS && !textField.getText().isEmpty()) {//is empty is better than equals("")
               if (textField.getText().length() > 16) {
                  alert.setHeaderText("The username is too long!");
                  alert.showAndWait();
                  textField.clear();
               } else if (textFieldContainsChar(textField.getText())) {
                  alert.setHeaderText("The username contains unrecognized character !");
                  alert.showAndWait();
                  textField.clear();
               } else if (nameIsAlreadyUsed(textField.getText())) {
                  alert.setHeaderText("The username is already used !");
                  alert.showAndWait();
                  textField.clear();
               } else {
                  int numPlayer = (Integer) btn.getUserData();
                  if (playerNames[numPlayer] == null) {
                     ++playerCounter;
                  }
                  textField.setDisable(true);
                  playerNames[numPlayer] = textField.getText();
                  btn.setText("Edit");
               }
            }
            if (playerCounter > 3) {
               displayInfo.textProperty().bind(emptyStringProperty.concat("Start the game !"));
               startGame.setDisable(false);
            }

         } else {
            btn.setText("Apply");
            textField.setDisable(false);
         }
      });
   }
}
