package uk.ac.aber.cs221.group13.gameobjects;

import uk.ac.aber.cs221.group13.gui.AlertPlayer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import javafx.scene.control.Alert;

/**
 *
 * @author dpv
 */
public class Engine {

   private static Board board;
   private final int MAX_PLAYERS = 4;
   private static Player[] players;
   private boolean hasMoved;
   private int numCurrentPlayer;
   private boolean gameHasStarted;
   private boolean playerChoseSide;
   private ChanceCard withDrawnChanceCard;//helps to fix a bug with chance card 1

   /*ship battle instance variables*/
   private Player shipBattleLooser;
   private Player shipBattleWinner;
   private boolean gameIsInShipBattleMode;
   private boolean shipBattleLooserMoved;
   private boolean shipBattleLooserHasTurned;
   private boolean looserHasSeenThePos;
   private boolean shipFightHasWinner;//false if the fight is draw
   private boolean gameHasWinner;

    /**
     * Constructor for the game engine
     * @throws IOException 
     */
   public Engine() throws IOException {
      board = new Board();
      playerChoseSide = false;
      hasMoved = false;
      gameHasStarted = false;
      numCurrentPlayer = 0;
      gameHasWinner = false;
      gameIsInShipBattleMode = false;
   }

    /**
     * setting a temporary copy of the necessary chance card
     * @param card the location the card is stored
     */
   public boolean ifLooserSeenThePos() {
      return looserHasSeenThePos;
   }

    
   public void setLooserHasSeenThePos(boolean seen) {

      looserHasSeenThePos = seen;
   }

   public boolean looserHasTurened() {
      return shipBattleLooserHasTurned;
   }

   public void setLooserHasTurned(boolean turned) {
      shipBattleLooserHasTurned = turned;
   }

   public boolean isShipBattleLooserMoved() {
      return shipBattleLooserMoved;
   }

   public void setShipBattleLooserMooved(boolean moved) {
      shipBattleLooserMoved = moved;
   }

   public boolean gameIsInBattleMode() {
      return gameIsInShipBattleMode;
   }

   public void setGameInBattleMode(boolean mode) {

      gameIsInShipBattleMode = mode;
   }

   public void setWithdrawnChanceCard(ChanceCard card) {
      withDrawnChanceCard = card;
   }

    /**
     * setting a temporary copy of the necessary chance card
     * @param card the location the card is stored
     */
   public ChanceCard getWithdrawnChanceCard() {
      return withDrawnChanceCard;
   }

    /**
     * Used to take the tempory chance card
     * @return ChanceCard returns chance card
     */
    /**
     * If the fight has a winner
     * @return boolean 
     */
   public boolean getShipFightHasWinner() {
      return shipFightHasWinner;
   }

    /**
     * Sets the winner of the fight
     * @param hasWinner where it stores the winner of the fight
     */
   public void setShipFightHasWinner(boolean hasWinner) {
      shipFightHasWinner = hasWinner;
   }

    /**
     * Used to pull the ship battle winner
     * @return Player returns the the winner of the battle, type player
     */
   public Player getShipBattleWinner() {
      return shipBattleWinner;
   }

    /**
     * Sets the winner of the fight
     * @param p where the winner of the battle is stored
     */
   public void setShipBattleWinner(Player p) {
      shipBattleWinner = p;
   }

    /**
     * Used to pull the player number e.g. player 1
     * @return int returns the current players number
     */
   public int getCurrentPlayerNumber() {
      return numCurrentPlayer;
   }

    /**
     * Setting the winner of the game
     */
   public void setWinner() {
      gameHasWinner = true;
   }

    /**
     * Returns if there has been a winner of the game yet, so if the game has
     * been finished
     * @return boolean returns the winner of the game to know who won
     */
   public boolean ifGameHasWinner() {
      return gameHasWinner;
   }

    /**
     * returns all of the players
     * @return players returns all the players from the array
     */
   public Player[] getAllPlayers() {
      return players;
   }

    /**
     * Return the player by the nickname that they gave themselves, and then
     * returns that player
     * @param nickname used to specify the player you want to find
     * @return Player returns the specific player looking for
     */
   public Player findPlayerByNickname(String nickname) {
      for (int x = 0; x < players.length; ++x) {
         if (players[x].getNickname().equals(nickname)) {
            return players[x];
         }
      }
      return null;
   }

    /**
     * Used to find a specific port
     * @param townName used to specify which port you want
     * @return Port returns the specific port name
     */
   public Port findPortByName(String portName) {

      for (int x = 0; x < board.getAllPorts().length; ++x) {
         if (portName.equals(board.getPort(x).getName())) {
            return board.getPort(x);
         }
      }
      return new Port();
   }

