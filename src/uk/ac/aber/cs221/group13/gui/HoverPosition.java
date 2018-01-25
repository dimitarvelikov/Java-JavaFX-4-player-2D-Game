package uk.ac.aber.cs221.group13.gui;

import uk.ac.aber.cs221.group13.gameobjects.CrewCard;
import uk.ac.aber.cs221.group13.gameobjects.Island;
import uk.ac.aber.cs221.group13.gameobjects.Port;
import uk.ac.aber.cs221.group13.gameobjects.Treasure;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Created by dpv on 02-Mar-17.
 */
public final class HoverPosition {

   private static final VBox container = new VBox(20);//20 is line spacing
   private final Label name = new Label();
   private final GridPane gridP = new GridPane();
   private final Label[] amountTreasure = new Label[5];
   private final HashMap<String, Integer> treasureCounter = new HashMap();
   private final HBox crewCards;

   public HoverPosition() {
      crewCards = new HBox();
      container.setAlignment(Pos.CENTER);
      gridP.setHgap(10.0);
      gridP.setVgap(10.0);
      Button hide = new Button("Hide");
      hide.setOnMouseClicked(event -> container.setVisible(false));

      container.getChildren().addAll(name, gridP, crewCards, hide);
      container.setOnMouseExited(e -> container.setVisible(false));
      container.setBorder(new Border(new BorderStroke(Color.BLACK,
              BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
      container.setStyle("-fx-background-color: #FFFFFF;");
      container.setPadding(new Insets(30.0, 10.0, 20.0, 10.0));
      setupHoverPaneData();
      DropShadow borderGlow = new DropShadow();
      borderGlow.setOffsetY(0f);
      borderGlow.setOffsetX(0f);
      borderGlow.setColor(Color.RED);
      borderGlow.setWidth(50);
      borderGlow.setHeight(50);
      container.setEffect(borderGlow);
   }

   //returns the main container to the game layout class
   protected VBox getHover() {
      return container;
   }

   private void setupHoverPaneData() {
      for (int x = 0; x < 10; ++x) {
         if (x % 2 != 0) {
            amountTreasure[x / 2] = new Label("");
            gridP.add(amountTreasure[x / 2], x, 0);
         } else {
            ImageView imageView = new ImageView();
            switch (x) {
               case 0:
                  imageView.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/treasure/diamond.png"));
                  break;
               case 2:
                  imageView.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/treasure/rubies.png"));
                  break;
               case 4:
                  imageView.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/treasure/gold_bar.png"));
                  break;
               case 6:
                  imageView.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/treasure/pearls.png"));
                  break;
               case 8:
                  imageView.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/treasure/barrels.png"));
                  break;
            }
            imageView.setStyle("-fx-border-width:1px solid; -fx-border-color:red");
            gridP.add(imageView, x, 0);
         }
      }
   }

   protected void updateGrid(Island i, Port t) {
      crewCards.getChildren().clear();
      if (i instanceof Island) {
         name.setText(i.getName());
         updateLabels(i.getTreasure());
         if (!i.getName().equals("Pirate Island")) {
            updateCrewCards(i.getCrewCards(), false);
         } else {
            updateCrewCards(i.getCrewCards(), true);
         }
      } else {
         name.setText(t.getName());
         updateLabels(t.getTreasure());
         updateCrewCards(t.getCrewCards(), false);
      }
   }

   //to prevent duplication of code in updateGrid()
   private void updateLabels(ArrayList<Treasure> t) {
      int numTreasure[] = {0, 0, 0, 0, 0};
      if (!t.isEmpty()) {
         for (Treasure tr : t) {
            switch (tr.getType()) {
               case "Diamonds":
                  numTreasure[0] += 1;
                  //   value = 5;
                  break;
               case "Rubies":
                  numTreasure[1] += 1;
                  // value = 5;
                  break;
               case "Gold bars":
                  numTreasure[2] += 1;
                  //value = 4;
                  break;
               case "Pearls":
                  numTreasure[3] += 1;
                  //value = 4;
                  break;
               case "Barrels of rum":
                  numTreasure[4] += 1;
                  //value = 2;
                  break;
            }
         }
         for (int x = 0; x < numTreasure.length; ++x) {
            amountTreasure[x].setText(Integer.toString(numTreasure[x]));
         }
      } else {
         for (int x = 0; x < numTreasure.length; ++x) {
            amountTreasure[x].setText("0");
         }
      }
   }

   private void updateCrewCards(ArrayList<CrewCard> cards, boolean isPirateIsland) {
      int cardColor;
      for (CrewCard c : cards) {
         cardColor = (c.isBlack()) ? 1 : 0;
         Image cardImg = null;
         if (!isPirateIsland) {
            switch (cardColor) {
               case 0://red
                  switch (c.getValue()) {
                     case 1:
                        cardImg = new Image("uk/ac/aber/cs221/group13/externalfiles/images/crewcards/red1.png");
                        break;
                     case 2:
                        cardImg = new Image("uk/ac/aber/cs221/group13/externalfiles/images/crewcards/red2.png");
                        break;
                     case 3:
                        cardImg = new Image("uk/ac/aber/cs221/group13/externalfiles/images/crewcards/red3.png");
                        break;
                  }
                  break;
               case 1://black
                  switch (c.getValue()) {
                     case 1:
                        cardImg = new Image("uk/ac/aber/cs221/group13/externalfiles/images/crewcards/black1.png");
                        break;
                     case 2:
                        cardImg = new Image("uk/ac/aber/cs221/group13/externalfiles/images/crewcards/black2.png");
                        break;
                     case 3:
                        cardImg = new Image("uk/ac/aber/cs221/group13/externalfiles/images/crewcards/black3.png");
                        break;
                  }
                  break;
            }
         } else {
            switch (cardColor) {
               case 0:
                  cardImg = new Image("uk/ac/aber/cs221/group13/externalfiles/images/crewcards/red_faceDown.png");
                  break;
               case 1:
                  cardImg = new Image("uk/ac/aber/cs221/group13/externalfiles/images/crewcards/black_faceDown.png");
                  break;

            }
         }
         crewCards.getChildren().add(new ImageView(cardImg));
      }
   }
}
