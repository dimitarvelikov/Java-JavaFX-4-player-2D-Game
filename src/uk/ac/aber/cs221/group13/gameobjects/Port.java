package uk.ac.aber.cs221.group13.gameobjects;

import java.util.ArrayList;

/**
 *
 * @author dpv
 */
public class Port {

   private final String name;
   private boolean isHomePort;
   private ArrayList<Treasure> treasures;
   private ArrayList<CrewCard> cardList;
   private boolean hasPlayer;
   private Coordinates pos;

    /**
     * Constructor for the port
     * @param name where the name of the port is stored
     */
   public Port(String name) {
      this.name = name;
      cardList = new ArrayList();
      isHomePort = false;
      hasPlayer = false;
      treasures = new ArrayList();
      generatePosition();
   }

     /**
     * Initialising the port name 
     */
   public Port() {
      name = "";
   }

     /**
     * getting the list of crew cards
     * @return ArrayList<CrewCard>
     */
   public ArrayList<CrewCard> getCrewCards() {
      return cardList;
   }

    /**
     * Adding a new treasure
     * @param t where the treasure is stored
     */
   public void addNewTreasure(Treasure t) {
      treasures.add(t);
   }

     /**
     * getting the name of the port
     * @return String
     */
   public String getName() {
      return name;
   }

    /**
     * Getting the list of the treasures
     * @return ArrayList<Treasure>
     */
   public ArrayList<Treasure> getTreasure() {
      return treasures;
   }

    /**
     * returns if the port is a home port
     * @return boolean
     */
   protected boolean isHomePort() {
      return isHomePort;
   }

    /**
     * does the port have a player
     * @return boolean
     */
   public boolean ifHasPlayer() {
      return hasPlayer;
   }
    
   /**
     * setting the port to be a home port
     */
   protected void makeHomePort() {
      //  isHomePort = true;
      hasPlayer = true;
   }

    /**
     * Gets the coordinates of the port
     * @return Coordinates
     */
   public Coordinates getCoordinates() {
      return pos;
   }

    /**
     * Generating the locations of the ports and setting them to be home ports.
     * This isnt where they are assigned to the players
     */
   private void generatePosition() {
      //all the positions are the same as the required 
      //but both x and y are -1 because the arrays starts from 0 to 19 not 1 to20

      switch (name) {
         case "Venice":
            pos = new Coordinates(0, 6);
            break;
         case "London":
            isHomePort = true;
            pos = new Coordinates(0, 13);
            break;
         case "Cadiz":
            isHomePort = true;
            pos = new Coordinates(13, 19);
            break;
         case "Amsterdam":
            pos = new Coordinates(19, 13);
            break;
         case "Marseilles":
            isHomePort = true;
            pos = new Coordinates(19, 6);
            break;
         case "Genoa":
            isHomePort = true;
            pos = new Coordinates(6, 0);
            break;
         //no need of default case
      }
   }
}