    /**
     * Returns a player who has a specific home port
     * @param portName used to specify the port being looked into
     * @return Player returns the player who has the port as the home port
     */
   public Player returnPlayerByPort(String portName) {
      for (int x = 0; x < 4; ++x) {
         if (players[x].getShip().getHomePort().getName().equals(portName)) {
            return players[x];
         }
      }
      return null;
   }

    /**
     * Finds and returns a specific island
     * @param townName used to specify the island 
     * @return Island returns the specific island which the param requested
     */
   public Island findIslandByName(String portName) {

      for (int x = 0; x < board.getAllIslands().length; ++x) {
         if (portName.equals(board.getIsland(x).getName())) {
            return board.getIsland(x);
         }
      }
      return new Island();
   }

    /**
     * Gets and returns the looser of a battle
     * @return Player 
     */
   public Player getShipBattleLooser() {
      return shipBattleLooser;
   }

    /**
     * Set the looser of the battle
     * @param p where the looser of the battle is stored
     */
   public void setShipBattleLooser(Player p) {
      shipBattleLooser = p;
   }

    /**
     * If the player has rotated the ship or not
     * @return boolean
     */
   public boolean ifPlayerChoseSide() {
      return playerChoseSide;
   }

    /**
     * Where the value the player has chosen is stored
     * @param set where the boolean value is stored
     */
   public void setPlayerChoseSide(boolean set) {
      playerChoseSide = set;
   }


    /**
     * this is not in the constructor because of singleton design pattern
     * It initialises all the new players and is assigning them to the 
     * @param playerNames where the names of the player is stored
     */
   public void initializePlayers(String[] playerNames) {
      players = new Player[4];
      Queue<CrewCard> cardPack = board.getCrewCardPack();
      for (int x = 0; x < MAX_PLAYERS; ++x) {
         Player newPlayer = new Player(playerNames[x]);
         newPlayer.initializeShip(board.generateHomePort());
         //setting the order of turns
         switch (newPlayer.getShip().getHomePort().getName()) {
            case "London":
               players[0] = newPlayer;
               players[0].updatePlayerImage(1, 0, 0);
               players[0].getShip().setShipTurn(
                       new Coordinates(players[0].getShip().getCoordinates().getColumn() + 1, players[0].getShip().getCoordinates().getRow()));
               break;
            case "Genoa":
               players[1] = newPlayer;
               players[1].updatePlayerImage(0, 1, 1);

               players[1].getShip().setShipTurn(
                       new Coordinates(players[1].getShip().getCoordinates().getColumn(), players[1].getShip().getCoordinates().getRow() + 1));
               break;
            case "Marseilles":
               players[2] = newPlayer;
               players[2].updatePlayerImage(-1, 0, 2);

               players[2].getShip().setShipTurn(
                       new Coordinates(players[2].getShip().getCoordinates().getColumn() - 1, players[2].getShip().getCoordinates().getRow()));
               break;
            case "Cadiz":
               players[3] = newPlayer;
               players[3].updatePlayerImage(0, -1, 3);

               players[3].getShip().setShipTurn(
                       new Coordinates(players[3].getShip().getCoordinates().getColumn(), players[3].getShip().getCoordinates().getRow() - 1));
               break;
         }
         addCardsToPlayer(newPlayer, cardPack);
      }

   }

    /**
     * Adds the crew cards to the players hand
     * @param p where the name of the player is stored whos gaining cards
     * @param cardPack where the cards are stored which they are being added the the players hand
     */
   private void addCardsToPlayer(Player p, Queue<CrewCard> cardPack) {
      for (int crewCards = 0; crewCards < 5; ++crewCards) {
         p.getCards().add(cardPack.poll());
      }
      p.updatePlayerStat();
   }

    /**
     * Used to say the player has moved
     */
   public void setHasMoved() {
      hasMoved = true;
   }

    /**
     * Used to say the game has started
     */
   public void setGameStarted() {
      gameHasStarted = true;
   }

    /**
     * Used to give a true or false value if the game has started or now
     * @return boolean
     */
   public boolean hasStarted() {
      return gameHasStarted;
   }

    /**
     * Returns a boolean value whether the player has moved or not
     * @return boolean  
     */
   public boolean hasMoved() {
      return hasMoved;
   }

