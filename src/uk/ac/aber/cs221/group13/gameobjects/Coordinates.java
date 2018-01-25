package uk.ac.aber.cs221.group13.gameobjects;

/**
 * This class is set up to allow for tracking of the coordinates in the board,
 * specifically tracking players location
 *
 * @author dpv
 */
public class Coordinates {//implements Comparable<Coordinates> {

   private int column;
   private int row;

   /**
    * coordinates constructor
    *
    * @param x
    * @param y
    */
   public Coordinates(int x, int y) {
      column = x;
      row = y;
   }

   /**
    *
    * @return
    */
   public int getRow() {
      return row;
   }

   /**
    *
    * @param newRow
    */
   public void setRow(int newRow) {
      row = newRow;
   }

   /**
    * How the coordinates will be displayed
    *
    * @return
    */
   @Override
   public String toString() {

      return "(" + (column + 1) + ", " + (row + 1) + ")";
   }

   /**
    *
    * @return
    */
   public int getColumn() {
      return column;
   }

   /**
    *
    * @param newColumn
    */
   public void setColumn(int newColumn) {
      column = newColumn;
   }
   /*
    protected void printCoordinates() {
        System.out.println("Row :" + row + "Column :" + column);
    }
    
    @Override
      public int compareTo(Coordinates o) {
        int cmp = column - o.column;
        if (cmp == 0) {
            cmp = row - o.column;
        }
        return cmp;
    }
   
     
    public int compareTo(Coordinates o) {
        int cmp = row - o.row;
        if (cmp == 0) {
            cmp = column - o.column;
        }
        return cmp;
    }*/

}
