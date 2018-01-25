package uk.ac.aber.cs221.group13.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This class is for the game rules page
 *
 * @author dpv and get7
 */
public class GameRules {

   private static VBox main;
   private final Button close;
   private TextArea text;
   private final Label header;
   private final ComboBox userSelection;

   /**
    * Creating the dimensions and detail of how the rules menu is going to look
    */
   public GameRules() {
      header = new Label("");
      header.setFont(new Font(26));
      text = new TextArea();
      userSelection = new ComboBox();
      text.setWrapText(true);
      text.setEditable(false);
      text.setPrefHeight(400);
      main = new VBox(30);
      

      main.setVisible(false);

      main.autosize();
      main.setManaged(false);
      main.setAlignment(Pos.TOP_CENTER);
      main.setBorder(new Border(new BorderStroke(Color.BLACK,
              BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
      main.setStyle("-fx-background-color: #FFFFFF;");

      close = new Button("Close");
      close.setOnMouseClicked(e -> {
         main.setVisible(false);
         text.clear();
      });
      main.setPadding(new Insets(60, 30, 30, 30));

      double w = main.prefWidth(-1);
      double h = main.prefHeight(w);
      main.resizeRelocate(150.5, 83.5, w, h);

      main.setPrefSize(500, 600);
      main.autosize();

   }

   /**
    *
    * @return
    */
   public VBox getMainPane() {
      return main;
   }

   /**
    * Setting up each section of the rules. Connecting each button two its own
    * individual page
    */
   public void gameRules() {
      userSelection.getItems().clear();
      userSelection.setPromptText("Select Section");
      main.getChildren().clear();
      header.setText("Game Rules");

      userSelection.getItems().addAll("About", "Treasure", "Crew Cards", "Starting the game", "Taking Turns", "Arriving at Port", "Trading", "Attacking", "Islands and Bays", "End of Game");

      Button select = new Button("Select");
      select.setOnMouseClicked(e -> {
         if (userSelection.getValue() != null) {
            text.clear();
            switch (userSelection.getValue().toString()) {
               case "About":
                  text.setText("Buccaneer is a turned based game, where the aim of the game is to sail around the board collecting treasure. The first player to have at least 20 points worth of treasure at their home port is the winner.\n\n"
                          + "The board is a 20 x 20 grid, in the middle of the grid you will find Treasure Island, and then on the top left and bottom right of the board you will find Pirate Island and Flat Island accordingly. Along the edges of the board, there are six ports. Four of which are belonging to an individual player, acting as their 'home' port (London, Genoa, Marseilles and Cadiz) and the remaining two ports are there solely to be traded with (Amsterdam and Venice). On the board you also have three different bays: Cliff Bay, Anchor Bay and Mud Bay.");
                  break;
               case "Treasure":
                  text.setText("Within the game you will have five different types of treasure:\n- Diamonds which have a value of 5 points\n- Rubies which have a value of 5 points\n- Gold bars which have a value of 4 points\n- Pearls which have a value of 3 points\n- Barrels of rum which have a value of 2 points");

                  break;
               case "Crew Cards":
                  text.setText("We then have crew cards which represent how many pirates each player has to their name. Each card can represent 1, 2 or 3 pirates and the colour can either be red or black. These crew cards are used to tell both how many squares a ship might move and the ships combat value.\n\nThe number of squares a ship might move can simply be worked out by adding the totals of all the crew cards in the players hand. The combat value of the crew would be calculated by adding up all the black cards and then subtracting the total number of red cards from the total number, or vice versa depending on which gives a positive value.");

                  break;
               case "Starting the game":
                  text.setText("When the game begins each player will randomly get assigned a boat and a port, they will also be given five crew cards. Each port is given 2 cards, and then treasures are also randomly assigned with a total value of 8.\n\nSome extra crew cards and a number of treasures are then put in various other places on the borad. The rest of the treasures will then be placed on the Treasure Island and the rest of the crew cards are placed face down on the Pirate Island.");

                  break;
               case "Taking Turns":
                  text.setText("- Each player will take in turn depending on which is their Home Port. The first turn will be given to the player with London as their Home port, then Genoa, then Marsailles, then Cadiz. \n\n"
                          + "- In a turn a player can choose to; move/rotate, sail to Treasure Island and draw a Chance Card, Sail to another harbour to trade and also they could choose to attack another players.\n\n"
                          + "- In wach turn the player has, they should be given an appropriate choice of either moving there ship and then selecting an orientation, or just to turn the ship. However, if the player is within a port they do not have a choice to just rotate, it is compulsory to move. \n\n"
                          + "- If a player chooses to move to a square occupied by another player, then they must attack the other player. It is illegal to attack a player on the coast of Treasure Island or in a port. \n\n"
                          + "- Also if the chosen move takes the player through a square which is ovvupied by another player, and that square is not a port or on the coast of Treasure Island, then the player they will be moving through will be given the choice to attack the player. In which case the move will be cut short at the square of the player being passed and the attack sequence is initiated.\n\n"
                          + "- After any move not involving an attack, or if the player has elected just to turn the ship, ther player is asked which direction they wish to turn the ship, unless they have moved to a port and then the choice of turning is irrelivent. If a player has ellected to turn in any direction which would then not allow them to move more then 1 square in the subsequent turn, it is deemed as illegal.\n\n"
                          + "- If at any point a player is on a square next to Treasure Island, Access to treasure island is granted.\n\n"
                          + "- If at any point a player is on a square next to Flat Island, Access to treasure island is granted.\n\n"
                          + "- If at any point a player is on a square next to a port, access to the port is granted.");

                  break;
               case "Arriving at Port":
                  text.setText("- When a player arrives at their home Port, any treasures in the ship is unloaded.\n- When a player arrives at another ports, they can trade with the port if it contains any treasure or cards. \n- If the port you are trading with is a home port of another player, any cards deposited at the port will be immediately added to the owner of the ports's hand.\n- There is certain chance cards which the player will get given the choice to use when they arrive at the port.");

                  break;
               case "Trading":
                  text.setText("When arriving at a port, you might wish to trade. You are able to trade crew cards and treasures which you have on your ship with those which are in the port. The items you are trading in have to be of equal value to those which you are recieving from the port. This also goes for treasures or crew cards placed in other players home ports. If you have three identical treasures in your hometown (for instance three gold bars) they will be moved into your safe zone, which means they cant be touched any more.");

                  break;
               case "Attacking":
                  text.setText("- When a player elects to attack another player, the winning of the battle will be the one who has a greater fighting strength.\n- If the lose has treasure on their ship, then the winner is awarded the treasure. Then if the winner doesnt have enough space on their ship for the treasure, the extra treasure is returned to Treasure Island. (A ship can hold a maximum of two pieces of treasure, and they might already have some when the battle commences.)\n- If the loser doesnt have any treasure, but has cards, then the two lowest value cards in the losers hand are given to the winner. However, if the loser only has 1 card, then that is just handed over to the winner.\n- Once the battle is complete, the loser is then forced to make a legal move up to the maximum squares they can move in any direction, followed by a change in direction. They HAVE to move atleast 1 square.\n- The winner will remain as they were, maintaining the direction they were facing.");

                  break;
               case "Islands and Bays":
                  text.setText("TREASURE ISLAND\nWhen a player arrives at treasure island they draw a chance card and follow the instructions. An example of a chance card would be:\n"
                          + "- 'Your best pirates have deserted to Pirates Island. Place your best crew card on Pirate Island'. \n\nFLAT ISLAND\nWhen the end of a players turn leaves them adjacent to the coast of the Flat island, they are awarded any treasure there (aslong as they have room in their ship; if there is only one space then they take the most value item of the island). Any cards on flat island are added to the players hand.\n\nANCHOR BAY\nAnchor bay only affects the game if the player holds chance card 25 and 26. If they do, the card is exchanged for a maximum of two items of treasure depending on what the players ship already holds. The value of the treasure taken can't have a combined total value more then 7.");

                  break;
               case "End of Game":
                  text.setText("When a player reaches their home port, the game will calculate the value of the treasure stored in their port and on the ship. If the value is atleast 20 points, they have won the game.");
                  break;
            }

         }

      });
      HBox selectionHolder = new HBox(10);
      selectionHolder.setPadding(new Insets(0, 0, 0, 100));
      selectionHolder.getChildren().addAll(userSelection, select);
      main.getChildren().addAll(header, selectionHolder, text, close);
   }

   public void chanceCardReferences() {

      main.getChildren().clear();

      header.setText("Chance Cards");
      userSelection.getItems().clear();
      userSelection.setPromptText("Select Chance Card");
      Button select = new Button("Select");
      select.setOnMouseClicked(e -> {
         if (userSelection.getValue() != null) {
            text.clear();
            switch (userSelection.getValue().toString()) {
               case "1":
                  text.setText("Your ship is blown 5 leagues (5 squares) off the coast of Treasure Island. If your crew total is 3 or less, take 4 crew cards from Pirate Island. If the square you are blown to is already occupied, move one square further)");
                  break;
               case "2":
                  text.setText("Present this card to any player who must then give you 3 crew cards. This card must be used at once then returned to the Chance card pack.");
                  break;
               case "3":
                  text.setText("You are blown to Mud Bay. If your crew total is 3 or less, take 4 crew cards from Pirate Island.");
                  break;
               case "4":
                  text.setText("You are blown to Cliff Creek. If your crew total is 3 or less, take 4 crew cards from Pirate Island.");
                  break;
               case "5":
                  text.setText("You are blown to your Home Port. If your crew total is 3 or less, take 4 crew cards from Pirate Island.");
                  break;
               case "6":
                  text.setText("You are blown to a random port. If your crew total is 3 or less, take 4 crew cards from Pirate Island.");
                  break;
               case "7":
                  text.setText("One treasure from your ship or 2 crew cards from your hand are lost and washed overboard to the nearest ship. If 2 ships are equidistant from yours you may ignore this instruction.");
                  break;
               case "8":
                  text.setText("One treasure from your ship or 2 crew cards from your hand are lost and washed overboard to Flat Island.");
                  break;
               case "9":
                  text.setText("Your most valuable treasure on board or if no treasure, the best crew card from your hand is washed overboard to Flat Island.");
                  break;
               case "10":
                  text.setText("The best crew card in your hand deserts for Pirate Island. The card must be placed there immediately.");
                  break;
               case "11":
                  text.setText("Take treasure up to 5 in total value, or 2 crew cards from Pirate Island.");
                  break;
               case "12":
                  text.setText("Take treasure up to 4 in total value, or 2 crew cards from Pirate Island.");
                  break;
               case "13":
                  text.setText("Take treasure up to 5 in total value, or 2 crew cards from Pirate Island.");
                  break;
               case "14":
                  text.setText("Take treasure up to 7 in total value, or 3 crew cards from Pirate Island.");
                  break;
               case "15":
                  text.setText("Take 2 crew cards from Pirate Island.");
                  break;
               case "16":
                  text.setText("Take treasure up to 7 in total value and reduce your ship's crew to 10, by taking crew cards from your hand and placing them on Pirate Island.");
                  break;
               case "17":
                  text.setText("Take treasure up to 6 in total value and reduce your ship's crew to 11, by taking crew cards from your hand and placing them on Pirate Island.");
                  break;
               case "18":
                  text.setText("Take treasure up to 4 in total value, and if your crew total is 7 or less, take 2 crew cards from Pirate Island.");
                  break;
               case "19":
                  text.setText("Exchange all crew cards in your hand as far as possible for the same number of crew cards from Pirate Island.");
                  break;
               case "20":
                  text.setText("If the ship of another player is anchored at Treasure Island, exchange 2 of your crew cards with that player. Both turn your cards face down and take 2 cards from each others hands without looking at them. If there is no other player at Treasure Island, place 2 of your crew cards on Pirate Island.");
                  break;
               case "21":
                  text.setText("Long John Silver (Keep this card). When you arrive at a port where there are crew for sale, you may exchange Long John for up to 5 crew in value. If you land at a Port where Long John has been left, you may take him on payment of one treasure to the Port. Once Long John has been played, he is not returned to the pack.");
                  break;
               case "22":
                  text.setText("Yellow fever! An epidemic of yellow fever strikes all ships and reduces the number of crew. Every player with more than 7 crew cards in their hand must bury the surplus crew cards at once on Pirate Island. He is at liberty to choose which of his cards shall be buried there.");
                  break;
               case "23":
                  text.setText("Doubloons (Keep this card). This card may be traded for crew or treasure up to value 5 in any port you visit.");
                  break;
               case "24":
                  text.setText("Pieces of eight (Keep this card). This card may be traded for crew or treasure up to value 4 in any port you visit.");
                  break;
               case "25":
                  text.setText("Kidd's chart (Keep this card). You may sail to the far side of Pirate Island, on to the square marked with an anchor. Land this chart there, and take treasure up to 7 in total value from Treasure Island.");
                  break;
               case "26":
                  text.setText("Kidd's chart (Keep this card). You may sail to the far side of Pirate Island, on to the square marked with an anchor. Land this chart there, and take treasure up to 7 in total value from Treasure Island.");
                  break;
               case "27":
                  text.setText("Take treasure up to 5 in total value, or 3 crew cards from Pirate Island.");
                  break;
               case "28":
                  text.setText("Take 2 crew cards from Pirate Island.");
                  break;
            }
         }
      });

      for (int x = 1; x < 29; ++x) {
         userSelection
                 .getItems().add(x);
      }
      HBox selectionHolder = new HBox(10);
      selectionHolder.setPadding(new Insets(0, 0, 0, 100));
      selectionHolder.getChildren().addAll(userSelection, select);
      text.clear();
      main.getChildren().addAll(header, selectionHolder, text, close);
   }

}