    /**
     * if the player has moved change whos go it is
     */
   public void newMove() {
      hasMoved = true;
      if (!gameIsInShipBattleMode) {
         ++numCurrentPlayer;
         if (numCurrentPlayer > 3) {// has to be 3 
            numCurrentPlayer = 0;
         }
      }
   }

    /**
     * Returns the current player whos go it is
     * @return Player
     */
   public Player getCurrentPlayer() {
      return players[numCurrentPlayer];
   }

    /**
     * Reset if the player has moved or not
     */
   public void resetHasMoved() {
      hasMoved = false;
   }

    /**
     * Gets the game board
     * @return Board
     */
   public Board getBoard() {
      return board;
   }

    /**
     * Gets a specific player by name
     * @param x the name of the player
     * @return Player
     */
   public Player getPlayer(int x) {
      return players[x];
   }

    /**
     * Fetching a specific number of crew cards from a specific island depending
     * on the chance card used for a specific player
     * @param chanceCardNumber where the chance card used is stored
     * @param numCards where the number of the cards needed is stored
     * @param minCrew where the minimum number of crew is stored
     * @param p where the player is stored
     * @param pirateIsland where the name of the island in use is stored
     * @param alert where the alert is being stored
     */
   public void getCrewCardsFromIsland(int chanceCardNumber, int numCards, int minCrew, Player p, Island pirateIsland, AlertPlayer alert) {
      int acquiredCards = 0;
      if (p.getMovementRange() < minCrew + 1) {
         for (int x = 0; x < numCards; ++x) {

            if (!pirateIsland.getCrewCards().isEmpty()) {
               p.getCards().add(pirateIsland.getCrewCards().get(0));
               pirateIsland.getCrewCards().remove(0);
               ++acquiredCards;

            }
         }
         alert.customAlert("Chance card " + chanceCardNumber, p.getNickname() + " acquired " + acquiredCards + " crew cards !", null);
      } else {
         alert.customAlert("Chance card " + chanceCardNumber, p.getNickname() + " position changed !", null);
      }
   }

     /**
     * Tells the game that a player is at and specific island and allows the 
     * game to respond accordingly
     * @param p where the player is stored
     * @param i where the island is stored
     */
   public void playerAtIsland(Player p, Island i) {
      Treasure t = null;
      int value;

      while (p.getShip().getTreasure().size() < 2 && !i.getTreasure().isEmpty()) {
         value = 0;//set an initial value or reset the value from the previous loop
         for (Treasure temp : i.getTreasure()) {
            if (temp.getValue() > value) {
               value = temp.getValue();
               t = temp;
            }
         }
         Alert alert = new Alert(Alert.AlertType.INFORMATION);
         alert.setContentText("Treasure of type " + t.getType() + " with value " + value + " has been acquired by player " + p.getNickname());
         alert.showAndWait();
         p.getShip().getTreasure().add(t);
         i.getTreasure().remove(t);

      }
   }

    /**
     * Get the coordinates next to the current position, by taking the current
     * coordinates and adding 1 or taking away 1
     * @param c where the current coordinates are stored
     * @return Coordinates
     */
   public List<Coordinates> getAdjacentCoordinates(Coordinates c) {
      //8 is the number of the adjacent positions - and set as size of the List
      List<Coordinates> adjacentCoords = new ArrayList<>(8);
      int[] coordinates = {
         //left
         -1, -1,
         -1, 0,
         -1, 1,
         //center
         0, -1,
         0, 1,
         //right
         1, -1,
         1, 0,
         1, 1
      };
      int rowVal, colVal;
      for (int x = 0; x < coordinates.length; ++x) {
         rowVal = coordinates[x] + c.getRow();
         colVal = coordinates[++x] + c.getColumn();
         if (rowVal < 20 && rowVal > -1 && colVal < 20 && colVal > -1) {
            adjacentCoords.add(new Coordinates(colVal, rowVal));
         }
      }
      return adjacentCoords;
   }

