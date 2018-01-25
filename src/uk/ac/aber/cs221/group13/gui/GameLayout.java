package uk.ac.aber.cs221.group13.gui;

import uk.ac.aber.cs221.group13.gameobjects.Board;
import uk.ac.aber.cs221.group13.gameobjects.ChanceCard;
import uk.ac.aber.cs221.group13.gameobjects.Coordinates;
import uk.ac.aber.cs221.group13.gameobjects.CrewCard;
import uk.ac.aber.cs221.group13.gameobjects.Engine;
import uk.ac.aber.cs221.group13.gameobjects.Island;
import uk.ac.aber.cs221.group13.gameobjects.Player;
import uk.ac.aber.cs221.group13.gameobjects.Port;
import uk.ac.aber.cs221.group13.gameobjects.Position;
import uk.ac.aber.cs221.group13.gameobjects.Ship;
import uk.ac.aber.cs221.group13.gameobjects.Treasure;
import java.io.IOException;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;

//check chance card 6
//fix draw ship battle
//change all town names with port
//comments
//rules btn
/**
 * @author dpv
 */
public class GameLayout {

   private final static int gridSize = 20;//number squares
   private final BorderPane bp = new BorderPane();//the whole game GUI container
   private Engine gameEngine;//game engine
   private final Position[][] gridMap = new Position[gridSize][gridSize];//holds all player,port,island rectangle etc. data
   private final VBox hoverPosition;//hover pane
   private TradingWindow trading = new TradingWindow();// tradingWindow;
   private Button endTurn;//button for end turn - easier access
   private Button startGame;//button for start game - easier access
   private AlertPlayer alertPl;
   private List<Position> positionsToMove;//list of positions that the player can select to move
   private VBox gameLegend;
   private ChanceCardsItemTransfer itemTransfer;
   private GameRules rules;

   //game layout constructor
   protected void setGameRules(GameRules instance) {
      rules = instance;
   }

   public GameLayout(String[] playerNames) throws IOException {
      rules = new GameRules();
      //  takeTreasureFromIsland.setMinSize(500, 500);
      alertPl = new AlertPlayer();
      positionsToMove = new ArrayList();
      //End turn and start game buttons
      endTurn = new Button("End turn");
      startGame = new Button("Start Game");

      //buttons default settings
      startGame.setMinSize(100.0, 40.0);
      endTurn.setMinSize(100.0, 40.0);
      endTurn.setDisable(true);//disable the button by default

      //start game ot action
      startGame.setOnAction(e -> {
         gameEngine.setGameStarted();//change the boolean gameStarted to true
         startGame.setVisible(false);//disable the button
         showPos(gameEngine.getCurrentPlayer());//show the 1st player available positions
         bp.setTop(getTopPane());//update colors add current player crew cards, fight power etc
      });

      //end turn button - when clicked
      endTurn.setOnAction(e -> {
         gameEngine.newMove();
         gameEngine.resetHasMoved(); // new move
         showPos(gameEngine.getCurrentPlayer()); // show the next player's available positions
         endTurn.setDisable(true);//disable the button
         if (!gameEngine.ifGameHasWinner()) {
            bp.setTop(getTopPane());
            bp.setLeft(getLeftPane());
            bp.setRight(getRightPane());
            bp.setBottom(getBottomPane());

         }
      });

      HoverPosition hoverPane = new HoverPosition();
      hoverPosition = hoverPane.getHover();
      gameEngine = new Engine();
      gameEngine.initializePlayers(playerNames);
      gameEngine.getBoard().addCrewCardsAndTreasureToPorts();
      setupBorderPane();
      updateGridPane();

      setupHoveringPane(hoverPane);
      hoverPosition.setVisible(false);
        bp.getStylesheets().add("uk/ac/aber/cs221/group13/externalfiles/BuccaneerTheme.css");
   gridMap[0][0].getRectangle().setOnMouseClicked(e->{
   if(gameEngine.gameIsInBattleMode()){
   endTurn.setDisable(false);
   }
   });


   }

   //grid pane all the 20x20 positions rectangles etc.
   private VBox getCenterPane() {

      VBox centerPane = new VBox();//this will hold the grid pane + the hover panes - its type of pane is not important - it can be HBox etc.
      double size = 38 * 20;
      // centerPane.setPadding(new Insets(0,0,0,0));
      centerPane.setMinSize(size, size);
       centerPane.setMaxSize(size, size);
      centerPane.setPrefSize(size, size);

      GridPane grid = new GridPane();
      DropShadow borderGlow = new DropShadow();
      borderGlow.setOffsetY(0f);
      borderGlow.setOffsetX(0f);
      borderGlow.setColor(Color.rgb(79, 91, 102));
      borderGlow.setWidth(20);
      borderGlow.setHeight(20);
      grid.setEffect(borderGlow);
      int colValue = 0, rowValue = 19;
      for (int rows = 0; rows < gridSize; ++rows) {
         for (int cols = 0; cols < gridSize; ++cols) {
            if (colValue > 19) {
               colValue = 0;
            }
            Position pos = new Position(colValue, rowValue);

            pos.getRectangle().setOnMouseClicked(e -> {
               positionClicked(pos);
               if (!gameEngine.ifGameHasWinner()) {
                  bp.setTop(getTopPane());
                  bp.setLeft(getLeftPane());
                  bp.setRight(getRightPane());
                  bp.setBottom(getBottomPane());

               }//update the whole top pane i.e player coords move range etc.
            });
            pos.getRectangle().hoverProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean show) -> {
               if (show) {

                  ListView left = new ListView();
                  ObservableList<String> content = FXCollections.observableArrayList();
                  if (!pos.positonIsEmpty()) {
                     // VBox box = new VBox();
                     if (!pos.hasIsland()) {
                        if (pos.getIsland() != null && pos.getIsland().getName() != null) {
                           content.add(pos.getIsland().getName());
                           content.add(pos.getCoordinates().toString());
                        }
                     } else if (!pos.ifPositionHasAPort()) {
                        content.add(pos.getPort().getName());
                        content.add(pos.getPort().getCoordinates().toString());
                     } else if (pos.hasShip()) {
                        content.add(gameEngine.findPlayerByShip(pos.getShip()).getNickname());
                        content.add(pos.getShip().getCoordinates().toString());
                     }

                     left.setItems(content);
                     left.setMaxWidth(150.0);
                  }
               }
            });
            if (rows % 2 == 0 && cols % 2 == 0) {
               pos.getRectangle().setFill(Color.rgb(0, 128, 255));
            } else if (rows % 2 == 0 && cols % 2 == 1) {
               pos.getRectangle().setFill(Color.rgb(0, 191, 255));
            } else if (rows % 2 == 1 && cols % 2 == 0) {
               pos.getRectangle().setFill(Color.rgb(0, 191, 255));
            } else {
               pos.getRectangle().setFill(Color.rgb(0, 128, 255));
            }
            gridMap[colValue][rowValue] = pos;
            grid.add(pos.getRectangle(), cols, rows);//accepts object,col value,row value not vice versa !!
            ++colValue;
         }
         --rowValue;
      }
      itemTransfer = new ChanceCardsItemTransfer(gridMap[2][13]);
      grid.setStyle("-fx-background-color:#0080FF");
      centerPane.getChildren().addAll(grid, hoverPosition, trading.getTradingWindow(), itemTransfer.getPane(), rules.getMainPane());
      return centerPane;
   }

   public BorderPane getBorderPane() {
      return bp;
   }

