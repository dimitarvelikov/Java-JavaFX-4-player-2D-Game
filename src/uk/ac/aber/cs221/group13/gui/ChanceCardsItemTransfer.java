package uk.ac.aber.cs221.group13.gui;

import uk.ac.aber.cs221.group13.gameobjects.CrewCard;
import uk.ac.aber.cs221.group13.gameobjects.Island;
import uk.ac.aber.cs221.group13.gameobjects.Player;
import uk.ac.aber.cs221.group13.gameobjects.Port;
import uk.ac.aber.cs221.group13.gameobjects.Position;
import uk.ac.aber.cs221.group13.gameobjects.Treasure;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author dpv
 */
public class ChanceCardsItemTransfer {

   /*this class will handle chance cards 10-17*/
   private VBox scene;
   private Button close;
   private Island i;
   private Port port;
   private Player p;
   private int transferValue;
   private Label title;

   /**
    * Used when the player gets to pick whether they want crew cards or
    * treasures for example.
    *
    * @param pos
    */
   public ChanceCardsItemTransfer(Position pos) {
      title = new Label("");
      close = new Button("Close");
      close.setOnMouseClicked(e -> {
         i = null;
         p = null;
         port = null;
         scene.setVisible(false);
         scene.getChildren().clear();
      });
      scene = new VBox(5);
      scene.setVisible(false);

      scene.setSpacing(20);
      scene.setPadding(new Insets(50, 0, 20, 0));

      scene.setAlignment(Pos.TOP_CENTER);
      scene.setPrefSize(300, 400);
      scene.setManaged(false);

      double w = scene.prefWidth(0);
      double h = scene.prefHeight(w);
      scene.resizeRelocate(220, 180, w, h);
      scene.setStyle("-fx-background-color: #FFFFFF;");

      scene.setBorder(new Border(new BorderStroke(Color.BLACK,
              BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
   }

   /**
    * When treasures are getting transferred of a set value. This function is
    * used when treasures are getting taken from either an island or a port.
    *
    * @param i
    * @param port
    * @param p
    * @param value
    */
   public void showTakeTreasurePane(Island i, Port port, Player p, int value) {

      this.p = p;
      this.i = i;
      this.port = port;

      scene.getChildren().clear();
      scene.setVisible(true);
      transferValue = value;
      ArrayList<Treasure> treasureList;

      if (i != null) {
         treasureList = i.getTreasure();
         title.setText("Select treasure with value up to " + value + ""
                 + "\n       from " + i.getName());
      } else {
         treasureList = port.getTreasure();
         title.setText("Select treasure with value up to " + value + ""
                 + "\n       from " + port.getName());
      }
      ImageView temp = new ImageView();
      temp.setVisible(false);
      Button takeTreasure = new Button("Take treasure");
      takeTreasure.setVisible(false);

      Label selectedTreasure = new Label("Selected treasure");
      selectedTreasure.setVisible(false);
      HBox treasureImages = new HBox(5);

      if (treasureList.size() < 10) {
         treasureImages.setPadding(new Insets(0, 0, 50, 100));
      } else {
         treasureImages.setPadding(new Insets(0, 0, 50, 25));
      }

      for (Treasure t : treasureList) {
         ImageView img = getImageViewByTreasureName(t.getType());
         img.setUserData(t.getType());
         img.setOnMouseClicked(e -> {
            if (temp.getImage() == null) {
               int selectedTreasureValue = getTreasureValue((String) img.getUserData());
               if (transferValue > 1) {

                  if (selectedTreasureValue <= value) {
                     if (transferValue - selectedTreasureValue > -1) {
                        temp.setImage(img.getImage());
                        temp.setUserData(img.getUserData());
                        img.setImage(null);
                        selectedTreasure.setVisible(true);
                        temp.setVisible(true);
                        takeTreasure.setVisible(true);
                        transferValue -= selectedTreasureValue;
                        title.setText("Select treaure with value up to " + transferValue + " \n or close the window !");
                     }
                  } else {
                     Alert alert = new Alert(Alert.AlertType.ERROR);
                     alert.setHeaderText("Error !");
                     alert.setContentText("The selected treasure value is higher than you can take !");
                     alert.showAndWait();
                  }
               } else if (transferValue < 2) {
                  title.setText("Close the window !");
               }

            }
         });
         treasureImages.getChildren().add(img);
      }
      takeTreasure.setOnMouseClicked(e -> {
         takeTreasureFromIsland((String) temp.getUserData());

         selectedTreasure.setVisible(false);
         takeTreasure.setVisible(false);
         temp.setImage(null);
      });

      scene.getChildren().addAll(title, treasureImages, selectedTreasure, temp, takeTreasure, close);

   }

   /**
    * Used to select the necessary image depending on what treasure is needed.
    * So the images are inline with what is going on.
    *
    * @param treasureName
    * @return
    */
   private ImageView getImageViewByTreasureName(String treasureName) {
      ImageView img = null;
      //System.out.println(treasureName);
      switch (treasureName) {
         case "Diamonds":
            img = new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/treasure/diamond.png"));
            break;
         case "Rubies":
            img = new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/treasure/rubies.png"));
            break;
         case "Gold bars":
            img = new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/treasure/gold_bar.png"));
            break;
         case "Pearls":
            img = new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/treasure/pearls.png"));
            break;
         case "Barrels of rum":
            img = new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/treasure//barrels.png"));
            break;
      }
      return img;
   }

   /**
    * Searching for a value of a specific treasure, it will take the name and
    * run that name through a switch statement looking for its value.
    *
    * @param treasureName
    * @return
    */
   public int getTreasureValue(String treasureName) {
      int value = 0; //assign some temp value

      switch (treasureName) {
         case "Diamonds":
            value = 5;
            break;
         case "Rubies":
            value = 5;
            break;
         case "Gold bars":
            value = 4;
            break;
         case "Pearls":
            value = 3;
            break;
         case "Barrels of rum":
            value = 2;
            break;
      }//no default case
      return value;
   }

   /**
    *
    * @return
    */
   public VBox getPane() {
      return scene;
   }

   /**
    * Here you taking the treasure from the island if it can fit on your ship It
    * checks to see if your boat is full, and if not then adds the treasure from
    * the island to the ship
    *
    * @param treasureName
    */
   private void takeTreasureFromIsland(String treasureName) {
      if (p.getShip().getTreasure().size() < 2) {
         Treasure exchange = null;
         if (i != null) {
            for (Treasure t : i.getTreasure()) {
               if (t.getType().equals(treasureName)) {
                  exchange = t;
                  break;
               }
            }
            if (exchange != null) {
               i.getTreasure().remove(exchange);
               p.getShip().getTreasure().add(exchange);
            }
         } else {
            for (Treasure t : port.getTreasure()) {
               if (t.getType().equals(treasureName)) {
                  exchange = t;
                  break;
               }
            }
            if (exchange != null) {
               port.getTreasure().remove(exchange);
               p.getShip().getTreasure().add(exchange);
            }
         }
      } else {
         //alert 
         scene.setVisible(false);
      }
   }

   /**
    * Showing the player that they are making the crew cards from an island or a
    * port. It also displays the images used within the trade.
    *
    * @param p
    * @param i
    * @param port
    * @param numCards
    * @param cardValue
    */
   public void showTakeCrewCardPane(Player p, Island i, Port port, int numCards, int cardValue) {

      this.p = p;
      this.i = i;
      this.port = port;
      ArrayList<CrewCard> crewCardsList = null;
      if (cardValue == 0) {
         transferValue = numCards;

         if (this.i != null) {
            crewCardsList = i.getCrewCards();
            title.setText("Take " + transferValue + " Crew cards \n" + "from " + i.getName());
         } else if (this.port != null) {
            crewCardsList = port.getCrewCards();
            title.setText("Take " + transferValue + " Crew cards \n" + "from " + port.getName());
         }
      } else {
         transferValue = cardValue;
         if (this.i != null) {
            crewCardsList = i.getCrewCards();
            title.setText("Take  crew cards  with total value " + cardValue + "\n" + "from " + i.getName());
         } else if (this.port != null) {
           
            crewCardsList = port.getCrewCards();
            title.setText("Take  crew cards  with total value " + cardValue + "\n" + "from " + port.getName());
         }
      }

      // transferValue = numCards;
      scene.getChildren().clear();

      HBox cardImages = new HBox(5);

      Label selectedCards = new Label("Selected Cards");
      selectedCards.setVisible(false);
      ImageView temp = new ImageView();
      temp.setVisible(false);
      Button takeCard = new Button("Take Crew Card");
      takeCard.setOnMouseClicked((MouseEvent e) -> {

         takeCrewCardFromIsland((CrewCard) temp.getUserData());
         takeCard.setVisible(false);
         temp.setVisible(false);
         selectedCards.setVisible(false);
         if (i != null) {
            if (cardValue > 0) {

               title.setText("Take  crew cards  with total value " + transferValue + "\n" + "from " + i.getName());
            } else {
               if (transferValue == 2) {
                  title.setText("Take " + transferValue + " Crew card \n" + "from " + i.getName());
               } else {
                  title.setText("Take " + transferValue + " Crew cards \n" + "from " + i.getName());
               }

            }

         } else if (port != null) {
            if (cardValue > 0) {
               title.setText("Take  crew cards  with total value " + transferValue + "\n" + "from " + port.getName());
            } else {
               if (transferValue == 2) {
                  title.setText("Take " + transferValue + " Crew card \n" + "from " + port.getName());
               } else {
                  title.setText("Take " + transferValue + " Crew cards \n" + "from " + port.getName());
               }
            }

         }

      });
      takeCard.setVisible(false);
      //  if (!crewCardsList.isEmpty()) {
      for (CrewCard c : crewCardsList) {
         ImageView img = getCrewCardsImageView(c);
         img.setOnMouseClicked(e -> {
            //if temp image is not visible (there is not selected card)
            if (!temp.isVisible()) {
               if (cardValue > 0 && (transferValue - ((CrewCard) img.getUserData()).getValue() > -1)) {
                  temp.setUserData(img.getUserData());
                  temp.setImage(img.getImage());
                  temp.setVisible(true);
                  takeCard.setVisible(true);
                  selectedCards.setVisible(true);
                  img.setVisible(false);
                  transferValue -= ((CrewCard) img.getUserData()).getValue();

               } else if (transferValue > 0 && cardValue == 0) {
                  temp.setUserData(img.getUserData());
                  temp.setImage(img.getImage());
                  temp.setVisible(true);
                  takeCard.setVisible(true);
                  selectedCards.setVisible(true);
                  img.setVisible(false);

                  --transferValue;
               } else if (transferValue < 1) {
                  //alert tell the player he has no cards left to pick up
               }
            }
         });

         cardImages.getChildren().add(img);
      }

      //  }
      scene.getChildren().addAll(title, cardImages, selectedCards, temp, takeCard, close);
      scene.setVisible(true);
   }

   /**
    * Grabbing the image of the specific crew card, so a red or a black one and
    * then whether it is of value 1, 2 or 3.
    *
    * @param c
    * @return
    */
   private ImageView getCrewCardsImageView(CrewCard c) {

      int cardColor, cardVal;

      cardColor = (c.isBlack()) ? 1 : 0;
      cardVal = c.getValue();
      ImageView cardImg = new ImageView();

      switch (cardColor) {
         case 0://red
            switch (cardVal) {
               case 1:
                  cardImg.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/crewcards/red1.png"));
                  cardImg.setUserData(new CrewCard(1, false));
                  break;
               case 2:
                  cardImg.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/crewcards/red2.png"));
                  cardImg.setUserData(new CrewCard(2, false));
                  break;
               case 3:
                  cardImg.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/crewcards/red3.png"));
                  cardImg.setUserData(new CrewCard(3, false));
                  break;
            }
            break;
         case 1://black
            switch (cardVal) {
               case 1:
                  cardImg.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/crewcards/black1.png"));
                  cardImg.setUserData(new CrewCard(1, true));
                  break;
               case 2:
                  cardImg.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/crewcards/black2.png"));
                  cardImg.setUserData(new CrewCard(2, true));
                  break;
               case 3:
                  cardImg.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/crewcards/black3.png"));
                  cardImg.setUserData(new CrewCard(3, true));
                  break;
            }
            break;
      }

      return cardImg;
   }

   /**
    * Taking the crew cards from an island. For example in pirate island when
    * crew cards are given to a player when they get a certain chance card. It
    * checks if there is cards in the island and then will tranfer them to the
    * players hand.
    *
    * @param crewCard
    */
   private void takeCrewCardFromIsland(CrewCard crewCard) {
      CrewCard c = null;
      if (i != null) {
         if (!i.getCrewCards().isEmpty()) {
            for (CrewCard card : i.getCrewCards()) {
               if (crewCard.isBlack() == card.isBlack() && crewCard.getValue() == card.getValue()) {
                  c = card;
                  break;
               }
            }
         }
         i.getCrewCards().remove(c);
         p.getCards().add(c);
      } else {
         if (!port.getCrewCards().isEmpty()) {
            for (CrewCard card : port.getCrewCards()) {
               if (crewCard.isBlack() == card.isBlack() && crewCard.getValue() == card.getValue()) {
                  c = card;
                  break;
               }
            }
         }
         port.getCrewCards().remove(c);
         p.getCards().add(c);
      }
   }

}