    /**
     * Initiating a player a battle between two players and determining the 
     * winner 
     * @param currentPlayer used to store the player who wants to fight
     * @param selectedPlayer used to store the player who the player wants to fight
     */
   public void shipBattle(Player currentPlayer, Player selectedPlayer) {
      gameIsInShipBattleMode = true;
      looserHasSeenThePos = false;
      shipBattleLooserHasTurned = false;
      shipBattleLooserMoved = false;
      currentPlayer.getShip().setIsAtPort(false);
      selectedPlayer.getShip().setIsAtPort(false);
      //System.err.println(" befor Fight powers " + selectedPlayer.getFightPower() + "-" + currentPlayer.getFightPower());
      int currPl = currentPlayer.getFightPower();
      int selPl = selectedPlayer.getFightPower();

      if (currPl > selPl) {
         shipBattleWinner = currentPlayer;
         shipBattleLooser = selectedPlayer;
         shipFightHasWinner = true;
         shipBattleItemTransfer();
      } else if (currPl < selPl) {
         shipBattleWinner = selectedPlayer;
         shipBattleLooser = currentPlayer;
         shipFightHasWinner = true;//battle has a winner
         shipBattleItemTransfer();
      } else if (currPl == selPl) {//doesn't really mattehr who is set as winner or looser
         //probably the one set as a looser will move its ship first
         shipBattleWinner = currentPlayer;
         shipBattleLooser = selectedPlayer;
         shipFightHasWinner = false;//its a draw
         Alert alert = new Alert(Alert.AlertType.ERROR);
         alert.setHeaderText(currentPlayer.getNickname() + "and " + selectedPlayer.getNickname() + " both has equal fight power: " + currentPlayer.getFightPower());
         alert.setContentText("Both players have to move now !");
         alert.showAndWait();
      }
   }

    /**
     * Find a specific player by their ship, by taking the detials of the ship
     * and looking up who owns it
     * @param ship where the ship you are looking into is stored
     * @return Player
     */
   public Player findPlayerByShip(Ship ship) {
      Player p = players[1];//initialize the obj otherwise error at return statement
      for (int x = 0; x < MAX_PLAYERS; ++x) {
         if (players[x].getShip().getCoordinates() == ship.getCoordinates()) {
            p = players[x];
         }
      }
      return p;
   }

    /**
     * What happens when the battle is finished, so the winner gains as many of 
     * their oppenents treasure as possible, the rest is sent to treasure island.
     * If the looser has no treasure but has cards, then the two lowest value 
     * cards in the losers hand are given to the winner. However, if the loser 
     * only has 1 card, then that is just handed over to the winner.
     * @param winner where the winner of the fight is stored
     * @param looser where the looser of the fight is stored
     */
   public void shipBattleItemTransfer() {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setHeaderText("Ship battle winner " + shipBattleWinner.getNickname());
      if (shipBattleLooser.getShip().hasTreasure() && shipBattleWinner.getShip().getTreasure().size() < 2) {
         Island treasureIsland = findIslandByName("Treasure Island");
         switch (shipBattleLooser.getShip().getTreasure().size()) {
            case 1://not tested
               Treasure tr;
               //Add 1 tresure to the winner's ship
               if (shipBattleWinner.getShip().getTreasure().size() < 2) {//not tested
                  tr = shipBattleLooser.getShip().getTreasure().remove(0);
                  shipBattleWinner.getShip().getTreasure().add(tr);
                  alert.setContentText(shipBattleWinner.getNickname() + " acquired treasure " + tr.getType());
               } else {//not tested
                  //add 1 treasure to Treasure Island
                  tr = shipBattleLooser.getShip().getTreasure().remove(0);
                  treasureIsland.getTreasure().add(tr);
                  alert.setContentText(shipBattleWinner.getNickname() + " had no space on its ship\n"
                          + tr.getType() + " went to Treasure Island !");
               }
               break;
            case 2://not tested
               switch (shipBattleWinner.getShip().getTreasure().size()) {
                  case 0://not tested
                     //add all treasure to the winner's ship
                     alert.setContentText(shipBattleWinner.getNickname() + " acquired two treasures !");
                     shipBattleWinner.getShip().getTreasure().addAll(shipBattleLooser.getShip().getTreasure());
                     break;
                  case 1://add 1 treasure to the winner's ship and 1 treasure to the treasure island
                     shipBattleWinner.getShip().getTreasure().add(shipBattleLooser.getShip().getTreasure().remove(0));
                     treasureIsland.getTreasure().add(shipBattleLooser.getShip().getTreasure().remove(0));
                     alert.setContentText(shipBattleWinner.getNickname() + " acquired one treasure  \n "
                             + "another treasure has been moved to Treasure Island !");
                     break;
                  case 2://not tested
                     //add 2 treasure to treasure island
                     treasureIsland.getTreasure().add(shipBattleLooser.getShip().getTreasure().remove(0));
                     treasureIsland.getTreasure().add(shipBattleLooser.getShip().getTreasure().remove(0));
                     alert.setContentText(shipBattleWinner.getNickname() + "acquired two treasures !");
                     break;
               }
               break;
         }
      } else {
         CrewCard smallestCard;
         //  int smallestCardIndex;
         int cardsWon = 0;
         for (int x = 0; x < 2; ++x) {
            if (!shipBattleLooser.getCards().isEmpty()) {
               smallestCard = getLowestValueCrewCard(shipBattleLooser.getCards());
               if (smallestCard != null) {
                  shipBattleLooser.getCards().remove(smallestCard);
                  ++cardsWon;
                  shipBattleWinner.getCards().add(smallestCard);
               }
            }
         }
         alert.setContentText(shipBattleWinner.getNickname() + " acquired " + cardsWon + " crew cards !");

         shipBattleLooser.updatePlayerStat();
         shipBattleWinner.updatePlayerStat();
      }
      alert.showAndWait();
   }

