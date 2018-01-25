package uk.ac.aber.cs221.group13.gui;

import uk.ac.aber.cs221.group13.gameobjects.CrewCard;
import uk.ac.aber.cs221.group13.gameobjects.Player;
import uk.ac.aber.cs221.group13.gameobjects.Port;
import uk.ac.aber.cs221.group13.gameobjects.Treasure;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author dpv
 */
public class TradingWindow {

   /*there are that many public variables for the class because once they are added to the GUI they are difficult and inneficient to find
    keeping their references here allows direct access O(1) and easier modification
    
    otherwise each element has to have an ID and there should be a search by ID through all the elements of the particular Pane and a lot of type castings
    */
   private static VBox mainContainer;
   private HBox horizontalContainer;
   private int[][] quantity = new int[2][5];
   private int[][] crewCardsQuantity = new int[2][6];
   private HBox[] selectedItems = new HBox[2];
   private ArrayList<Treasure>[] selectedTreasureList = new ArrayList[2];
   private ArrayList<CrewCard>[] selectedCrewList = new ArrayList[2];
   private int[] selectedItemsValue = new int[2];
   private Label[] selectedItemsLabel = new Label[2];
   private Label[][][] quantityLabels = new Label[2][2][6];
   private Player player;
   private Port port;

   public TradingWindow() {
      mainContainer = new VBox(20);
      mainContainer.setPadding(new Insets(40, 0, 0, 0));
      mainContainer.setAlignment(Pos.TOP_CENTER);
      mainContainer.setVisible(false);
      mainContainer.setManaged(false);
      mainContainer.setPrefSize(600, 500);
      horizontalContainer = new HBox(60);

      horizontalContainer.setPadding(new Insets(0, 0, 0, 40));

      double w = mainContainer.prefWidth(0);
      double h = mainContainer.prefHeight(w);
      mainContainer.resizeRelocate(80, 100, w, h);
      mainContainer.autosize();
      DropShadow borderGlow = new DropShadow();
      borderGlow.setOffsetY(0f);
      borderGlow.setOffsetX(0f);
      borderGlow.setColor(Color.BLUEVIOLET);
      borderGlow.setWidth(150);
      borderGlow.setHeight(150);
      mainContainer.setEffect(borderGlow);
      mainContainer.setStyle("-fx-background-color: #FFFFFF;");

      mainContainer.setBorder(new Border(new BorderStroke(Color.BLACK,
              BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

      Label trade = new Label("Trade");
      trade.setFont(new Font(30));

      Button close = new Button("Close");
      close.setOnMouseClicked(e -> {
         mainContainer.setVisible(false);
         horizontalContainer.getChildren().clear();
      });

      Button tradeBtn = new Button("Trade");
      tradeBtn.setOnMouseClicked(e -> {
         trade();
      });
      for (int z = 0; z < 2; ++z) {
         for (int x = 0; x < 2; ++x) {
            for (int y = 0; y < 6; ++y) {
               quantityLabels[z][x][y] = new Label("");
            }
         }
      }
      for (int x = 0; x < 2; ++x) {
         selectedCrewList[x] = new ArrayList();
         selectedTreasureList[x] = new ArrayList();
         selectedItemsLabel[x] = new Label("Selected items :" + selectedItemsValue[x]);
         selectedItems[x] = new HBox(5);
      }

      mainContainer.getChildren().addAll(trade, horizontalContainer, tradeBtn, close);
   }

   public VBox getTradingWindow() {
      return mainContainer;
   }

   public void setNewTrade(Player pl, Port port) {
      this.player = null;
      this.port = null;
      this.player = pl;
      this.port = port;
      horizontalContainer.getChildren().clear();

      VBox playerItems = new VBox(20);
      VBox portItems = new VBox(20);
      clearQuantityLabel();
      updateItems(pl.getNickname(), pl.getCards(), pl.getShip().getTreasure(), playerItems, 0);
      updateItems(port.getName(), port.getCrewCards(), port.getTreasure(), portItems, 1);
      horizontalContainer.getChildren().addAll(playerItems, portItems);
      updateLabels(0, 0);
      updateLabels(0, 1);

      mainContainer.setVisible(true);

   }

   private void updateItems(String name, ArrayList<CrewCard> cardList, ArrayList<Treasure> treasureList, VBox pane, int leftRight) {
      pane.setAlignment(Pos.TOP_CENTER);
      Label paneName = new Label(name);
      paneName.setFont(new Font(18));
      pane.getChildren().addAll(paneName, new Label("Treasure List"),
              getTreasureListImages(treasureList, leftRight),
              new Label("Crew Cards list"), getCrewCardListImages(cardList, leftRight),
              selectedItemsLabel[leftRight], selectedItems[leftRight]);
   }

   private void trade() {
      if (selectedItemsValue[0] == selectedItemsValue[1]) {
         int playerTreasureCounter = 0;
         for (int x = 0; x < 5; ++x) {
            playerTreasureCounter += quantity[0][x];
         }
         if (!selectedTreasureList[1].isEmpty()) {
            playerTreasureCounter += selectedTreasureList[1].size();
         }
         if (playerTreasureCounter > 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Trading error !");
            alert.setContentText("Your ship cannot hold " + playerTreasureCounter + " treasures !");
            alert.showAndWait();
         } else {

            //CREW CARDS
            //from player to port
            if (!selectedCrewList[0].isEmpty()) {
               port.getCrewCards().addAll(selectedCrewList[0]);
               for (CrewCard card : selectedCrewList[0]) {
                  findAndRemoveCrewCard(player.getCards(), card);
               }
               selectedCrewList[0].clear();
            }
            //from port to player
            if (!selectedCrewList[1].isEmpty()) {
               player.getCards().addAll(selectedCrewList[1]);
               for (CrewCard card : selectedCrewList[1]) {
                  findAndRemoveCrewCard(port.getCrewCards(), card);
               }
               selectedCrewList[1].clear();
            }

            //TREASURE
            if (!selectedTreasureList[0].isEmpty()) {
               port.getTreasure().addAll(selectedTreasureList[0]);
               for (Treasure t : selectedTreasureList[0]) {
                  findAndRemoveTreasure(player.getShip().getTreasure(), t);
               }
               selectedTreasureList[0].clear();
            }
            if (!selectedTreasureList[1].isEmpty()) {
               player.getShip().getTreasure().addAll(selectedTreasureList[1]);
               for (Treasure t : selectedTreasureList[1]) {
                  findAndRemoveTreasure(port.getTreasure(), t);
               }
               selectedTreasureList[1].clear();
            }
            setNewTrade(player, port);
         }
      } else {

         Alert alert = new Alert(Alert.AlertType.ERROR);
         alert.setHeaderText("Trading error !");
         alert.setContentText("Both trading values should match !");
         alert.showAndWait();
         setNewTrade(player, port);
      }
   }

   public int getTreasureValue(String trName) {
      switch (trName) {
         case "Diamonds":
            return 5;
         case "Rubies":
            return 5;
         case "Gold bars":
            return 4;
         case "Pearls":
            return 3;
         case "Barrels of rum":
            return 2;
      }
      return 0;
   }

   private ImageView getTreasureImageView(String trName, int leftRight) {
      ImageView img = new ImageView();
      switch (trName) {

         case "Diamonds":
            img.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/treasure/diamond.png"));
            img.setUserData(new Treasure("Diamonds"));
            img.setOnMouseClicked(e -> {
               if (quantity[leftRight][0] > 0) {
                  quantity[leftRight][0] -= 1;
                  ImageView i = new ImageView(img.getImage());
                  i.setUserData(img.getUserData());
                  i.setOnMouseClicked(event -> {
                     selectedItems[leftRight].getChildren().remove(i);
                     quantity[leftRight][0] += 1;
                     selectedTreasureList[leftRight].remove((Treasure) img.getUserData());
                     updateLabels(0 - ((Treasure) img.getUserData()).getValue(), leftRight);
                  });
                  selectedItems[leftRight].getChildren().add(i);
                  updateLabels(((Treasure) img.getUserData()).getValue(), leftRight);
                  selectedTreasureList[leftRight].add((Treasure) img.getUserData());
               }
            });

            break;
         case "Rubies":
            img.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/treasure/rubies.png"));
            img.setUserData(new Treasure("Rubies"));
            img.setOnMouseClicked(e -> {
               if (quantity[leftRight][1] > 0) {
                  quantity[leftRight][1] -= 1;
                  ImageView i = new ImageView(img.getImage());
                  i.setUserData(img.getUserData());

                  i.setOnMouseClicked(event -> {
                     selectedItems[leftRight].getChildren().remove(i);
                     quantity[leftRight][1] += 1;
                     selectedTreasureList[leftRight].remove((Treasure) img.getUserData());
                     updateLabels(0 - ((Treasure) img.getUserData()).getValue(), leftRight);
                  });
                  selectedItems[leftRight].getChildren().add(i);
                  updateLabels(((Treasure) img.getUserData()).getValue(), leftRight);
                  selectedTreasureList[leftRight].add((Treasure) img.getUserData());
               }
            });

            break;
         case "Gold bars":
            img.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/treasure/gold_bar.png"));
            img.setUserData(new Treasure("Gold bars"));
            img.setOnMouseClicked(e -> {
               if (quantity[leftRight][2] > 0) {
                  quantity[leftRight][2] -= 1;
                  ImageView i = new ImageView(img.getImage());
                  i.setUserData(img.getUserData());
                  i.setOnMouseClicked(event -> {
                     selectedItems[leftRight].getChildren().remove(i);
                     quantity[leftRight][2] += 1;
                     selectedTreasureList[leftRight].remove((Treasure) img.getUserData());
                     updateLabels(0 - ((Treasure) img.getUserData()).getValue(), leftRight);
                  });
                  selectedItems[leftRight].getChildren().add(i);
                  selectedTreasureList[leftRight].add((Treasure) img.getUserData());
                  updateLabels(((Treasure) img.getUserData()).getValue(), leftRight);
               }
            });

            break;
         case "Pearls":
            img.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/treasure/pearls.png"));
            img.setUserData(new Treasure("Pearls"));
            img.setOnMouseClicked(e -> {
               if (quantity[leftRight][3] > 0) {
                  quantity[leftRight][3] -= 1;
                  ImageView i = new ImageView(img.getImage());
                  i.setUserData(img.getUserData());
                  i.setOnMouseClicked(event -> {
                     selectedItems[leftRight].getChildren().remove(i);
                     quantity[leftRight][3] += 1;
                     selectedTreasureList[leftRight].remove((Treasure) img.getUserData());
                     updateLabels(0 - ((Treasure) img.getUserData()).getValue(), leftRight);
                  });
                  selectedItems[leftRight].getChildren().add(i);
                  updateLabels(((Treasure) img.getUserData()).getValue(), leftRight);
                  selectedTreasureList[leftRight].add((Treasure) img.getUserData());
               }
            });

            break;
         case "Barrels of rum":
            img.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/treasure/barrels.png"));
            img.setUserData(new Treasure("Barrels of rum"));
            img.setOnMouseClicked(e -> {
               if (quantity[leftRight][4] > 0) {
              //    System.err.println("quantity " + quantity[leftRight][4]);
                  quantity[leftRight][4] -= 1;
                //  System.err.println("quantity after " + quantity[leftRight][4]);
                  ImageView i = new ImageView(img.getImage());
                  i.setUserData(img.getUserData());
                  i.setOnMouseClicked(event -> {
                     selectedItems[leftRight].getChildren().remove(i);
                     quantity[leftRight][4] += 1;
                     selectedTreasureList[leftRight].remove((Treasure) img.getUserData());
                     updateLabels(0 - ((Treasure) img.getUserData()).getValue(), leftRight);
                  });
                  selectedItems[leftRight].getChildren().add(i);
                  updateLabels(((Treasure) img.getUserData()).getValue(), leftRight);
                  selectedTreasureList[leftRight].add((Treasure) img.getUserData());
               }
            });

            break;
      }

      return img;
   }

   private ImageView getCrewCardImageVIew(int value, boolean isBlack, int leftRight) {
      ImageView img = new ImageView();
      int color = (isBlack) ? 1 : 0;
      switch (color) {
         case 0://red
            switch (value) {
               case 1:
                  img.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/crewcards/tradingwindowcrewcards/red1.png"));
                  img.setUserData(new CrewCard(1, false));
                  img.setOnMouseClicked(e -> {
                     if (crewCardsQuantity[leftRight][0] > 0) {
                        crewCardsQuantity[leftRight][0] -= 1;
                        ImageView i = new ImageView(img.getImage());
                        i.setUserData(img.getUserData());
                        i.setOnMouseClicked(event -> {
                           selectedItems[leftRight].getChildren().remove(i);
                           crewCardsQuantity[leftRight][0] += 1;
                           selectedCrewList[leftRight].remove((CrewCard) img.getUserData());
                           updateLabels(0 - ((CrewCard) img.getUserData()).getValue(), leftRight);
                        });

                        selectedItems[leftRight].getChildren().add(i);
                        updateLabels(((CrewCard) img.getUserData()).getValue(), leftRight);
                        selectedCrewList[leftRight].add(((CrewCard) img.getUserData()));
                     }
                  });
                  break;
               case 2:
                  img.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/crewcards/tradingwindowcrewcards/red2.png"));
                  img.setUserData(new CrewCard(2, false));
                  img.setOnMouseClicked(e -> {
                     if (crewCardsQuantity[leftRight][1] > 0) {
                        crewCardsQuantity[leftRight][1] -= 1;
                        ImageView i = new ImageView(img.getImage());
                        i.setUserData(img.getUserData());
                        i.setOnMouseClicked(event -> {
                           selectedItems[leftRight].getChildren().remove(i);
                           crewCardsQuantity[leftRight][1] += 1;
                           selectedCrewList[leftRight].remove((CrewCard) img.getUserData());
                           updateLabels(0 - ((CrewCard) img.getUserData()).getValue(), leftRight);
                        });
                        selectedItems[leftRight].getChildren().add(i);
                        updateLabels(((CrewCard) img.getUserData()).getValue(), leftRight);
                        selectedCrewList[leftRight].add(((CrewCard) img.getUserData()));
                     }
                  });
                  break;
               case 3:
                  img.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/crewcards/tradingwindowcrewcards/red3.png"));
                  img.setUserData(new CrewCard(3, false));
                  img.setOnMouseClicked(e -> {
                     if (crewCardsQuantity[leftRight][2] > 0) {
                        crewCardsQuantity[leftRight][2] -= 1;
                        ImageView i = new ImageView(img.getImage());
                        i.setUserData(img.getUserData());
                        i.setOnMouseClicked(event -> {
                           selectedItems[leftRight].getChildren().remove(i);
                           crewCardsQuantity[leftRight][2] += 1;
                           selectedCrewList[leftRight].remove((CrewCard) img.getUserData());
                           updateLabels(0 - ((CrewCard) img.getUserData()).getValue(), leftRight);
                        });
                        selectedItems[leftRight].getChildren().add(i);
                        updateLabels(((CrewCard) img.getUserData()).getValue(), leftRight);
                        selectedCrewList[leftRight].add(((CrewCard) img.getUserData()));
                     }
                  });
                  break;
            }
            break;
         case 1://black
            switch (value) {
               case 1:
                  img.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/crewcards/tradingwindowcrewcards/black1.png"));
                  img.setUserData(new CrewCard(1, true));
                  img.setOnMouseClicked(e -> {
                     if (crewCardsQuantity[leftRight][3] > 0) {
                        crewCardsQuantity[leftRight][3] -= 1;
                        ImageView i = new ImageView(img.getImage());
                        i.setUserData(img.getUserData());
                        i.setOnMouseClicked(event -> {
                           selectedItems[leftRight].getChildren().remove(i);
                           crewCardsQuantity[leftRight][3] += 1;
                           selectedCrewList[leftRight].remove((CrewCard) img.getUserData());
                           updateLabels(0 - ((CrewCard) img.getUserData()).getValue(), leftRight);
                        });
                        selectedItems[leftRight].getChildren().add(i);
                        updateLabels(((CrewCard) img.getUserData()).getValue(), leftRight);
                        selectedCrewList[leftRight].add(((CrewCard) img.getUserData()));
                     }
                  });
                  break;
               case 2:
                  img.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/crewcards/tradingwindowcrewcards/black2.png"));
                  img.setUserData(new CrewCard(2, true));
                  img.setOnMouseClicked(e -> {
                     if (crewCardsQuantity[leftRight][4] > 0) {
                        crewCardsQuantity[leftRight][4] -= 1;
                        ImageView i = new ImageView(img.getImage());
                        i.setUserData(img.getUserData());
                        i.setOnMouseClicked(event -> {
                           selectedItems[leftRight].getChildren().remove(i);
                           crewCardsQuantity[leftRight][4] += 1;
                           selectedCrewList[leftRight].remove((CrewCard) img.getUserData());
                           updateLabels(0 - ((CrewCard) img.getUserData()).getValue(), leftRight);
                        });
                        selectedItems[leftRight].getChildren().add(i);
                        updateLabels(((CrewCard) img.getUserData()).getValue(), leftRight);
                        selectedCrewList[leftRight].add(((CrewCard) img.getUserData()));
                     }
                  });

                  break;
               case 3:
                  img.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/crewcards/tradingwindowcrewcards/black3.png"));
                  img.setUserData(new CrewCard(3, true));
                  img.setOnMouseClicked(e -> {
                     if (crewCardsQuantity[leftRight][5] > 0) {
                        crewCardsQuantity[leftRight][5] -= 1;
                        ImageView i = new ImageView(img.getImage());
                        i.setUserData(img.getUserData());
                        i.setOnMouseClicked(event -> {
                           selectedItems[leftRight].getChildren().remove(i);
                           crewCardsQuantity[leftRight][5] += 1;
                           selectedCrewList[leftRight].remove((CrewCard) img.getUserData());
                           updateLabels(0 - ((CrewCard) img.getUserData()).getValue(), leftRight);
                        });
                        selectedItems[leftRight].getChildren().add(i);
                        updateLabels(((CrewCard) img.getUserData()).getValue(), leftRight);
                        selectedCrewList[leftRight].add(((CrewCard) img.getUserData()));
                     }
                  });
                  break;
            }
            break;
      }
      return img;
   }

   private HBox getTreasureListImages(ArrayList<Treasure> trList, int leftRight) {
      HBox treasure = new HBox(5);
      for (Treasure t : trList) {
         switch (t.getType()) {
            case "Diamonds":
               quantity[leftRight][0] += 1;
               break;
            case "Rubies":
               quantity[leftRight][1] += 1;
               break;
            case "Gold bars":
               quantity[leftRight][2] += 1;
               break;
            case "Pearls":
               quantity[leftRight][3] += 1;
               break;
            case "Barrels of rum":
               quantity[leftRight][4] += 1;
               break;
         }
      }

      for (int x = 0; x < 5; ++x) {
         switch (x) {
            case 0:
               treasure.getChildren().addAll(getTreasureImageView("Diamonds", leftRight), quantityLabels[leftRight][1][0]);
               //  treasure.getChildren().add(new Label(Integer.toString(quantity[leftRight][0])));
               break;
            case 1:
               treasure.getChildren().addAll(getTreasureImageView("Rubies", leftRight), quantityLabels[leftRight][1][1]);
               //  treasure.getChildren().add(new Label(Integer.toString(quantity[leftRight][1])),quantityLabels[leftRight][1][]);
               break;
            case 2:
               treasure.getChildren().addAll(getTreasureImageView("Gold bars", leftRight), quantityLabels[leftRight][1][2]);
               // treasure.getChildren().add(new Label(Integer.toString(quantity[leftRight][2])));
               break;
            case 3:
               treasure.getChildren().addAll(getTreasureImageView("Pearls", leftRight), quantityLabels[leftRight][1][3]);

               break;
            case 4:
               treasure.getChildren().addAll(getTreasureImageView("Barrels of rum", leftRight), quantityLabels[leftRight][1][4]);
               //  treasure.getChildren().add(new Label(Integer.toString(quantity[leftRight][4])));
               break;
         }
      }
      return treasure;
   }

   private HBox getCrewCardListImages(ArrayList<CrewCard> cardList, int leftRight) {
      HBox cardImages = new HBox(5);
      int color = 0;
      for (CrewCard card : cardList) {
         color = (card.isBlack()) ? 1 : 0;
         switch (color) {
            case 0:
               switch (card.getValue()) {
                  case 1:
                     crewCardsQuantity[leftRight][0] += 1;
                     break;
                  case 2:
                     crewCardsQuantity[leftRight][1] += 1;
                     break;
                  case 3:
                     crewCardsQuantity[leftRight][2] += 1;
                     break;
               }
               break;
            case 1:
               switch (card.getValue()) {
                  case 1:
                     crewCardsQuantity[leftRight][3] += 1;
                     break;
                  case 2:
                     crewCardsQuantity[leftRight][4] += 1;
                     break;
                  case 3:
                     crewCardsQuantity[leftRight][5] += 1;
                     break;
               }
               break;
         }
      }

      for (int x = 0; x < 6; ++x) {
         switch (x) {

            case 0:
               cardImages.getChildren().addAll(getCrewCardImageVIew(1, false, leftRight),
                       quantityLabels[leftRight][0][0]);
               // cardImages.getChildren().add(new Label(Integer.toString(crewCardsQuantity[leftRight][0])));
               break;
            case 1:
               cardImages.getChildren().addAll(getCrewCardImageVIew(2, false, leftRight), quantityLabels[leftRight][0][1]);
               //  cardImages.getChildren().add(new Label(Integer.toString(crewCardsQuantity[leftRight][1])));
               break;
            case 2:
               cardImages.getChildren().addAll(getCrewCardImageVIew(3, false, leftRight), quantityLabels[leftRight][0][2]);
               // cardImages.getChildren().add(new Label(Integer.toString(crewCardsQuantity[leftRight][2])));
               break;
            case 3:
               cardImages.getChildren().addAll(getCrewCardImageVIew(1, true, leftRight), quantityLabels[leftRight][0][3]);
               //     cardImages.getChildren().add(new Label(Integer.toString(crewCardsQuantity[leftRight][3])));
               break;
            case 4:
               cardImages.getChildren().addAll(getCrewCardImageVIew(2, true, leftRight), quantityLabels[leftRight][0][4]);
               // cardImages.getChildren().add(new Label(Integer.toString(crewCardsQuantity[leftRight][4])));
               break;
            case 5:
               cardImages.getChildren().addAll(getCrewCardImageVIew(3, true, leftRight), quantityLabels[leftRight][0][5]);
               //  cardImages.getChildren().add(new Label(Integer.toString(crewCardsQuantity[leftRight][5])));
               break;
         }

      }

      return cardImages;
   }

   private void clearQuantityLabel() {
      for (int x = 0; x < 2; ++x) {
         selectedItems[x].getChildren().clear();
         selectedItemsValue[x] = 0;
         selectedItemsLabel[x].setText("Selected items value " + selectedItemsValue[x]);
         selectedCrewList[x].clear();
         selectedTreasureList[x].clear();
      }

      for (int x = 0; x < 2; ++x) {
         for (int y = 0; y < 6; ++y) {
            crewCardsQuantity[x][y] = 0;
            if (y < 5) {
               quantity[x][y] = 0;
            }
         }
      }
   }

   private void updateLabels(int value, int leftRight) {
      selectedItemsValue[leftRight] += value;
      selectedItemsLabel[leftRight].setText("Selected items: " + selectedItemsValue[leftRight]);

      for (int y = 0; y < 6; ++y) {
         quantityLabels[0][0][y].setText(Integer.toString(crewCardsQuantity[0][y]));//player crew
         quantityLabels[1][0][y].setText(Integer.toString(crewCardsQuantity[1][y]));//island crew
         if (y < 5) {
            quantityLabels[0][1][y].setText(Integer.toString(quantity[0][y])); //player treasure
            quantityLabels[1][1][y].setText(Integer.toString(quantity[1][y]));
         } //island treasure
      }
    //  System.err.println("quantity after update " + quantity[leftRight][4]);
   }

   private void findAndRemoveCrewCard(ArrayList<CrewCard> cards, CrewCard card) {
      CrewCard c = null;
      for (CrewCard cc : cards) {
         if (cc.getValue() == card.getValue() && cc.isBlack() == card.isBlack()) {
            c = cc;
         }
      }
      if (c != null) {
         cards.remove(c);
      } else {
         //   System.err.println("error card removing - trading");
      }
   }

   private void findAndRemoveTreasure(ArrayList<Treasure> trList, Treasure treasure) {
      Treasure t = null;
      for (Treasure tr : trList) {
         if (tr.getValue() == treasure.getValue() && tr.getType().equals(treasure.getType())) {
            t = tr;
         }
      }
      if (t != null) {
         trList.remove(t);

      } else {
         System.err.println("error treasure removing - trading");
      }
   }
}
