package uk.ac.aber.cs221.group13.gameobjects;

/**
 *
 * @author dpv
 */
public class CrewCard {

   private boolean color;//true-black false- red

   public CrewCard() {
   }
   private int value;

   /**
     * Constructor for the crew cards
     * @param value where the crew card value is stord
     * @param color where the colour is stored
     */
   public CrewCard(int value, boolean color) {
      this.value = value;
      this.color = color;
   }

   /**
     * whether the card is black or red
     * @return boolean
     */
   public boolean isBlack() {//same as get color but is more clear what the color is 
      return color;
   }

   /**
     * setting the colour of the card
     * @param color where the colour of the card is stored
     */
   protected void setColor(boolean color) {
      this.color = color;
   }

   /**
     * getting the value of the crew card
     * @return int
     */
   public int getValue() {
      return value;
   }

   /**
     * Setting the value of the crew card
     * @param value where the value of the crew card is stored
     */
   protected void setValue(int value) {
      this.value = value;
   }
}
