package uk.ac.aber.cs221.group13.gameobjects;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 *
 * @author dpv
 */
public class Island {//implements Comparable<Island> {

   private final String name;
   private ArrayList<Coordinates> pos;
   private ArrayList<CrewCard> cards;
   private ArrayList<Treasure> t;
   private Queue<ChanceCard> chanceCardsQ;

    /**
     * Constructor for the islands
     * @param name where the name of the island is stored
     * @throws IOException 
     */
   public Island(String name) throws IOException {
      this.name = name;
      t = new ArrayList();
      cards = new ArrayList();
      setupCoordinates();
      setupTreasures();
   }

    /**
     * Get the top chance cards
     * @return ChanceCard
     */
   public ChanceCard getTopOfThePackChanceCard() {
      return chanceCardsQ.poll();
   }

    /**
     * Adding chance card back into the pack
     * @param card where the chance card is stored
     */
   public void addChanceCardToTheTopOfThePack(ChanceCard card) {
      chanceCardsQ.offer(card);
   }

    /**
     * Initialising the islands name
     */
   public Island() {
      name = "";
   }
    /**
     * Returns the crew cards which are on the island
     * @return ArrayList<CrewCard>
     */
   public ArrayList<CrewCard> getCrewCards() {
      return cards;
   }

   public String getName() {
      return name;
   }

    /**
     * Gets the name of the island
     * @return String
     */
    /**
     * Gets the coordinates of the island
     * @return ArrayList<Coordinates>
     */
   public ArrayList<Coordinates> getCoordinates() {
      return pos;
   }

    /**
     * returns the treasure which is on the island
     * @return ArrayList<Treasure>
     */
   public ArrayList<Treasure> getTreasure() {
      return t;
   }

    /**
     * Setting up the positions of each island
     */
   private void setupCoordinates() {
      pos = new ArrayList();
      switch (name) {
         case "Mud Bay":
            pos.add(new Coordinates(0, 0));
            break;
         case "Anchor Bay":
            pos.add(new Coordinates(19, 0));
            break;
         case "Cliff Creek":
            pos.add(new Coordinates(19, 19));
            break;
         case "Flat Island":
            for (int x = 1; x < 4; ++x) {
               for (int y = 15; y < 19; ++y) {
                  pos.add(new Coordinates(x, y));
               }
            }
            break;
         case "Pirate Island":
            for (int x = 16; x < 19; ++x) {
               for (int y = 1; y < 5; ++y) {
                  pos.add(new Coordinates(x, y));
               }
            }
            break;
         case "Treasure Island":
            for (int x = 8; x < 12; ++x) {
               for (int y = 8; y < 12; ++y) {
                  pos.add(new Coordinates(x, y));
               }
            }
            break;
      }
   }

    /**
     * Setting up the island to have treasures and cards
     * @throws FileNotFoundException
     * @throws IOException 
     */
   private void setupTreasures() throws FileNotFoundException, IOException {
      if (name.equals("Treasure Island")) {

         ArrayList<ChanceCard> chanceCards = new ArrayList();
         try (BufferedReader br = new BufferedReader(new FileReader("src/uk/ac/aber/cs221/group13/externalfiles/chanceCards.txt"))) {
            String line;
            int cardNumber = 0;
            while ((line = br.readLine()) != null) {
               ++cardNumber;
               chanceCards.add(new ChanceCard(cardNumber, line));
            }
         }

         chanceCardsQ = new LinkedList();

         Random rand = new Random();
         int randomCard;
         while (!chanceCards.isEmpty()) {
            //get random chance card and put it in the queue then remove from the array list - repeat until array list is empty
            randomCard = rand.nextInt(chanceCards.size());
            chanceCardsQ.offer(chanceCards.get(randomCard));
            chanceCards.remove(randomCard);
         }

         String[] treasureTypes = {"Diamonds", "Rubies", "Gold bars", "Pearls", "Barrels of rum"};
         //1st loop - 5 types of treasure
         for (int x = 0; x < 5; ++x) {
            //2nd loop - 4 treasures of each kind
            for (int i = 0; i < 4; ++i) {
               t.add(new Treasure(treasureTypes[x]));
            }
         }

      } else if (name.equals("Flat Island")) {

      }
   }
}
