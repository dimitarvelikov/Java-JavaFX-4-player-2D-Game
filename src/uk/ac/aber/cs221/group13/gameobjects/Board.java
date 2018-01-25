package uk.ac.aber.cs221.group13.gameobjects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 *
 * @author dpv
 */
public class Board {

   private final Port[] allPorts = new Port[6];
   private final Island[] allIslands = new Island[6];
   private Queue<CrewCard> crewCards;

    /**
     * Initialising the board
     * @throws IOException
     */
   public Board() throws IOException {
      createCrewCards();
      createPorts();
      createIslands();
   }
    
    /**
     * @return Queue<CrewCard> returns a queue of crew cards
     */
   protected Queue<CrewCard> getCrewCardPack() {
      return crewCards;
   }

    /**
     * This method is adding the crew cards and the treasure to the port. They
     * treasures get added at random, but they total for the treasures has to 
     * equal 8. 
     */
   public void addCrewCardsAndTreasureToPorts() {
      int allItemsValue;
      Random rand = new Random();
      Port currentPort;
      for (int x = 0; x < allPorts.length; ++x) {
         allItemsValue = 0;//for each port - reset
         currentPort = allPorts[x];
         for (int numCards = 0; numCards < 2; ++numCards) {

            CrewCard c = crewCards.poll();
            allItemsValue += c.getValue();
            currentPort.getCrewCards().add(c);
         }
         //add the treasure

         switch (8 - allItemsValue) {
            case 2:
               currentPort.addNewTreasure(new Treasure("Barrels of rum"));
               break;
            case 3:
               currentPort.addNewTreasure(new Treasure("Pearls"));
               break;
            case 4:
               if (rand.nextBoolean()) {
                  for (int tr = 0; tr < 2; ++tr) {
                     currentPort.addNewTreasure(new Treasure("Barrels of rum"));
                  }
               } else {
                  currentPort.addNewTreasure(new Treasure("Gold bars"));
               }
               break;
            case 5:
               if (rand.nextBoolean()) {
                  currentPort.addNewTreasure(new Treasure("Pearls"));
                  currentPort.addNewTreasure(new Treasure("Barrels of rum"));
               } else {
                  currentPort.addNewTreasure(new Treasure("Diamonds"));
               }
               break;
            case 6:
               switch (rand.nextInt(3)) {
                  case 0://3,3
                     for (int tr = 0; tr < 3; ++tr) {
                        currentPort.addNewTreasure(new Treasure("Barrels of rum"));
                     }
                     break;
                  case 1: //4,2
                     currentPort.addNewTreasure(new Treasure("Barrels of rum"));
                     currentPort.addNewTreasure(new Treasure("Gold bars"));
                     break;
                  case 2://2,2,2
                     for (int tr = 0; tr < 3; ++tr) {
                        currentPort.addNewTreasure(new Treasure("Barrels of rum"));
                     }
                     break;
               }
               break;
         }
      }
      removeTreasuresFromTreasureIsland();
      moveCrewCardsFromBoardToPirateIsland();
   }

    /**
     * Generating all the ports which can be home ports
     * @return Port
     */
   protected Port generateHomePort() {
      boolean portIsFound = false;
      Random rand = new Random();
      int randomPort = 0;

      while (!portIsFound) {
         randomPort = rand.nextInt(4);
         if (allPorts[randomPort].isHomePort() && !allPorts[randomPort].ifHasPlayer()) {
            allPorts[randomPort].makeHomePort();
            return allPorts[randomPort];
         }
      }
      return null;
   }

    /**
     * Creating the ports
     */
   private void createPorts() {
      String[] portNames = {"London", "Genoa", "Marseilles", "Cadiz", "Amsterdam", "Venice"};

      for (int ports = 0; ports < 6; ++ports) {
         allPorts[ports] = new Port(portNames[ports]);
      }
   }

    /**
     * Get a list of all the islands
     * @return Island[]
     */
   public Island[] getAllIslands() {
      return allIslands;
   }

    /**
     * Gets a list of all the ports
     * @return Port[]
     */
   public Port[] getAllPorts() {
      return allPorts;
   }

    /**
     * creating the islands and the bays
     * @throws IOException
     */
   private void createIslands() throws IOException {

      String[] islandNames = {"Treasure Island", "Mud Bay", "Anchor Bay", "Cliff Creek", "Flat Island", "Pirate Island"};
      for (int x = 0; x < 6; ++x) {
         allIslands[x] = new Island(islandNames[x]);

      }
   }

    /**
    * Getting the specific ports
    * @return Port
    */
   public Port getPort(int x) {
      return allPorts[x];
   }
   
    /**
     * Getting a specific island
     * @return Island
     */
   public Island getIsland(int x) {
      return allIslands[x];
   }

    /**
     * Creating the crew cards there is 18 of each colour, 6 of each number
     */
   private void createCrewCards() {
      crewCards = new LinkedList();
      ArrayList<CrewCard> cardList = new ArrayList();
      for (int x = 0; x < 18; ++x) {
         cardList.add(new CrewCard((x / 6) + 1, true));
         cardList.add(new CrewCard((x / 6) + 1, false));
      }
      /*     cardList.forEach((c) -> {
            System.err.println(c.getValue());
        });*/
      int numCard = 0;
      Random rand = new Random();
      while (!cardList.isEmpty()) {
         numCard = rand.nextInt(cardList.size());
         crewCards.offer(cardList.remove(numCard));
      }
      //  System.err.println(crewCards.size());
   }

    /**
     * Remove the treasures from the island
     */
   private void removeTreasuresFromTreasureIsland() {
      for (int x = 0; x < allIslands.length; ++x) {
         if (allIslands[x].getName().equals("Treasure Island")) {
            Treasure removeTreasure = null;
            for (int port = 0; port < allPorts.length; ++port) {

               for (Treasure tr : allPorts[port].getTreasure()) {
                  for (Treasure t : allIslands[x].getTreasure()) {
                     if (tr.getType().equals(t.getType())) {
                        removeTreasure = t;
                     }

                  }
                  if (removeTreasure != null) {
                     allIslands[x].getTreasure().remove(removeTreasure);
                     removeTreasure = null;//reset in case of repetition
                  }
               }
            }
            break;
         }
      }
   }

    /**
     * Moving the crew cards to pirate island.
     */
   private void moveCrewCardsFromBoardToPirateIsland() {
      for (int x = 0; x < allIslands.length; ++x) {
         if (allIslands[x].getName().equals("Pirate Island")) {
            while (!crewCards.isEmpty()) {

               allIslands[x].getCrewCards().add(crewCards.remove());

            }
         }

      }
   }
}
