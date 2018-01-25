package uk.ac.aber.cs221.group13.gameobjects;

import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

/**
 *
 * @author dpv
 */
public class Player {//implements Comparable<Player> {

   private final String nickname;
   private int score;
   private int fightPower;
   private int movementRange;
   private ArrayList<CrewCard> cards;
   private ArrayList<ChanceCard> chanceCards;
   private Ship ship;
   private ImagePattern image;

    /**
     * Constructor for the player in the game
     * @param nick
     */
   public Player(String nick) {
      image = new ImagePattern(new Image("uk/ac/aber/cs221/group13/externalfiles/images/rotation/ship/2/w.png"));
      nickname = nick;
      score = 0;
      chanceCards = new ArrayList();
      cards = new ArrayList();
   }

    /**
     * Gets a list of the chance cards that the player owns
     * @return ArrayList<ChanceCard>
     */
   public ArrayList<ChanceCard> getChanceCards() {
      return chanceCards;
   }

    /**
     * Updates the player image depending on the actions they take, so it will
     * change the rotation, when the player has decided to change it.
     * @param cols where the x position in the 3 x 3 grid the player clicked to turn is stored
     * @param rows where the y position in the 3 x 3 grid the player clicked to turn is stored
     * @param numPlayer where the player number is stored
     */
   public void updatePlayerImage(int cols, int rows, int numPlayer) {
      String location = "uk/ac/aber/cs221/group13/externalfiles/images/rotation/ship/" + Integer.toString(numPlayer + 1);
      switch (cols) {
         case -1:
            switch (rows) {
               case -1:
                  image = new ImagePattern(new Image(location + "/sw.png"));
                  break;
               case 0:
                  image = new ImagePattern(new Image(location + "/w.png"));
                  break;
               case 1:
                  image = new ImagePattern(new Image(location + "/nw.png"));
                  break;
            }
            break;
         case 0:
            switch (rows) {
               case -1:
                  image = new ImagePattern(new Image(location + "/s.png"));
                  break;
               case 1:
                  image = new ImagePattern(new Image(location + "/n.png"));
                  break;
            }
            break;
         case 1:
            switch (rows) {
               case -1:
                  image = new ImagePattern(new Image(location + "/se.png"));
                  break;
               case 0:
                  image = new ImagePattern(new Image(location + "/e.png"));
                  break;
               case 1:
                  image = new ImagePattern(new Image(location + "/ne.png"));
                  break;
            }
            break;
      }

   }

    /**
     * Gets the ship image
     * @return ImagePattern
     */
   public ImagePattern getShipImage() {
      return image;
   }

    /**
     * Updates the players statistics, so it will update the fight power that 
     * the player has and also the movement range that it has depending on the 
     * crew cards the player has picked up.
     */
   public void updatePlayerStat() {
      calculateFightPower();
      calculateMovementRange();
   }

    /**
     * Initializing the ship
     * @param t where the port is stored
     */
   protected void initializeShip(Port t) {
      ship = new Ship(t);
   }

    /**
     * Gets the players name
     * @return String
     */
   public String getNickname() {
      return nickname;
   }

    /**
     * gets the players current score so how many treasures they have in their
     * home port
     * @return int
     */
   public int getScore() {
      return score;
   }

    /**
     * Sets the score of the player when they have transferred items to their
     * home port
     * @param newScore where the value of their score is stored
     */
   public void setScore(int newScore) {
      score = newScore;
   }

    /**
     * Get the current fight power of the player
     * @return int
     */
   public int getFightPower() {
      return fightPower;
   }

    /**
     * Get the current crew cards that the player owns
     * @return ArrayList<CrewCard>
     */
   public ArrayList<CrewCard> getCards() {
      return cards;
   }

    /**
     * Gets the players ship
     * @return Ship
     */
   public Ship getShip() {
      return ship;
   }

    /**
     * Gets the current range of the player depending on what crew cards they
     * have in their hand
     * @return int
     */
   public int getMovementRange() {
      return movementRange;
   }

    /**
     * Calculating the movement range of the player depending on the black crew 
     * cards in the players hand.
     */
   protected void calculateMovementRange() {
      if (!cards.isEmpty()) {
         movementRange = 0;//reset
         cards.forEach((c) -> {
            movementRange += c.getValue();
         });
      } else {
         movementRange = 1;
      }
   }

    /**
     * Calculating the fight power of the player depending on the red crew cards
     * in their hand
     */
   protected void calculateFightPower() {
      fightPower = 0;//reset fight power
      int red = 0, black = 0;//for both types of cards
      if (!cards.isEmpty()) {
         for (CrewCard c : cards) {
            if (c.isBlack()) {//if is black
               black += c.getValue();
            } else {
               red += c.getValue();
            }
         }
         if (red != black) {//if they are == fight power remains 0 

            fightPower = (red > black) ? red - black : black - red;
            //        System.err.println("red is: " + red + " black is: " + black + " fight power is: " + fightPower);
         }
      }
   }

   /* @Override
    public int compareTo(Player o) {

        return (this.nickname.equals(o.nickname)) ? 1 : 0;
    }
    */
}