    /**
     * What happens when the player arrives at the home port, the transfer the
     * treasure they had in their ship to their port. The game also alerts the 
     * player they have arrived at their home port
     * @param currentPlayer where the current player is stored
     * @param alert where the alert is stored to notify the player
     */
   public void playerAtHomePort(Player currentPlayer, AlertPlayer alert) {
      Port t = currentPlayer.getShip().getHomePort();
      if (!currentPlayer.getShip().getTreasure().isEmpty()) {
         alert.customAlert(currentPlayer.getNickname() + " you are at your home port !", currentPlayer.getShip().getTreasure().size() + " treasures transferred to " + t.getName(), null);
         for (Treasure tr : currentPlayer.getShip().getTreasure()) {
            t.getTreasure().add(tr);
            currentPlayer.setScore((currentPlayer.getScore() + tr.getValue()));

         }
         // t.getTreasure().addAll(currentPlayer.getShip().getTreasure());
         currentPlayer.getShip().getTreasure().clear();

      } else {
         alert.customAlert(currentPlayer.getNickname() + " you are at your home port !", "Your ship has no treasure to deposit.", null);
      }
   }

    /**
     * Gets the highest card from the array list 
     * @param cardList where the list of cards are stored
     * @return CrewCard
     */
   public CrewCard getHighestValueCrewCard(ArrayList<CrewCard> cardList) {
      CrewCard highestCard = null;
      if (!cardList.isEmpty()) {
         for (CrewCard c : cardList) {
            if (highestCard == null) {
               highestCard = c;
            } else if (c.getValue() > highestCard.getValue()) {
               highestCard = c;
               if (highestCard.getValue() > 2) {
                  return c;
               }
            }
         }
      }
      return highestCard;
   }

    /**
     * Getting the lowest possible value crew card from the list
     * @param cardList where the list of cards is stored
     * @return CrewCard
     */
   public CrewCard getLowestValueCrewCard(ArrayList<CrewCard> cardList) {
      CrewCard lowestCard = null;
      if (!cardList.isEmpty()) {
         for (CrewCard c : cardList) {
            if (lowestCard == null) {
               lowestCard = c;
            } else if (lowestCard.getValue() == 1) {
               return lowestCard;
            } else if (c.getValue() < lowestCard.getValue()) {
               lowestCard = c;
            }
         }
      }
      return lowestCard;
   }

    /**
     * Get the lowest value of treasure from the list of treasures
     * @param tr where the list of treasures is stored
     * @return Treasure
     */
   public Treasure getLowestValueTreasure(ArrayList<Treasure> tr) {
      Treasure treasure = null;
      if (!tr.isEmpty()) {
         for (Treasure t : tr) {
            if (treasure == null) {
               treasure = t;
            } else if (treasure.getValue() > t.getValue()) {
               treasure = t;
            }
         }
      }
      return treasure;
   }

    /**
     * Getting the highest value of treasure from the list of treasures
     * @param tr where the list of treasures is stored
     * @return Treasure
     */
   public Treasure getHighestValueTreasure(ArrayList<Treasure> tr) {
      Treasure treasure = null;
      if (!tr.isEmpty()) {
         for (Treasure t : tr) {
            if (treasure == null) {
               treasure = t;
            } else if (treasure.getValue() < t.getValue()) {
               treasure = t;
            }
         }
      }
      return treasure;
   }

    /**
     * Getting the amount of a specific treasure is in the list
     * @param tr where the list of treasures is stored
     * @param treasureName where the name of the treasure is stored
     * @return int
     */
   public int getTreasureQuantity(ArrayList<Treasure> tr, String treasureName) {
      int x = 0;
      if (!tr.isEmpty()) {
         x = tr.stream().filter((t) -> (t.getType().equals(treasureName))).map((_item) -> 1).reduce(x, Integer::sum);
      }

      return x;
   }
}