//set the differents part of the border pane
   private void setupBorderPane() {
      bp.setLeft(getLeftPane());
      bp.setTop(getTopPane());
      bp.setCenter(getCenterPane());
      bp.setRight(getRightPane());
      bp.setBottom(getBottomPane());
   }

   private void updateGridPane() {
      Position currPos;

      Coordinates currPortCoords;
      Island currIsland;
      for (int x = 0; x < 6; ++x) {
         //ports
         currPortCoords = gameEngine.getBoard().getAllPorts()[x].getCoordinates();
         currPos = gridMap[currPortCoords.getColumn()][currPortCoords.getRow()];
         currPos.getRectangle().setFill(new ImagePattern(new Image("uk/ac/aber/cs221/group13/externalfiles/images/landing/ports/" + gameEngine.getBoard().getAllPorts()[x].getName() + ".png")));

         currPos.setPort(gameEngine.getBoard().getAllPorts()[x]);
      }
      String filePath = "";
      for (int i = 0; i < 6; ++i) {
         currIsland = gameEngine.getBoard().getAllIslands()[i];
         ArrayList<Coordinates> islandCoors = currIsland.getCoordinates();
         switch (currIsland.getName()) {
            case "Pirate Island":
               filePath = "uk/ac/aber/cs221/group13/externalfiles/images/landing/islands/pirateisland/";
               break;
            case "Treasure Island":
               filePath = "uk/ac/aber/cs221/group13/externalfiles/images/landing/islands/treasureisland/";
               break;
            case "Flat Island":
               filePath = "uk/ac/aber/cs221/group13/externalfiles/images/landing/islands/flatisland/";
               break;
            case "Cliff Creek":
               filePath = "uk/ac/aber/cs221/group13/externalfiles/images/landing/islands/cliffcreek/";
               break;
            case "Anchor Bay":
               filePath = "uk/ac/aber/cs221/group13/externalfiles/images/landing/islands/anchorbay/";
               break;
            case "Mud Bay":
               filePath = "uk/ac/aber/cs221/group13/externalfiles/images/landing/islands/mudbay/";
               break;
         }
         for (Coordinates c : islandCoors) {
            currPos = gridMap[c.getColumn()][c.getRow()];
            currPos.setIsland(currIsland);
            currPos.getRectangle().setFill(new ImagePattern(new Image(filePath + Integer.toString(c.getColumn()) + Integer.toString(c.getRow()) + ".png")));
         }

      }
   }

   private int showPos(Player p) {
      positionsToMove.clear();
      Coordinates plCoords = p.getShip().getCoordinates();
      if (p.getShip().isAtPort()) {//if the ship is at port show positions in all sides
         for (int cols = -1; cols < 2; ++cols) {
            for (int rows = -1; rows < 2; ++rows) {
               if (cols != 0) {
                  orderPosList(plCoords, new Coordinates(plCoords.getColumn() + cols, plCoords.getRow() + rows), p.getMovementRange());
               } else {
                  if (rows != 0) {
                     orderPosList(plCoords, new Coordinates(plCoords.getColumn() + cols, plCoords.getRow() + rows), p.getMovementRange());
                  }
               }
            }
         }
      } else {
         return orderPosList(plCoords, p.getShip().getShipTurn(), p.getMovementRange());
      }
      return 0;
   }

   private int orderPosList(Coordinates currentPos, Coordinates rotation, int moveRange) {
      ArrayList<Coordinates> orderedCoords = new ArrayList();

      int cols, rows;
      cols = rotation.getColumn() - currentPos.getColumn();
      rows = rotation.getRow() - currentPos.getRow();
      switch (cols) {
         case -1:
            switch (rows) {
               case -1:
                  for (int x = 1; x < moveRange + 1; ++x) {
                     orderedCoords.add(new Coordinates(currentPos.getColumn() - x, currentPos.getRow() - x));
                  }
                  break;
               case 0:
                  for (int x = 1; x < moveRange + 1; ++x) {
                     orderedCoords.add(new Coordinates(currentPos.getColumn() - x, currentPos.getRow()));
                  }
                  break;
               case 1:
                  for (int x = 1; x < moveRange + 1; ++x) {
                     orderedCoords.add(new Coordinates(currentPos.getColumn() - x, currentPos.getRow() + x));
                  }
                  break;
            }
            break;
         case 0:
            switch (rows) {
               case -1:
                  for (int x = 1; x < moveRange + 1; ++x) {
                     orderedCoords.add(new Coordinates(currentPos.getColumn(), currentPos.getRow() - x));
                  }
                  break;
               case 1:
                  for (int x = 1; x < moveRange + 1; ++x) {
                     orderedCoords.add(new Coordinates(currentPos.getColumn(), currentPos.getRow() + x));
                  }
                  break;
            }
            break;
         case 1:
            switch (rows) {
               case -1:
                  for (int x = 1; x < moveRange + 1; ++x) {
                     orderedCoords.add(new Coordinates(currentPos.getColumn() + x, currentPos.getRow() - x));
                  }
                  break;
               case 0:
                  for (int x = 1; x < moveRange + 1; ++x) {
                     orderedCoords.add(new Coordinates(currentPos.getColumn() + x, currentPos.getRow()));
                  }
                  break;
               case 1:
                  for (int x = 1; x < moveRange + 1; ++x) {
                     orderedCoords.add(new Coordinates(currentPos.getColumn() + x, currentPos.getRow() + x));
                  }
                  break;
            }
            break;

      }

      ArrayList<Coordinates> posList = new ArrayList();
      for (Coordinates c : orderedCoords) {
         if (c.getColumn() < 20 && c.getColumn() > -1 && c.getRow() < 20 && c.getRow() > -1) {
            posList.add(c);
         }
      }

      for (Coordinates c : posList) {
         if (gridMap[c.getColumn()][c.getRow()].positionHasNoPortOrIsland()) {
            if (!gridMap[c.getColumn()][c.getRow()].hasShip()) {
               gridMap[c.getColumn()][c.getRow()].getRectangle().setFill(Color.GREENYELLOW);
            }
            positionsToMove.add(gridMap[c.getColumn()][c.getRow()]);
         } else {//if there is port or island stop
            break;
         }
      }
      if (posList.isEmpty()) {
         if (positionsToMove.isEmpty()) {
            //calculate again the rotation
            if (gameEngine.getShipBattleWinner() != null) {
               Player looser = gameEngine.getShipBattleWinner();
               int colls = looser.getShip().getCoordinates().getColumn();//-looser.getShip().getCoordinates().getColumn();
               int rowws = looser.getShip().getCoordinates().getRow();//-looser.getShip().getCoordinates().getRow();
               Random random = new Random();
               int randomCol = random.nextInt(2) - 1;
               int randomRow = random.nextInt(2) - 1;

               for (int x = 0; x < looser.getMovementRange(); ++x) {
                  if ((colls + (randomCol * x)) > -1 && (colls + (randomCol * x)) < 20
                          && (rowws + (randomRow * x)) > -1 && (rowws + (randomRow * x)) < 20) {
                     posList.add(gridMap[colls + (randomCol * x)][rowws + (randomRow * x)].getCoordinates());
                  }
               }
               if (!posList.isEmpty()) {
                  for (Coordinates c : posList) {
                     if (gridMap[c.getColumn()][c.getRow()].positionHasNoPortOrIsland()) {
                        if (!gridMap[c.getColumn()][c.getRow()].hasShip()) {
                           gridMap[c.getColumn()][c.getRow()].getRectangle().setFill(Color.GREENYELLOW);
                        }
                        positionsToMove.add(gridMap[c.getColumn()][c.getRow()]);
                     } else {//if there is port or island stop
                        break;
                     }
                  }
             //     System.err.println(posList.size()
                  //);
               } else {
                  //System.err.println("poslist still empty");
               }

               alertPl.customAlert("Error", "Unfortunately " + looser.getNickname() + " ship rotation has been lost.\nRandom rotation will be generated.", ButtonType.CLOSE);
               //  System.err.println("showing again" + showPos(looser));

               gameEngine.setShipBattleWinner(null);
            }
         }

      }
      //  System.err.println("poslist size " + posList.size());
      return posList.size();
   }

   private void removeHighlightedPositions() {
      for (int rows = 0; rows < gridSize; ++rows) {
         for (int cols = 0; cols < gridSize; ++cols) {
            //  if (grid_map[cols][rows].getFill() == Color.GREENYELLOW || grid_map[cols][rows].getFill() == Color.PINK) {
            if (gridMap[cols][rows].positonIsEmpty()) {
               Rectangle r = gridMap[cols][rows].getRectangle();
               if (rows % 2 == 0 && cols % 2 == 0) {
                  r.setFill(Color.rgb(0, 128, 255));
               } else if (rows % 2 == 0 && cols % 2 == 1) {
                  r.setFill(Color.rgb(0, 191, 255));
               } else if (rows % 2 == 1 && cols % 2 == 0) {
                  r.setFill(Color.rgb(0, 191, 255));
               } else {
                  r.setFill(Color.rgb(0, 128, 255));
                  //  r.setFill(Color.rgb(51,153,255));
               }

            } else if (gridMap[cols][rows].hasShip() && gridMap[cols][rows].positionHasNoPortOrIsland()) {
               gridMap[cols][rows].getRectangle().setFill(gameEngine.findPlayerByShip(gridMap[cols][rows].getShip()).getShipImage());
            }
         }
      }
   }

   private int chanceCardHandler() {
      int acquiredCards = 0;
      Island treasureIsland = gameEngine.findIslandByName("Treasure Island");
      Island pirateIsland = gameEngine.findIslandByName("Pirate Island");
      ChanceCard chanceCard = treasureIsland.getTopOfThePackChanceCard();
      Player currentPlayer = gameEngine.getCurrentPlayer();
      Coordinates c = currentPlayer.getShip().getCoordinates();
      // int numCard = 23;
      int cardNumber = chanceCard.getCardNumber();
      if (cardNumber > 20 && cardNumber < 27 && cardNumber != 22) {
         currentPlayer.getChanceCards().add(chanceCard);
         alertPl.customAlert("Chance card " + chanceCard.getCardNumber() + " withdrawn !", chanceCard.getCardDescription(), new ButtonType("Keep Chance Card"));
      } else {
         alertPl.customAlert("Chance card " + chanceCard.getCardNumber() + " withdrawn !", chanceCard.getCardDescription(), new ButtonType("Use Chance Card"));

         treasureIsland.addChanceCardToTheTopOfThePack(chanceCard);
      }
      Island i;
      // Position pos;
      switch (cardNumber) {
         case 1://it is working but if there is another player on that position is not handled
            if (c.getColumn() == 7 && c.getRow() == 12) {
               changePosition(currentPlayer, gridMap[12][17]);
            } else if (c.getColumn() == 7 && c.getRow() == 11) {
               changePosition(currentPlayer, gridMap[7][6]);
            } else if (c.getColumn() == 7 && c.getRow() == 10) {
               changePosition(currentPlayer, gridMap[7][5]);
            } else if (c.getColumn() == 7 && c.getRow() == 9) {
               changePosition(currentPlayer, gridMap[7][4]);
            } else if (c.getColumn() == 7 && c.getRow() == 8) {
               changePosition(currentPlayer, gridMap[7][3]);
            } else if (c.getColumn() == 7 && c.getRow() == 7) {
               changePosition(currentPlayer, gridMap[12][2]);
            } else if (c.getColumn() == 8 && c.getRow() == 7) {
               changePosition(currentPlayer, gridMap[8][2]);
            } else if (c.getColumn() == 9 && c.getRow() == 7) {
               changePosition(currentPlayer, gridMap[9][2]);
            } else if (c.getColumn() == 10 && c.getRow() == 7) {
               changePosition(currentPlayer, gridMap[10][2]);
            } else if (c.getColumn() == 11 && c.getRow() == 7) {
               changePosition(currentPlayer, gridMap[11][2]);
            } else if (c.getColumn() == 8 && c.getRow() == 12) {
               changePosition(currentPlayer, gridMap[13][12]);
            } else if (c.getColumn() == 9 && c.getRow() == 12) {
               changePosition(currentPlayer, gridMap[14][12]);
            } else if (c.getColumn() == 10 && c.getRow() == 12) {
               changePosition(currentPlayer, gridMap[15][12]);
            } else if (c.getColumn() == 11 && c.getRow() == 12) {
               changePosition(currentPlayer, gridMap[16][12]);
            } else if (c.getColumn() == 12 && c.getRow() == 12) {
               changePosition(currentPlayer, gridMap[17][7]);
            } else if (c.getColumn() == 12 && c.getRow() == 11) {
               changePosition(currentPlayer, gridMap[12][6]);
            } else if (c.getColumn() == 12 && c.getRow() == 10) {
               changePosition(currentPlayer, gridMap[12][5]);
            } else if (c.getColumn() == 12 && c.getRow() == 9) {
               changePosition(currentPlayer, gridMap[12][4]);
            } else if (c.getColumn() == 12 && c.getRow() == 8) {
               changePosition(currentPlayer, gridMap[12][3]);
            } else if (c.getColumn() == 12 && c.getRow() == 7) {
               changePosition(currentPlayer, gridMap[17][12]);
            }
            gameEngine.setWithdrawnChanceCard(new ChanceCard(1, ""));

            if (currentPlayer.getMovementRange() < 4) {//tested working
               i = gameEngine.findIslandByName("Pirate Island");
               int cardsAcquired = 0;
               if (i != null) {
                  for (int x = 0; x < 4; ++x) {
                     if (!i.getCrewCards().isEmpty()) {
                        currentPlayer.getCards().add(i.getCrewCards().remove(0));
                        ++cardsAcquired;
                     }
                  }

               }
               alertPl.customAlert("", "Your position has been changed \n" + cardsAcquired + " Crew Cards acquired !", null);
            } else {
               alertPl.customAlert("", "Your position has been changed \n", null);
            }
            currentPlayer.getShip().setIsAtPort(false);
            break;
         case 2://tested working
            acquiredCards = 0;
            Player selectedPlayer = gameEngine.findPlayerByNickname(alertPl.selectPlayer(gameEngine.getAllPlayers(), gameEngine.getCurrentPlayer()));
            for (int x = 0; x < 3; ++x) {
               if (!selectedPlayer.getCards().isEmpty()) {
                  CrewCard card = gameEngine.getLowestValueCrewCard(selectedPlayer.getCards());
                  if (card != null) {
                     currentPlayer.getCards().add(card);
                     selectedPlayer.getCards().remove(card);
                     ++acquiredCards;
                  }
               }
            }
            alertPl.customAlert("Chance card 2", currentPlayer.getNickname() + " acquired " + acquiredCards + " crew cards !", null);
            //show the player what he has acquired
            break;
         case 3://not tested
            changePosition(currentPlayer, gridMap[0][1]);
            gameEngine.getCrewCardsFromIsland(3, 4, 3, currentPlayer, pirateIsland, alertPl);
            break;
         case 4:
            changePosition(currentPlayer, gridMap[18][19]);
            gameEngine.getCrewCardsFromIsland(4, 4, 3, currentPlayer, pirateIsland, alertPl);
            break;
         case 5:
            Port homePort = currentPlayer.getShip().getHomePort();
            switch (homePort.getName()) {
               case "London":
                  changePosition(currentPlayer, gridMap[1][13]);
                  break;
               case "Cadiz":
                  changePosition(currentPlayer, gridMap[13][18]);
                  break;
               case "Marseilles":
                  changePosition(currentPlayer, gridMap[18][6]);
                  break;
               case "Genoa":
                  changePosition(currentPlayer, gridMap[6][1]);
                  break;
            }
            int score = 0;
            if (!currentPlayer.getShip().getTreasure().isEmpty()) {
               for (Treasure tr : currentPlayer.getShip().getTreasure()) {
                  homePort.addNewTreasure(tr);
                  score += tr.getValue();
               }
               currentPlayer.getShip().getTreasure().clear();
               currentPlayer.setScore(currentPlayer.getScore() + score);
               alertPl.customAlert("Chance card 5", "Treasure with value " + score + " has been deposited to your home port ! \n Your score has been updated !", null);
            } else {
               alertPl.customAlert("Chance card 5", currentPlayer.getNickname() + " is at home port !\nNo treasure to deposit.", null);
            }
            gameEngine.getCrewCardsFromIsland(5, 4, 3, currentPlayer, pirateIsland, alertPl);
            break;
         case 6:
            Random rand = new Random();
            Port randomPort = gameEngine.getBoard().getAllPorts()[rand.nextInt(gameEngine.getBoard().getAllPorts().length - 1)];
            switch (randomPort.getName()) {
               case "London":
                  changePosition(currentPlayer, gridMap[1][13]);
                  break;
               case "Venice":
                  changePosition(currentPlayer, gridMap[1][6]);
                  break;
               case "Genoa":
                  changePosition(currentPlayer, gridMap[6][1]);
                  break;
               case "Marseilles":
                  changePosition(currentPlayer, gridMap[18][6]);
                  break;
               case "Amsterdam":
                  changePosition(currentPlayer, gridMap[18][13]);
                  break;
               case "Cadiz":
                  changePosition(currentPlayer, gridMap[13][18]);
                  break;
            }
            //      alertPl.customAlert("Chance card 6", "Your position has been changed !", null);
            gameEngine.getCrewCardsFromIsland(6, 4, 3, currentPlayer, pirateIsland, alertPl);
            break;
         case 7:
            int nearestShipDistance = 100;//some high value
            int currentShipDistance = 0;//this value doesn't matter
            boolean twoShipsSameDistance = false;
            Player nearestPlayer = null;
            int cols,
             rows;
            Coordinates curPlCoords = currentPlayer.getShip().getCoordinates();
            Coordinates loopPlayerCoord;
            for (int x = 0; x < 4; ++x) {
               if (!gameEngine.getPlayer(x).getNickname().equals(currentPlayer.getNickname())) {
                  loopPlayerCoord = gameEngine.getPlayer(x).getShip().getCoordinates();
                  cols = (curPlCoords.getColumn() > loopPlayerCoord.getColumn())
                          ? curPlCoords.getColumn() - loopPlayerCoord.getColumn()
                          : loopPlayerCoord.getColumn() - curPlCoords.getColumn();
                  rows = (curPlCoords.getRow() > loopPlayerCoord.getRow())
                          ? curPlCoords.getRow() - loopPlayerCoord.getRow()
                          : loopPlayerCoord.getRow() - curPlCoords.getRow();
                  currentShipDistance = cols + rows;
                  if (nearestShipDistance > currentShipDistance) {
                     nearestShipDistance = currentShipDistance;
                     nearestPlayer = gameEngine.getPlayer(x);
                  } else if (nearestShipDistance == currentShipDistance) {
                     twoShipsSameDistance = true;
                     break;
                  }
               }
            }
            if (!twoShipsSameDistance) {
               if (nearestPlayer != null) {
                  if (!currentPlayer.getShip().getTreasure().isEmpty() && nearestPlayer.getShip().getTreasure().size() < 2) {
                     Treasure lowestValue = gameEngine.getLowestValueTreasure(currentPlayer.getShip().getTreasure());
                     currentPlayer.getShip().getTreasure().remove(lowestValue);
                     nearestPlayer.getShip().getTreasure().add(lowestValue);
                     alertPl.customAlert("Chance card 7 ", "Treasure of type " + lowestValue.getType() + " has been washed away to the nearest ship " + nearestPlayer.getNickname() + "!", null);
                  } else {
                     int cardsAway = 0;
                     CrewCard card = null;
                     for (int x = 0; x < 2; ++x) {
                        if (!currentPlayer.getCards().isEmpty()) {
                           card = gameEngine.getLowestValueCrewCard(currentPlayer.getCards());
                           if (card != null) {
                              currentPlayer.getCards().remove(card);
                              nearestPlayer.getCards().add(card);
                              ++cardsAway;
                           }
                        }
                     }
                     alertPl.customAlert("Chance card 7", cardsAway + " of your crew cards have been washed away to the nearest ship " + nearestPlayer.getNickname() + "!", null);
                     //alert 1or  2 card has been added
                  }
               }
               //else alert there is some error
            } else {
               alertPl.customAlert("Chance card 7", currentPlayer.getNickname() + " there are two ships at the same distance from you, nothing is done !", null);
            }
            break;
         case 8:
            Island flatIsland = gameEngine.findIslandByName("Flat Island");
            if (!currentPlayer.getShip().getTreasure().isEmpty()) {
               Treasure t = gameEngine.getLowestValueTreasure(currentPlayer.getShip().getTreasure());
               currentPlayer.getShip().getTreasure().remove(t);
               flatIsland.getTreasure().add(t);
               alertPl.customAlert("Chance card 8", "Treasure of type " + t.getType() + " has been washed away to Flat Island ! ", null);
            } else if (!currentPlayer.getCards().isEmpty()) {
               int cardsAway = 0;
               for (int x = 0; x < 2; ++x) {
                  if (!currentPlayer.getCards().isEmpty()) {
                     CrewCard lowestValueCard = gameEngine.getLowestValueCrewCard(currentPlayer.getCards());
                     if (lowestValueCard != null) {
                        flatIsland.getCrewCards().add(lowestValueCard);
                        currentPlayer.getCards().remove(lowestValueCard);
                        ++cardsAway;
                     }
                  }
               }
               alertPl.customAlert("Chance card 8 ", cardsAway + " crew cards had been given away to Flat Island !", null);
            } else {
               alertPl.customAlert("Chance card 8", "You don't have any treasure or crew card to give away !", null);
            }
            break;
         case 9://tested
            flatIsland = gameEngine.findIslandByName("Flat Island");
            if (!currentPlayer.getShip().getTreasure().isEmpty()) {//tested
               Treasure highestValueTr = gameEngine.getHighestValueTreasure(currentPlayer.getShip().getTreasure());
               flatIsland.getTreasure().add(highestValueTr);
               currentPlayer.getShip().getTreasure().remove(highestValueTr);
               alertPl.customAlert("Chance card 9", "Your highest value treasure of type " + highestValueTr.getType() + " has been washed away to Flat Island !", null);
            } else if (!currentPlayer.getCards().isEmpty()) {//tested
               CrewCard highestValueCard = gameEngine.getHighestValueCrewCard(currentPlayer.getCards());
               flatIsland.getCrewCards().add(highestValueCard);
               currentPlayer.getCards().remove(highestValueCard);
               alertPl.customAlert("Chance card 9", "Your highest value crew card with value " + highestValueCard.getValue() + " has been washed away to Flat Island !", null);
            } else {
               alertPl.customAlert("Chance card 9", "You don't have any treasure or crew card to give away !", null);
            }
            break;
         case 10://best crew card goes to pirate island
            //tested
            i = gameEngine.findIslandByName("Pirate Island");
            CrewCard card = gameEngine.getHighestValueCrewCard(currentPlayer.getCards());
            //       System.err.println("Card before:" + currentPlayer.getCards().size());
            if (card != null) {
               i.getCrewCards().add(card);
               currentPlayer.getCards().remove(card);
               alertPl.customAlert("Chance card 10", "Your highest value crew card with value " + card.getValue() + " has been washed away to Flat Island !", null);

            }
            break;
         case 11:

            if (!treasureIsland.getTreasure().isEmpty() && !pirateIsland.getCrewCards().isEmpty() && currentPlayer.getShip().getTreasure().size() < 2) {
               //ask the player if he wish to take treasure or chance card
               if (alertPl.treasureOrChanceCard()) {
                  //true - treasure
                  itemTransfer.showTakeTreasurePane(treasureIsland, null, currentPlayer, 5);
               } else {
                  //false - chance card
                  itemTransfer.showTakeCrewCardPane(currentPlayer, pirateIsland, null, 2, 0);
               }
            } else if (!pirateIsland.getCrewCards().isEmpty()) {
               //let the player take crew cards
               itemTransfer.showTakeCrewCardPane(currentPlayer, pirateIsland, null, 2, 0);
            } else if (!pirateIsland.getTreasure().isEmpty() && currentPlayer.getShip().getTreasure().size() < 2) {
               itemTransfer.showTakeTreasurePane(treasureIsland, null, currentPlayer, 5);
            } else {//tell the player he cannot take any treasure or chance card
               alertPl.islandHasNoTreasureOrCrewCards(pirateIsland.getName());
            }

            break;
         case 12:   //take treasure up to 4 or 2 crew cards from pirate island

            if (!treasureIsland.getTreasure().isEmpty() && !pirateIsland.getCrewCards().isEmpty() && currentPlayer.getShip().getTreasure().size() < 2) {
               //ask the player if he wish to take treasure or chance card
               if (alertPl.treasureOrChanceCard()) {
                  //true - treasure
                  itemTransfer.showTakeTreasurePane(treasureIsland, null, currentPlayer, 4);
               } else {
                  //false - chance card
                  itemTransfer.showTakeCrewCardPane(currentPlayer, pirateIsland, null, 2, 0);
               }
            } else if (!pirateIsland.getCrewCards().isEmpty()) {
               //let the player take crew cards
               itemTransfer.showTakeCrewCardPane(currentPlayer, pirateIsland, null, 2, 0);
            } else if (!treasureIsland.getTreasure().isEmpty() && currentPlayer.getShip().getTreasure().size() < 2) {
               itemTransfer.showTakeTreasurePane(treasureIsland, null, currentPlayer, 4);
            } else {//tell the player he cannot take any treasure or chance card
               alertPl.islandHasNoTreasureOrCrewCards(pirateIsland.getName());
            }

            break;
         case 13://same as 11\
            treasureIsland.getTreasure().add(new Treasure("Pearls"));

            treasureIsland.getTreasure().add(new Treasure("Pearls"));
            //take treasure up to 5 or 2 crew cards from pirate island
            if (!treasureIsland.getTreasure().isEmpty() && !pirateIsland.getCrewCards().isEmpty() && currentPlayer.getShip().getTreasure().size() < 2) {
               //ask the player if he wish to take treasure or chance card
               if (alertPl.treasureOrChanceCard()) {
                  //true - treasure
                  itemTransfer.showTakeTreasurePane(treasureIsland, null, currentPlayer, 5);
               } else {
                  //false - chance card
                  itemTransfer.showTakeCrewCardPane(currentPlayer, pirateIsland, null, 2, 0);
               }
            } else if (!pirateIsland.getCrewCards().isEmpty()) {
               //let the player take crew cards
               itemTransfer.showTakeCrewCardPane(currentPlayer, pirateIsland, null, 2, 0);
            } else if (!treasureIsland.getTreasure().isEmpty() && currentPlayer.getShip().getTreasure().size() < 2) {
               itemTransfer.showTakeTreasurePane(treasureIsland, null, currentPlayer, 5);
            } else {//tell the player he cannot take any treasure or chance card
               alertPl.islandHasNoTreasureOrCrewCards(pirateIsland.getName());
            }

            break;
         case 14:
            //take treasure up to 7 or 3 crew cards from pirate island

            if (!treasureIsland.getTreasure().isEmpty() && !pirateIsland.getCrewCards().isEmpty() && currentPlayer.getShip().getTreasure().size() < 2) {
               //ask the player if he wish to take treasure or chance card
               if (alertPl.treasureOrChanceCard()) {
                  //true - treasure
                  itemTransfer.showTakeTreasurePane(treasureIsland, null, currentPlayer, 7);
               } else {
                  //false - chance card
                  itemTransfer.showTakeCrewCardPane(currentPlayer, pirateIsland, null, 3, 0);
               }
            } else if (!pirateIsland.getCrewCards().isEmpty()) {
               //let the player take crew cards
               itemTransfer.showTakeCrewCardPane(currentPlayer, pirateIsland, null, 3, 0);
            } else if (!treasureIsland.getTreasure().isEmpty() && currentPlayer.getShip().getTreasure().size() < 2) {
               itemTransfer.showTakeTreasurePane(treasureIsland, null, currentPlayer, 7);
            } else {//tell the player he cannot take any treasure or chance card
               alertPl.islandHasNoTreasureOrCrewCards(pirateIsland.getName());

            }
            break;
         case 15://C1 Applies - get 2 cards from the top of the pack
            // tested
            acquiredCards = 0;
            for (int x = 0; x < 2; ++x) {
               if (!pirateIsland.getCrewCards().isEmpty()) {
                  currentPlayer.getCards().add(pirateIsland.getCrewCards().remove(0));
                  ++acquiredCards;
               }
            }
            if (acquiredCards > 0) {
               alertPl.customAlert("Chance card 15", acquiredCards + " crew cards has been acquired !", null);
            } else {
               alertPl.customAlert("Chance card 15", pirateIsland.getName() + " has no crew cards.\nNo cards acquired !", null);
            }
            break;
         case 16:// tested

            if (!treasureIsland.getTreasure().isEmpty()) {
               itemTransfer.showTakeTreasurePane(treasureIsland, null, currentPlayer, 7);
            } else {
               alertPl.islandHasNoTreasureOrCrewCards(treasureIsland.getName());
            }
            int removedCrew = 0;
            while (currentPlayer.getMovementRange() > 10) {// tested
               CrewCard lowestCrew = gameEngine.getLowestValueCrewCard(currentPlayer.getCards());
               currentPlayer.getCards().remove(lowestCrew);
               currentPlayer.updatePlayerStat();
               removedCrew += lowestCrew.getValue();
            }
            if (removedCrew > 0) {
               alertPl.customAlert("Chance card 16", removedCrew + " members of your crew were placed on Pirate Island !", null);
            }
            break;
         case 17:// tested

            if (!treasureIsland.getTreasure().isEmpty()) {
               itemTransfer.showTakeTreasurePane(treasureIsland, null, currentPlayer, 6);
            } else {
               alertPl.islandHasNoTreasureOrCrewCards(treasureIsland.getName());
            }
            removedCrew = 0;
            while (currentPlayer.getMovementRange() > 11) {// tested
               CrewCard lowestCrew = gameEngine.getLowestValueCrewCard(currentPlayer.getCards());
               currentPlayer.getCards().remove(lowestCrew);
               currentPlayer.updatePlayerStat();
               removedCrew += lowestCrew.getValue();
            }
            if (removedCrew > 0) {
               alertPl.customAlert("Chance card 17", removedCrew + " members of your crew were placed on Pirate Island !", null);
            }
            break;
         case 18:// tested

            if (!treasureIsland.getTreasure().isEmpty()) {
               itemTransfer.showTakeTreasurePane(treasureIsland, null, currentPlayer, 4);
            } else {
               alertPl.islandHasNoTreasureOrCrewCards(treasureIsland.getName());
            }
            acquiredCards = 0;
            if (currentPlayer.getMovementRange() < 8) {
               for (int x = 0; x < 2; ++x) {
                  if (!pirateIsland.getCrewCards().isEmpty()) {
                     currentPlayer.getCards().add(pirateIsland.getCrewCards().remove(0));
                     ++acquiredCards;
                  }
               }
               if (acquiredCards > 0) {
                  alertPl.customAlert("Chance card 15", acquiredCards + " crew cards has been acquired !", null);
               } else {
                  alertPl.customAlert("Chance card 15", pirateIsland.getName() + " has no crew cards.\nNo cards acquired !", null);
               }
            }

            break;
         case 19://tested
            int exchangeCards = currentPlayer.getCards().size();
            for (int x = 0; x < exchangeCards; ++x) {
               pirateIsland.getCrewCards().add(pirateIsland.getCrewCards().size(), currentPlayer.getCards().remove(0));
            }
            for (int x = 0; x < exchangeCards; ++x) {
               currentPlayer.getCards().add(pirateIsland.getCrewCards().remove(0));
            }
            alertPl.customAlert("Chance card 19", exchangeCards + " exchanged with " + pirateIsland.getName(), null);
            break;
         case 20:
            List<Coordinates> adjacentPositions;
            ArrayList<Player> playersAtTreasureIsland = new ArrayList();
            for (int x = 0; x < 4; ++x) {
               Player p = gameEngine.getPlayer(x);
               if (!p.getNickname().equals(currentPlayer.getNickname())) {
                  adjacentPositions = gameEngine.getAdjacentCoordinates(p.getShip().getCoordinates());
                  for (Coordinates coords : adjacentPositions) {
                     if (!gridMap[coords.getColumn()][coords.getRow()].hasIsland()) {
                        if (gridMap[coords.getColumn()][coords.getRow()].getIsland().getName().equals("Treasure Island")) {
                           playersAtTreasureIsland.add(p);
                        }
                     }

                  }
               }
            }
            if (playersAtTreasureIsland.isEmpty()) {
               i = gameEngine.findIslandByName("Pirate Island");
               if (i != null) {
                  int removedCards = 0;
                  for (int x = 0; x < 2; ++x) {
                     if (!currentPlayer.getCards().isEmpty()) {
                        CrewCard lowestcard = gameEngine.getLowestValueCrewCard(currentPlayer.getCards());
                        if (lowestcard != null) {
                           currentPlayer.getCards().remove(lowestcard);
                           i.getCrewCards().add(lowestcard);
                           ++removedCards;
                        }
                     }
                  }
                      alertPl.customAlert("Chance card 20", removedCards + " crew cards has been washed away to Pirate Island !", null);
           
                  //alert player how many cards are removed;
               }
            } else if (playersAtTreasureIsland.size() >0) {
               int removedCards=0;
               rand = new Random();
               Player p = playersAtTreasureIsland.get(0);
               CrewCard randomlyRemovedCard;
               for (int x = 0; x < 2; ++x) {
                  if (!p.getCards().isEmpty() && !currentPlayer.getCards().isEmpty()) {
                     randomlyRemovedCard = p.getCards().get(rand.nextInt(p.getCards().size()));
                     p.getCards().remove(randomlyRemovedCard);
                     currentPlayer.getCards().add(randomlyRemovedCard);

                     randomlyRemovedCard = currentPlayer.getCards().get(rand.nextInt(currentPlayer.getCards().size()));
                     currentPlayer.getCards().remove(randomlyRemovedCard);
                     p.getCards().add(randomlyRemovedCard);
                     ++removedCards;
                  }
               }
               alertPl.customAlert("Chance card 20", currentPlayer.getNickname() + " took " + removedCards + " crew cards from " + p.getNickname() + " !", null);
            } else if (playersAtTreasureIsland.size() > 1) {
               //let the user select which player's cards
            }
            break;
         case 21:
            // currentPlayer.getChanceCards().add(new ChanceCard(21, ""));
            break;
         case 22:// tested
            int playersLost = 0;
            for (int x = 0; x < 4; ++x) {
               Player pl = gameEngine.getPlayer(x);
               if (pl.getCards().size() > 7) {
                  ++playersLost;
               }
               while (pl.getCards().size() > 7) {
                  CrewCard lowestCard = gameEngine.getLowestValueCrewCard(pl.getCards());
                  if (lowestCard != null) {
                     pl.getCards().remove(lowestCard);
                     pirateIsland.getCrewCards().add(lowestCard);
                  }
               }
            }
            alertPl.customAlert("Chance card 22", playersLost + " players who had more than 7 crew cards lost crew !", null);
            break;
         case 23:
            //currentPlayer.getChanceCards().add(new ChanceCard(23, "23"));

            break;
         case 24:
            //    currentPlayer.getChanceCards().add(new ChanceCard(24, "24"));

            break;
         case 25:
            //    currentPlayer.getChanceCards().add(new ChanceCard(25, "25"));
            break;
         case 26:
            //   currentPlayer.getChanceCards().add(new ChanceCard(26, "26"));
            break;
         case 27://tested

            if (!treasureIsland.getTreasure().isEmpty() && !pirateIsland.getCrewCards().isEmpty() && currentPlayer.getShip().getTreasure().size() < 2) {
               //ask the player if he wish to take treasure or chance card
               if (alertPl.treasureOrChanceCard()) {
                  //true - treasure
                  itemTransfer.showTakeTreasurePane(treasureIsland, null, currentPlayer, 5);
               } else {
                  //false - chance card
                  itemTransfer.showTakeCrewCardPane(currentPlayer, pirateIsland, null, 3, 0);
               }
            } else if (!pirateIsland.getCrewCards().isEmpty()) {
               //let the player take crew cards
               itemTransfer.showTakeCrewCardPane(currentPlayer, pirateIsland,
                       null, 3, 0);
            } else if (!treasureIsland.getTreasure().isEmpty() && currentPlayer.getShip().getTreasure().size() < 2) {
               itemTransfer.showTakeTreasurePane(treasureIsland, null, currentPlayer, 5);
            } else {//tell the player he cannot take any treasure or chance card
               alertPl.islandHasNoTreasureOrCrewCards(pirateIsland.getName());
            }

            break;
         case 28:// tested

            if (pirateIsland != null) {
               int cardsRemoved = 0;
               for (int x = 0; x < 2; ++x) {
                  if (!pirateIsland.getCrewCards().isEmpty()) {
                     currentPlayer.getCards().add(pirateIsland.getCrewCards().get(0));
                     pirateIsland.getCrewCards().remove(0);
                     ++cardsRemoved;
                  }

               }
               if (cardsRemoved == 0) {
                  alertPl.customAlert("Chance Card 28", "Pirate Island has no crew cards !", null);
               } else {
                  alertPl.customAlert("Chance Card 28", "You have acquired " + cardsRemoved + " from Pirate Island  !", null);
               }
            }
      }
      return 1;
   }

   private void changePosition(Player p, Position newPos) {
      Coordinates oldCoords = p.getShip().getCoordinates();
      //remove the ship from its old position
      Position oldPos = gridMap[oldCoords.getColumn()][oldCoords.getRow()];
      oldPos.removeShipFromCurrentPos();

      //reset the color of the old position
      if (oldPos.positonIsEmpty()) {
         int cols = oldCoords.getColumn();
         int rows = oldCoords.getRow();
         if (rows % 2 == 0 && cols % 2 == 0) {
            oldPos.getRectangle().setFill(Color.rgb(0, 128, 255));
         } else if (rows % 2 == 0 && cols % 2 == 1) {
            oldPos.getRectangle().setFill(Color.rgb(0, 191, 255));
         } else if (rows % 2 == 1 && cols % 2 == 0) {
            oldPos.getRectangle().setFill(Color.rgb(0, 191, 255));
         } else {
            oldPos.getRectangle().setFill(Color.rgb(0, 128, 255));
         }
         // grid_map[oldCoords.getColumn()][oldCoords.getRow()].getRectangle().setFill(Color.LIGHTBLUE);
      }
      //the new position code
      //newPos.getRectangle().setFill(Color.BLUEVIOLET);//indicate with color that there is a player

      newPos.setShip(p.getShip());//set the ship to the new position
      p.getShip().setCoordinates(newPos.getCoordinates()); //update the coordinates of the ship

   }

   private void positionClicked(Position newPosition) {
      //there is not a present ship battle
      if (!gameEngine.gameIsInBattleMode()) {
         if (gameEngine.hasStarted()) {//if game has started
            if (gameEngine.getCurrentPlayer().getShip().getCoordinates() != newPosition.getCoordinates()) {
               if (gameEngine.getShipBattleLooser() == null) {//if there is not a ship battle
                  Player currPlayer = gameEngine.getCurrentPlayer();

                  if (!gameEngine.hasMoved()) {//if the player hasn't already moved
                     if (gameEngine.getShipBattleWinner() != null) {
                        if (positionsToMove.isEmpty()) {
                           Player looser = gameEngine.getShipBattleWinner();
                           //calculate again the rotation
                           int cols = looser.getShip().getCoordinates().getColumn() - looser.getShip().getShipTurn().getColumn();//-looser.getShip().getCoordinates().getColumn();
                           int rows = looser.getShip().getCoordinates().getRow() - looser.getShip().getShipTurn().getRow();//-looser.getShip().getCoordinates().getRow();
                           if (cols > 0) {
                              cols = 1;
                           }
                           if (cols < 0) {
                              cols = -1;
                           }
                           if (rows > 0) {
                              rows = 1;
                           }
                           if (rows < 0) {
                              rows = -1;
                           }
                           // System.err.println("rotation" + "  " + cols + "- " + rows);
                           looser.getShip().setShipTurn(new Coordinates(looser.getShip().getCoordinates().getColumn() + cols, looser.getShip().getCoordinates().getRow() + rows));
                           Random rand = new Random();
                           //  while (showPos(looser) < 1) {
                           looser.getShip().setShipTurn(new Coordinates(looser.getShip().getCoordinates().getColumn() + rand.nextInt(2) - 1, looser.getShip().getCoordinates().getRow() + rand.nextInt(2) - 1));

                           // }
                           
                           alertPl.customAlert("Error", "Unfortunately " + looser.getNickname() + " ship rotation has been lost.\nRandom rotation will be generated.", ButtonType.CLOSE);
                           //  System.err.println("showing again" + showPos(looser));

                           gameEngine.setShipBattleWinner(null);
                        }
                     }

                     if (positionsToMove.contains(newPosition)) {

                        if (!newPosition.hasShip()) {
                           checkForOtherShipsOnTheWay(newPosition);
                           if (!gameEngine.gameIsInBattleMode()) {
                              if (!checkIfPlayerHasBeenAtPortAndNewPosIsAtPort(currPlayer, newPosition)) {

                                 changePosition(currPlayer, newPosition);
                                 removeHighlightedPositions();

                                 checkAdjacentPositions(newPosition, currPlayer);
                                 if (currPlayer.getShip().isAtPort()) {

                                    endTurn.setDisable(false);
                                 } else {
                                    gameEngine.setHasMoved();
                                    gameEngine.setPlayerChoseSide(false);
                                 }
                              } else {
                                 alertPl.customAlert("You cannot be at port or at island two moves in a row", "Please select another position !", null);
                              }
                           }
                        } else if (newPosition.hasShip()) {
                           Player plUnderAttack = gameEngine.findPlayerByShip(newPosition.getShip());
                           if (checkIfPlayerHasBeenAtPortAndNewPosIsAtPort(plUnderAttack, newPosition)) {
                              alertPl.customAlert("Invalid move !", "You cannot attack a player, landed on a port or island!", null);

                           } else {
                              //if the player select select new position nothing will happen - he will just select a new position
                              if (alertPl.warnForFight(plUnderAttack)) {
                                 if (currPlayer.getFightPower() != plUnderAttack.getFightPower()) {
                                    //assign something it will be changed anyway
                                    //check who the winner is
                                    Ship s = currPlayer.getShip();//keep reference to player's ship - make the code shorter and readable
                                    //Remove the ship from old its old position
                                    gridMap[s.getCoordinates().getColumn()][s.getCoordinates().getRow()].removeShipFromCurrentPos();
                                    //clear the color
                                    //set new coordinates to the ship
                                    s.setCoordinates(plUnderAttack.getShip().getCoordinates());
                                    removeHighlightedPositions();
                                    gameEngine.shipBattle(currPlayer, plUnderAttack);
                                    positionClicked(null);

                                 } else {
                                    alertPl.customAlert("Fighting", "Both players have equal fight power.\n There was no fight. Please select another position.", null);
                                 }

                              }
                           }
                        }
                     } else {
                        alertPl.alertPositionNotAvailable();
                     }

                     // turn the ship
                  } else if (gameEngine.hasMoved() && !gameEngine.ifPlayerChoseSide()) {
                     if (positionsToMove.contains(newPosition)) {//make the adjacent squares pink
                        currPlayer.getShip().setShipTurn(newPosition.getCoordinates());
                        removeHighlightedPositions();

                        gameEngine.setPlayerChoseSide(true);
                        endTurn.setDisable(false);
                        positionsToMove.clear();
                     } else {
                        alertPl.alertPositionNotAvailable();
                     }
                  } else {
                     alertPl.playerAlreadymoved();
                  }

                  //if there is a playr who has lost a ship battle let him move.
               }
            } else {
               if (!gameEngine.getCurrentPlayer().getShip().isAtPort()) {

                  gameEngine.setHasMoved();
                  gameEngine.setPlayerChoseSide(false);
                  removeHighlightedPositions();
                  checkAdjacentPositions(newPosition, gameEngine.getCurrentPlayer());
               } else {
                  alertPl.samePositionClicked();
               }
            }
         } else {
            alertPl.gameHasNotStarted();
         }
      }
      if (!gameEngine.gameIsInBattleMode()) {
         if (gridMap[gameEngine.getCurrentPlayer().getShip().getCoordinates().getColumn()][gameEngine.getCurrentPlayer().getShip().getCoordinates().getRow()].positionHasNoPortOrIsland()) {
            updatePlayerGridImage(gameEngine.getCurrentPlayer());
         }
         if (gameEngine.getShipBattleLooser() != null && positionsToMove.isEmpty()) {
            removeHighlightedPositions();
            //   System.err.println("removing");
         }

         //this is the buf fix of chance card 1
         if (gameEngine.getWithdrawnChanceCard() != null && gameEngine.getWithdrawnChanceCard().getCardNumber() == 1) {
            removeHighlightedPositions();
            gameEngine.getCurrentPlayer().getShip().setIsAtPort(false);
            //    gameEngine.getCurrentPlayer().getShip().setShipTurn(
            //          new Coordinates(gameEngine.getCurrentPlayer().getShip().getCoordinates().getColumn() - 1, gameEngine.getCurrentPlayer().getShip().getCoordinates().getRow()));
            gameEngine.setHasMoved();
            gameEngine.setPlayerChoseSide(false);
            checkAdjacentPositions(gridMap[gameEngine.getCurrentPlayer().getShip().getCoordinates().getColumn()][gameEngine.getCurrentPlayer().getShip().getCoordinates().getRow()],
                    gameEngine.getCurrentPlayer());
            // showPos(gameEngine.getCurrentPlayer());
            gameEngine.setWithdrawnChanceCard(null);
         }

      }
      if (gameEngine.gameIsInBattleMode()) {
         //if (gameEngine.getShipFightHasWinner()) {
         if (gameEngine.getShipBattleLooser() != null) {
            Player looser = gameEngine.getShipBattleLooser();
            if (!gameEngine.ifLooserSeenThePos()) {
               looser.getShip().setIsAtPort(false);
               //    removeHighlightedPositions();
               //   System.err.println("aaa");
               //  showAllPlayersOnTheGrid();
               showPos(looser);
               if (positionsToMove.isEmpty()) {
                  //calculate again the rotation
                  int cols = looser.getShip().getCoordinates().getColumn() - looser.getShip().getShipTurn().getColumn();//-looser.getShip().getCoordinates().getColumn();
                  int rows = looser.getShip().getCoordinates().getRow() - looser.getShip().getShipTurn().getRow();//-looser.getShip().getCoordinates().getRow();
                  if (cols > 0) {
                     cols = 1;
                  }
                  if (cols < 0) {
                     cols = -1;
                  }
                  if (rows > 0) {
                     rows = 1;
                  }
                  if (rows < 0) {
                     rows = -1;
                  }
                  // System.err.println("rotation" + "  " + cols + "- " + rows);
                  looser.getShip().setShipTurn(new Coordinates(looser.getShip().getCoordinates().getColumn() + cols, looser.getShip().getCoordinates().getRow() + rows));
                  Random rand = new Random();
                  while (showPos(looser) < 1) {
                     looser.getShip().setShipTurn(new Coordinates(looser.getShip().getCoordinates().getColumn() + rand.nextInt(2) - 1, looser.getShip().getCoordinates().getRow() + rand.nextInt(2) - 1));

                  }
                  alertPl.customAlert("Error", "Unfortunately " + looser.getNickname() + " ship rotation has been lost.\nRandom rotation will be generated.", ButtonType.CLOSE);
                  //  System.err.println("showing again" + showPos(looser));

                  gameEngine.setLooserHasSeenThePos(true);
               } else if (positionsToMove.contains(newPosition)) {
                  gameEngine.setLooserHasSeenThePos(true);
               }

            } else if (gameEngine.ifLooserSeenThePos() && !gameEngine.isShipBattleLooserMoved() && positionsToMove.contains(newPosition)) {
               //  System.err.println("inside");
               changePosition(looser, newPosition);
               removeHighlightedPositions();
               gameEngine.setShipBattleLooserMooved(true);
               checkAdjacentPositions(newPosition, looser);
            } else if (gameEngine.ifLooserSeenThePos() && gameEngine.isShipBattleLooserMoved() && !gameEngine.looserHasTurened() && positionsToMove.contains(newPosition)) {
               //  System.err.println("save rotation");
               looser.getShip().setShipTurn(newPosition.getCoordinates());
               gameEngine.setLooserHasTurned(true);
               removeHighlightedPositions();
               gameEngine.setGameInBattleMode(false);
               endTurn.setDisable(false);
               gameEngine.setShipBattleLooser(null);

               gameEngine.setHasMoved();
               gameEngine.setPlayerChoseSide(false);

            } else if (gameEngine.ifLooserSeenThePos() && gameEngine.isShipBattleLooserMoved() && gameEngine.looserHasTurened()) {
               //System.err.println("done");
               endTurn.setDisable(false);

               gameEngine.setHasMoved();
               gameEngine.setPlayerChoseSide(false);
               for (int x = 0; x < 4; ++x) {
                  Player p = gameEngine.getPlayer(x);
                  Coordinates c = p.getShip().getCoordinates();
                  gridMap[c.getColumn()][c.getRow()].setShip(p.getShip());
               }
            }
         }

      }
      if (gameEngine.getShipBattleWinner() != null) {
        // System.err.println("not null");
      }

      setPlayersToTheBoard();
      showAllPlayersOnTheGrid();
   }

   private void setPlayersToTheBoard() {
      for (int x = 0; x < 4; ++x) {
         Player pl = gameEngine.getPlayer(x);
         Coordinates c = pl.getShip().getCoordinates();
         gridMap[c.getColumn()][c.getRow()].setShip(pl.getShip());
      }
   }

   private void checkAdjacentPositions(Position pos, Player p) {
      List<Coordinates> adjacentPositions = gameEngine.getAdjacentCoordinates(pos.getCoordinates());
      //check for chance card 25 and 26 here before every other check 
      //otherwise something might get wrong
      if (!p.getChanceCards().isEmpty()) {
         ChanceCard c = null;
         if ((p.getShip().getCoordinates().getColumn() == 18 && p.getShip().getCoordinates().getRow() == 0)
                 || (p.getShip().getCoordinates().getColumn() == 19 && p.getShip().getCoordinates().getRow() == 1)) {
            for (ChanceCard chCards : p.getChanceCards()) {
               if (chCards.getCardNumber() == 25 || chCards.getCardNumber() == 26) {
                  itemTransfer.showTakeTreasurePane(gameEngine.findIslandByName("Treasure Island"), null, p, 7);
                  c = chCards;
               }
            }
            if (c != null) {
               p.getChanceCards().remove(c);
               gameEngine.findIslandByName("Treasure Island").addChanceCardToTheTopOfThePack(c);
            }
         }
      }
      boolean isAtPort = false;
      Position adjPos;
      for (Coordinates c : adjacentPositions) {
         adjPos = gridMap[c.getColumn()][c.getRow()];
         if (!adjPos.positionHasNoPortOrIsland()) {
            isAtPort = true;

            if (!adjPos.ifPositionHasAPort()) {
               if (gameEngine.getCurrentPlayer().getShip().getHomePort().getName().equals(adjPos.getPort().getName())) {
                  checkForWinner(p);
                  gameEngine.playerAtHomePort(gameEngine.getCurrentPlayer(), alertPl);
                  checkForWinner(p);//just in case
               } else {
                  //         System.out.println("the player is at town");
                  if (!gameEngine.getCurrentPlayer().getChanceCards().isEmpty()) {
                     if (!alertPl.useChanceCardOrTrade()) {
                        trading.setNewTrade(gameEngine.getCurrentPlayer(), adjPos.getPort());
                     } else {

                        ChanceCard chanceCard = gameEngine.getCurrentPlayer().getChanceCards().get(0);
                        switch (chanceCard.getCardNumber()) {
                           case 21:
                              if (alertPl.chanceCardUsageConfirmation(chanceCard.getCardNumber(), "21")) {
                                 itemTransfer.showTakeCrewCardPane(gameEngine.getCurrentPlayer(), null, adjPos.getPort(), 0, 5);
                                 removeUsedChanceCard(21, p.getChanceCards());
                              }
                              break;

                           case 23:
                              if (alertPl.chanceCardUsageConfirmation(chanceCard.getCardNumber(), "23")) {

                                 if (!adjPos.getPort().getTreasure().isEmpty() && !adjPos.getPort().getCrewCards().isEmpty() && p.getShip().getTreasure().size() < 2) {
                                    //ask the player if he wish to take treasure or chance card
                                    if (alertPl.treasureOrChanceCard()) {
                                       //true - treasure
                                       itemTransfer.showTakeTreasurePane(null, adjPos.getPort(), p, 5);
                                    } else {
                                       //false - chance card
                                       itemTransfer.showTakeCrewCardPane(p, null, adjPos.getPort(), 0, 5);
                                    }
                                 } else if (!adjPos.getPort().getCrewCards().isEmpty()) {
                                    //let the player take crew cards
                                    itemTransfer.showTakeCrewCardPane(p, null, adjPos.getPort(), 0, 5);
                                 } else if (!adjPos.getPort().getTreasure().isEmpty() && p.getShip().getTreasure().size() < 2) {
                                    itemTransfer.showTakeTreasurePane(null, adjPos.getPort(), p, 5);
                                 } else {//tell the player he cannot take any treasure or chance card
                                    alertPl.islandHasNoTreasureOrCrewCards(adjPos.getPort().getName());
                                 }
                                 removeUsedChanceCard(23, p.getChanceCards());
                              }
                              break;
                           case 24:
                              if (alertPl.chanceCardUsageConfirmation(chanceCard.getCardNumber(), "23")) {

                                 if (!adjPos.getPort().getTreasure().isEmpty() && !adjPos.getPort().getCrewCards().isEmpty() && p.getShip().getTreasure().size() < 2) {
                                    //ask the player if he wish to take treasure or chance card
                                    if (alertPl.treasureOrChanceCard()) {
                                       //true - treasure
                                       itemTransfer.showTakeTreasurePane(null, adjPos.getPort(), p, 4);
                                    } else {
                                       //false - chance card
                                       itemTransfer.showTakeCrewCardPane(p, null, adjPos.getPort(), 0, 4);
                                    }
                                 } else if (!adjPos.getPort().getCrewCards().isEmpty()) {
                                    //let the player take crew cards
                                    itemTransfer.showTakeCrewCardPane(p, null, adjPos.getPort(), 0, 4);
                                 } else if (!adjPos.getPort().getTreasure().isEmpty() && p.getShip().getTreasure().size() < 2) {
                                    itemTransfer.showTakeTreasurePane(null, adjPos.getPort(), p, 4);
                                 } else {//tell the player he cannot take any treasure or chance card
                                    alertPl.islandHasNoTreasureOrCrewCards(adjPos.getPort().getName());
                                 }
                                 removeUsedChanceCard(24, p.getChanceCards());
                              }
                              break;

                        }
                     }
                  } else {
                     trading.setNewTrade(gameEngine.getCurrentPlayer(), adjPos.getPort());
                  }
               }
               //check for chance cards

            } else {
               switch (adjPos.getIsland().getName()) {
                  case "Treasure Island":
                     chanceCardHandler();

                     break;
                  case "Flat Island":
                     Island i = gameEngine.findIslandByName("Flat Island");
                     int treasureAqcuired = 0;
                     if (p.getShip().getTreasure().size() < 2) {
                        if (!i.getTreasure().isEmpty()) {
                           for (int x = 0; x < 2; ++x) {
                              if (!i.getTreasure().isEmpty() && p.getShip().getTreasure().size() < 2) {
                                 p.getShip().getTreasure().add(i.getTreasure().remove(0));
                                 ++treasureAqcuired;
                              }
                           }
                           alertPl.customAlert(p.getNickname() + " at Flat Island", p.getNickname() + " acquired " + treasureAqcuired + " treasure !", null);
                        } else {
                           alertPl.customAlert(p.getNickname() + " at Flat Island", "Currently there is no treasure on Flat Island", null);
                        }
                     } else {
                        alertPl.customAlert(p.getNickname() + " at Flat Island", "Player does not have enough space on its ship", null);
                     }
                     break;
                  case "Anchor Bay":
                     break;
               }

            }
            break;
         }
      }
      p.getShip().setIsAtPort(isAtPort);
      if (!isAtPort) {//if is not at port let him turn the ship
         int cols = 0, rows = 0;
         positionsToMove.clear();
         for (Coordinates c : adjacentPositions) {
            positionsToMove.add(gridMap[c.getColumn()][c.getRow()]);
            cols = c.getColumn() - p.getShip().getCoordinates().getColumn();
            rows = c.getRow() - p.getShip().getCoordinates().getRow();

            ImagePattern image = null;

            switch (cols) {
               case -1:
                  switch (rows) {
                     case -1:
                        image = new ImagePattern(new Image("uk/ac/aber/cs221/group13/externalfiles/images/rotation/arrows/sw.png"));
                        break;
                     case 0:
                        image = new ImagePattern(new Image("uk/ac/aber/cs221/group13/externalfiles/images/rotation/arrows/w.png"));
                        break;
                     case 1:
                        image = new ImagePattern(new Image("uk/ac/aber/cs221/group13/externalfiles/images/rotation/arrows/nw.png"));
                        break;
                  }
                  break;
               case 0:
                  switch (rows) {
                     case -1:
                        image = new ImagePattern(new Image("uk/ac/aber/cs221/group13/externalfiles/images/rotation/arrows/s.png"));
                        break;
                     case 1:
                        image = new ImagePattern(new Image("uk/ac/aber/cs221/group13/externalfiles/images/rotation/arrows/n.png"));
                        break;
                  }
                  break;
               case 1:
                  switch (rows) {
                     case -1:
                        image = new ImagePattern(new Image("uk/ac/aber/cs221/group13/externalfiles/images/rotation/arrows/se.png"));
                        break;
                     case 0:
                        image = new ImagePattern(new Image("uk/ac/aber/cs221/group13/externalfiles/images/rotation/arrows/e.png"));
                        break;
                     case 1:
                        image = new ImagePattern(new Image("uk/ac/aber/cs221/group13/externalfiles/images/rotation/arrows/ne.png"));
                        break;
                  }
                  break;
            }

            gridMap[c.getColumn()][c.getRow()].getRectangle().setFill(image);

         }
      }
   }

   private void updatePlayerGridImage(Player p) {
      for (int x = 0; x < 4; ++x) {
         if (gameEngine.getPlayer(x).getNickname().equals(p.getNickname())) {
            int cols = p.getShip().getShipTurn().getColumn() - p.getShip().getCoordinates().getColumn();
            int rows = p.getShip().getShipTurn().getRow() - p.getShip().getCoordinates().getRow();
            p.updatePlayerImage(cols, rows, x);
            gridMap[p.getShip().getCoordinates().getColumn()][p.getShip()
                    .getCoordinates().getRow()].getRectangle().setFill(p.getShipImage());
            break;
         }
      }
   }

   private Node getTopPane() {

      GridPane container = new GridPane();

      //left buttons here
      Button legend = new Button("Legend");
      legend.setOnMouseClicked(e -> {
         if (gameLegend != null) {
            if (gameLegend.isVisible()) {
               gameLegend.setVisible(false);
            } else {
               gameLegend.setVisible(true);
            }
         } else {
            gameLegend = gameLegend();
            ((VBox) bp.getCenter()).getChildren().add(gameLegend);
            double w = legend.prefWidth(-1);
            double h = legend.prefHeight(w);

            gameLegend.resizeRelocate(250.5, 83.5, w, h);
            gameLegend.setPrefSize(300, 400);
            gameLegend.setMinSize(300, 400);
            gameLegend.autosize();
            gameLegend.setVisible(true);
         }

      });

      Button rules = new Button("Rules");
      rules.setOnMouseClicked(e -> {

         if (this.rules.getMainPane().isVisible()) {
            this.rules.getMainPane().setVisible(false);
         } else {
            this.rules.gameRules();
            this.rules.getMainPane().setVisible(true);
         }
      });

      Button chanceCardReference = new Button("Chance Card References");
      chanceCardReference.setOnMouseClicked(e -> {
         if (this.rules.getMainPane().isVisible()) {
            this.rules.getMainPane().setVisible(false);
         } else {
            this.rules.chanceCardReferences();
            this.rules.getMainPane().setVisible(true);
         }
      }
      );

      VBox leftButtons = new VBox(10);
      leftButtons.setAlignment(Pos.CENTER);
      leftButtons.setMinWidth(200.0);
      leftButtons.getChildren().addAll(rules, chanceCardReference, legend);

      GridPane playerCards = new GridPane();
      int numCards = 0;
      int cardColor, cardVal;

      for (CrewCard c : gameEngine.getCurrentPlayer().getCards()) {
         cardColor = (c.isBlack()) ? 1 : 0;
         cardVal = c.getValue();
         Image cardImg = null;

         switch (cardColor) {
            case 0://red
               switch (cardVal) {
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
               switch (cardVal) {
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
         playerCards.add(new ImageView(cardImg), numCards, 0);
         ++numCards;
      }

      playerCards.setPadding(new Insets(0.0, 10.0, 0.0, 0.0));
      gameEngine.getCurrentPlayer().updatePlayerStat();
      Label playerFightPower = new Label("Fight Power: " + gameEngine.getCurrentPlayer().getFightPower());
      playerFightPower.setPadding(new Insets(0, 0, 0, 50));
      if (!gameEngine.hasStarted()) {
         playerCards.getChildren().clear();
         playerFightPower.setText("");
      }
      playerCards.add(playerFightPower, numCards, 0);

      //player info here
      playerCards.setHgap(5.0);
      HBox playerInfo = new HBox();
      for (int x = 0; x < 4; ++x) {
         playerInfo.getChildren().add(topPanePlayerInfo(x));
      }
      //start game end turn buttons
      VBox topRightButtons = new VBox(10);
      topRightButtons.setAlignment(Pos.CENTER);

      if (gameEngine.hasStarted()) {
         topRightButtons.setPadding(new Insets(30, 0, 0, 25));
         topRightButtons.getChildren().add(endTurn);
      } else {
         topRightButtons.setPadding(new Insets(10, 0, 0, 25));
         topRightButtons.getChildren().addAll(startGame, endTurn);
      }
      //add the different Nodes to the grid pane
      container.add(leftButtons, 0, 0);
      container.add(playerInfo, 1, 0);
      container.add(topRightButtons, 2, 0);
      //   container.add(playerCards, 1, 1);

      VBox cadizPort = new VBox(0);
      if (gameEngine.hasStarted()) {
         cadizPort.setPadding(new Insets(0, 0, 0, 175));
      } else {
         cadizPort.setPadding(new Insets(0, 0, 0, 400));
      }
      HBox cadizPlayer = new HBox(5);
      HBox cadizPlayerName = new HBox(5);
      HBox cadizPortName = new HBox(5);
      cadizPortName.getChildren().addAll(new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/boardicons/port.png")), new Label("Port of Cadiz"));
      cadizPlayerName.getChildren().addAll(returnPlayerIcon(gameEngine.returnPlayerByPort("Cadiz").getNickname()),
              new Label(gameEngine.returnPlayerByPort("Cadiz").getNickname()));
      Port p = gameEngine.findPortByName("Cadiz");
      cadizPort.getChildren().addAll(cadizPortName, playerCards, cadizPlayerName, generatePortItemsIcons(p.getTreasure(), p.getCrewCards()));
      cadizPlayer.getChildren().addAll(playerCards, cadizPort);

      container.add(cadizPlayer, 1, 1);
      return container;
   }

   private void showAllPlayersOnTheGrid() {
      Coordinates currPlayerCoords;
      for (int x = 0; x < 4; ++x) {
         currPlayerCoords = gameEngine.getPlayer(x).getShip().getCoordinates();
         if (gridMap[currPlayerCoords.getColumn()][currPlayerCoords.getRow()].positionHasNoPortOrIsland()) {
            gridMap[currPlayerCoords.getColumn()][currPlayerCoords.getRow()].getRectangle().setFill(gameEngine.getPlayer(x).getShipImage());
         }
      }
   }

   private void setupHoveringPane(HoverPosition obj) {
      Board b = gameEngine.getBoard();
      for (int x = 0; x < 6; ++x) {
         Island i = b.getIsland(x);
         Port p = b.getPort(x);
         i.getCoordinates().forEach((c) -> {
            gridMap[c.getColumn()][c.getRow()].getRectangle().hoverProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean show) -> {
               obj.updateGrid(i, null);
               if (show) {
                  double w = hoverPosition.prefWidth(-1);
                  double h = hoverPosition.prefHeight(w);
                  hoverPosition.resizeRelocate(gridMap[c.getColumn()][c.getRow()].getRectangle().getLayoutX(), gridMap[c.getColumn()][c.getRow()].getRectangle().getLayoutY(), w, h);
                  hoverPosition.setVisible(true);
                  hoverPosition.setManaged(false);
                  hoverPosition.setBorder(new Border(new BorderStroke(Color.BLACK,
                          BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                  switch (i.getName()) {
                     case "Flat Island":
                        hoverPosition.resizeRelocate(180, 40, w, h);
                        break;
                     case "Cliff Creek":
                        hoverPosition.resizeRelocate(500, 50, w, h);
                        break;
                     case "Pirate Island":
                        hoverPosition.resizeRelocate(360, 500, w, h);
                        break;
                     case "Mud Bay":
                        hoverPosition.resizeRelocate(50, 500, w, h);
                        break;
                     case "Treasure Island":
                        hoverPosition.resizeRelocate(290, 120, w, h);
                        break;
                     case "Anchor Bay":
                        hoverPosition.resizeRelocate(360, 500, w, h);
                        break;
                  }
               }
            });
         });

         gridMap[p.getCoordinates().getColumn()][p.getCoordinates().getRow()].getRectangle().hoverProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean show) -> {
            obj.updateGrid(null, p);
            if (show) {
               hoverPosition.setManaged(false);
               double w = hoverPosition.prefWidth(-1);
               double h = hoverPosition.prefHeight(w);
               hoverPosition.setVisible(true);
               hoverPosition.setBorder(new Border(new BorderStroke(Color.BLACK,
                       BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
               switch (p.getName()) {
                  case "London":
                     hoverPosition.resizeRelocate(50, 200, w, h);
                     break;
                  case "Genoa":
                     hoverPosition.resizeRelocate(140, 500, w, h);
                     break;
                  case "Marseilles":
                     hoverPosition.resizeRelocate(470, 460, w, h);
                     break;
                  case "Cadiz":
                     hoverPosition.resizeRelocate(410, 50, w, h);
                     break;
                  case "Venice":
                     hoverPosition.resizeRelocate(50, 460, w, h);
                     break;
                  case "Amsterdam":
                     hoverPosition.resizeRelocate(470, 180, w, h);
                     break;
               }
            }
         });
      }
   }

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
            img = new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/treasure/barrels.png"));
            break;
      }
      return img;
   }

   private ImageView returnPlayerIcon(String playerName) {

      ImageView playerIcon = new ImageView();
      Image imageLocation = null;
      for (int x = 0; x < 4; ++x) {
         if (gameEngine.getPlayer(x).getNickname().equals(playerName)) {
            switch (x) {
               case 0:
                  imageLocation = new Image("uk/ac/aber/cs221/group13/externalfiles/images/playericons/greenplayer.png");
                  break;
               case 1:
                  imageLocation = new Image("uk/ac/aber/cs221/group13/externalfiles/images/playericons/blueplayer.png");
                  break;
               case 2:
                  imageLocation = new Image("uk/ac/aber/cs221/group13/externalfiles/images/playericons/redplayer.png");
                  break;
               case 3:
                  imageLocation = new Image("uk/ac/aber/cs221/group13/externalfiles/images/playericons/yellowplayer.png");
                  break;
            }
            playerIcon.setImage(imageLocation);
            break;
         }

      }
      return playerIcon;
   }

   private VBox topPanePlayerInfo(int numPlayer) {
      VBox playerInfo = new VBox(1.0);
      playerInfo.setPadding(new Insets(2.0, 20.0, 0.0, 10.0));
      Player p = gameEngine.getPlayer(numPlayer);
      p.updatePlayerStat();
      Label playerName = new Label(p.getNickname());

      if (gameEngine.getCurrentPlayer().getNickname().equals(p.getNickname()) && gameEngine.hasStarted()) {
         playerName.setId("currentPlayer");
         playerInfo.setStyle("-fx-border-width:4px 0px 0px 0px;-fx-border-color:chartreuse;");
      } else {
         playerInfo.setStyle("-fx-border-width:4px 0px 0px 0px;-fx-border-color:black");
      }

      //playername
      HBox plName = new HBox(5.0);
      String imageLocation = "";
      switch (numPlayer) {
         case 0:
            imageLocation = "uk/ac/aber/cs221/group13/externalfiles/images/playericons/greenplayer.png";
            break;
         case 1:
            imageLocation = "uk/ac/aber/cs221/group13/externalfiles/images/playericons/blueplayer.png";
            break;
         case 2:
            imageLocation = "uk/ac/aber/cs221/group13/externalfiles/images/playericons/redplayer.png";
            break;
         case 3:
            imageLocation = "uk/ac/aber/cs221/group13/externalfiles/images/playericons/yellowplayer.png";
            break;
      }
      plName.getChildren().addAll(new ImageView(new Image(imageLocation)), playerName);

      //player score
      HBox plScore = new HBox(5.0);
      plScore.getChildren().addAll(new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/boardicons/trophy.png")), new Label("Score:" + Integer.toString(p.getScore())));

      //player coordinates
      HBox plCoords = new HBox(5.0);
      plCoords.getChildren().addAll(new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/boardicons/coordinates.png")), new Label("("
              + Integer.toString(p.getShip().getCoordinates().getColumn() + 1)
              + ", " + Integer.toString(p.getShip().getCoordinates().getRow() + 1) + ")"));

      //player movement range
      HBox plMoveRange = new HBox(5.0);
      plMoveRange.getChildren().addAll(new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/boardicons/range.png")), new Label("Movement range: " + p.getMovementRange()));

      //treasure
      HBox plTreasure = new HBox(5.0);
      if (p.getShip().getTreasure().isEmpty()) {
         plTreasure.getChildren().addAll(new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/boardicons/treasureholder.png")), new Label("No treasure"));
      } else {
         plTreasure.getChildren().add(new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/boardicons/treasureholder.png")));
         for (Treasure tr : p.getShip().getTreasure()) {
            plTreasure.getChildren().add(getImageViewByTreasureName(tr.getType()));
         }
      }

      //chance cards
      HBox plChanceCards = new HBox(5.0);
      if (p.getChanceCards().isEmpty()) {
         plChanceCards.getChildren().addAll(new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/boardicons/chancecard.png")), new Label("No chance cards"));
      } else if (p.getChanceCards().size() == 1) {
         plChanceCards.getChildren().addAll(new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/boardicons/chancecard.png")), new Label("Chance Cards: " + p.getChanceCards().get(0).getCardNumber()));
      } else if (p.getChanceCards().size() > 1) {
         plChanceCards.getChildren().addAll(new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/boardicons/chancecard.png")), new Label("Chance Cards: " + p.getChanceCards().get(0).getCardNumber() + ", " + p.getChanceCards().get(1).getCardNumber()));
      }

      playerInfo.getChildren().addAll(plName, plScore, plCoords, plMoveRange, plTreasure, plChanceCards);
      return playerInfo;
   }

   private Node getRightPane() {
      VBox rightPane = new VBox(5);
      rightPane.setPadding(new Insets(0, 50, 0, 0));

      Island i;
      Port p;

      p = gameEngine.findPortByName("Amsterdam");
      HBox amsterdamName = new HBox(5);
      amsterdamName.getChildren().addAll(new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/boardicons/port.png")), new Label("Port of " + p.getName()));

      amsterdamName.setPadding(new Insets(220, 0, 0, 0));
      rightPane.getChildren().addAll(amsterdamName, generatePortItemsIcons(p.getTreasure(), p.getCrewCards()));

      //Marseilles
      HBox marseillesPlayerName = new HBox(5);

      marseillesPlayerName.getChildren().addAll(returnPlayerIcon(gameEngine.returnPlayerByPort("Marseilles").getNickname()),
              new Label(gameEngine.returnPlayerByPort("Marseilles").getNickname()));

      p = gameEngine.findPortByName("Marseilles");
      HBox marseillesName = new HBox(5);
      marseillesName.getChildren().addAll(new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/boardicons/port.png")), new Label("Port of " + p.getName()));

      marseillesName.setPadding(new Insets(220, 0, 0, 0));
      rightPane.getChildren().addAll(marseillesName, marseillesPlayerName, generatePortItemsIcons(p.getTreasure(), p.getCrewCards()));

      return rightPane;
   }

   private Node getLeftPane() {
      VBox leftPane = new VBox(5);

      leftPane.setPadding(new Insets(225, 0, 0, 100));

      HBox londonPlayerName = new HBox();
      londonPlayerName.getChildren().addAll(returnPlayerIcon(gameEngine.returnPlayerByPort("London").getNickname()),
              new Label(gameEngine.returnPlayerByPort("London").getNickname()));
      HBox portOfLondon = new HBox(5);
      HBox portOfVenice = new HBox(5);

      portOfLondon.getChildren().addAll(new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/boardicons/port.png")), new Label("Port of London"));
      portOfVenice.getChildren().addAll(new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/boardicons/port.png")), new Label("Port of Venice"));
      portOfVenice.setPadding(new Insets(200, 0, 0, 0));

      Port p = gameEngine.findPortByName("London");
      HBox londonItems = generatePortItemsIcons(p.getTreasure(), p.getCrewCards());
      p = gameEngine.findPortByName("Venice");
      HBox veniceItems = generatePortItemsIcons(p.getTreasure(), p.getCrewCards());

      leftPane.getChildren().addAll(portOfLondon, londonPlayerName, londonItems,
              portOfVenice, veniceItems);
      return leftPane;
   }

   private Node getBottomPane() {
      HBox bottomPane = new HBox(300);
      //genoa
      VBox genoaPort = new VBox(0);
      Port genoa = gameEngine.findPortByName("Genoa");

      HBox genoaPlayerName = new HBox();
      String playerName = gameEngine.returnPlayerByPort(genoa.getName()).getNickname();
      genoaPlayerName.getChildren().addAll(returnPlayerIcon(gameEngine.returnPlayerByPort(genoa.getName()).getNickname()),
              new Label(gameEngine.returnPlayerByPort(genoa.getName()).getNickname()));
      HBox genoaPortName = new HBox(5);
      genoaPortName.getChildren().addAll(new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/boardicons/port.png")), new Label("Port of " + genoa.getName()));
      genoaPort.getChildren().addAll(genoaPortName, genoaPlayerName, generatePortItemsIcons(genoa.getTreasure(), genoa.getCrewCards()));
      //genoaPort.setPadding(new Insets(0, 150 - 4 * (playerName.length()), 0, 420));
      genoaPort.setPadding(new Insets(0, 0, 0, 420));

      bottomPane.getChildren().addAll(genoaPort);
      return bottomPane;
   }

   private boolean checkForOtherShipsOnTheWay(Position newPos) {
      Player currentPlayer = gameEngine.getCurrentPlayer();
      int cols = 0, rows = 0;
      int distance = 0;
      if (currentPlayer.getShip().getCoordinates().getColumn() > newPos.getCoordinates().getColumn()) {
         cols = -1;
         distance = currentPlayer.getShip().getCoordinates().getColumn() - newPos.getCoordinates().getColumn();
      } else if (currentPlayer.getShip().getCoordinates().getColumn() < newPos.getCoordinates().getColumn()) {
         cols = 1;
         distance = newPos.getCoordinates().getColumn() - currentPlayer.getShip().getCoordinates().getColumn();
      } else {
         cols = 0;
      }
      if (currentPlayer.getShip().getCoordinates().getRow() > newPos.getCoordinates().getRow()) {
         rows = -1;
         distance = currentPlayer.getShip().getCoordinates().getRow() - newPos.getCoordinates().getRow();
      } else if (currentPlayer.getShip().getCoordinates().getRow() < newPos.getCoordinates().getRow()) {
         rows = 1;
         distance = newPos.getCoordinates().getRow() - currentPlayer.getShip().getCoordinates().getRow();
      } else {
         rows = 0;
      }
      int column, row;
      for (int x = 1; x < distance; ++x) {
         column = currentPlayer.getShip().getCoordinates().getColumn() + x * cols;
         row = currentPlayer.getShip().getCoordinates().getRow() + x * rows;
         if (column > -1 && column < 20 && row > -1 && row < 20) {
            if (gridMap[column][row].hasShip()) {
               Player attacker = gameEngine.findPlayerByShip(gridMap[currentPlayer.getShip().getCoordinates().getColumn() + x * cols][currentPlayer.getShip().getCoordinates().getRow() + x * rows].getShip());

               //  if (!currentPlayer.getNickname().equals(attacker.getNickname())) {
               if (alertPl.crossingPositionFight(attacker.getNickname(), currentPlayer.getNickname())) {
                  if (attacker.getFightPower()
                          != currentPlayer.getFightPower()) {
                     Ship s = currentPlayer.getShip();//keep reference to player's ship - make the code shorter and readable
                     //Remove the ship from old its old position
                     gridMap[s.getCoordinates().getColumn()][s.getCoordinates().getRow()].removeShipFromCurrentPos();
                     //set new coordinates to the ship
                     s.setCoordinates(attacker.getShip().getCoordinates());
                     removeHighlightedPositions();
                     gameEngine.shipBattle(currentPlayer, attacker);

                  } else {
                     alertPl.customAlert("Fighting", "Both players have equal fight power.\n There was no fight. Your ship has been moved.", null);
                  }

               }
            }
         }
      }
      return true;
   }

   private void checkForWinner(Player winner) {

      if (winner.getScore() > 19) {
         gameEngine.setWinner();
         VBox endOfGame = new VBox(30);
         endOfGame.setAlignment(Pos.CENTER);
         Button close = new Button("Exit");
         close.setOnMouseClicked(e -> {
            System.exit(0);
         });

         endOfGame.getChildren().addAll(new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/boardicons/1stPlace.png")), new Label("Winner: " + winner.getNickname() + "\n Score: " + winner.getScore()), close);

         bp.getChildren().clear();
         bp.setCenter(endOfGame);
      }
   }

   private VBox gameLegend() {
      Label header = new Label("Legend");
      header.setPadding(new Insets(30, 0, 20, 0));
      VBox legend = new VBox(10, header);
      header.setFont(Font.font(20));

      for (int itemNumber = 0; itemNumber < 5; ++itemNumber) {
         HBox box = new HBox(10);
         ImageView img = new ImageView();
         Label text = new Label("");
         switch (itemNumber) {
            case 0:
               img.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/treasure/diamond.png"));
               text.setText("Diamonds - 5pts");
               break;
            case 1:
               img.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/treasure/rubies.png"));
               text.setText("Rubies - 5pts");
               break;
            case 2:
               img.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/treasure/gold_bar.png"));
               text.setText("Gold Bar - 4pts");
               break;
            case 3:
               img.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/treasure/pearls.png"));
               text.setText("Pearls - 3pts");
               break;
            case 4:
               img.setImage(new Image("uk/ac/aber/cs221/group13/externalfiles/images/treasure/barrels.png"));
               text.setText("Barrels of Rum - 2pts");
               break;
         }
         box.getChildren().addAll(img, text);
         //  box.setPadding(new Insets(100,100,100,100));
         legend.getChildren().add(box);
      }

      legend.setAlignment(Pos.TOP_CENTER);
      legend.setStyle("-fx-background-color: #FFFFFF;");
      legend.setPadding(new Insets(50, 50, 50, 50));
      Button close = new Button("Close");
      close.setOnMouseClicked(e -> {
         legend.setVisible(false);
      });

      legend.setBorder(new Border(new BorderStroke(Color.BLACK,
              BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
      legend.setManaged(false);
      legend.setVisible(false);
      legend.getChildren().add(close);

      DropShadow borderGlow = new DropShadow();
      borderGlow.setOffsetY(0f);
      borderGlow.setOffsetX(0f);
      borderGlow.setColor(Color.BLUEVIOLET);
      borderGlow.setWidth(300);
      borderGlow.setHeight(300);
      legend.setEffect(borderGlow);

      return legend;
   }

   private boolean checkIfPlayerHasBeenAtPortAndNewPosIsAtPort(Player p, Position newPosition) {
      if (p.getShip().isAtPort()) {
         for (Coordinates c : gameEngine.getAdjacentCoordinates(newPosition.getCoordinates())) {
            if (!gridMap[c.getColumn()][c.getRow()].positionHasNoPortOrIsland()) {
               return true;
            }
         }
      }
      return false;
   }

   private HBox generatePortItemsIcons(ArrayList<Treasure> treasureList, ArrayList<CrewCard> cardList) {
      HBox box = new HBox(5);
      if (!treasureList.isEmpty()) {
         box.getChildren().add(new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/boardicons/treasureholder.png")));
      }
      boolean hasRed = false;
      boolean hasBlack = false;
      if (!cardList.isEmpty()) {
         for (CrewCard c : cardList) {
            if (c.isBlack()) {
               hasBlack = true;
            } else {
               hasRed = true;
            }
         }
      }
      if (hasBlack) {
         box.getChildren().add(new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/crewcards/blackcards.png")));
      }
      if (hasRed) {
         box.getChildren().add(new ImageView(new Image("uk/ac/aber/cs221/group13/externalfiles/images/crewcards/redcards.png")));
      }
      return box;
   }

   private void removeUsedChanceCard(int cardNum, ArrayList<ChanceCard> chanceCardList) {
      Island i = gameEngine.findIslandByName("Treasure Island");
      ChanceCard c = null;
      for (ChanceCard card : chanceCardList) {
         if (card.getCardNumber() == cardNum) {
            c = card;
         }
      }
      if (c != null) {
         chanceCardList.remove(c);
         i.addChanceCardToTheTopOfThePack(c);
      }
   }
}
