package uk.ac.aber.cs221.group13.gameobjects;

/**
 *
 * @author dpv
 */
public class Treasure {//implements Comparable<Treasure> {

   private int value;
   private String type;

   public Treasure(String type) {
      this.type = type;
      setValue();
   }

   public int getValue() {
      return value;
   }

   public String getType() {
      return type;
   }

   protected void setValue(int newVal) {
      value = newVal;
   }

   private void setValue() {

      switch (type) {
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
      }
   }
}
