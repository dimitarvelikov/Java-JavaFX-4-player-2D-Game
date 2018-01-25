package uk.ac.aber.cs221.group13.gameobjects;

import javafx.scene.shape.Rectangle;

/**
 *
 * @author dpv
 */
public class Position {//implements Comparable<Position> {

   private final Coordinates pos;
   private Ship ship;
   private Port port;
   private Island island;
   private Rectangle r;

    /**
     * Constructor for position
     * @param x where the x coordinate is stored
     * @param y where the  coordinate is stored
     */
   public Position(int x, int y) {
      this.pos = new Coordinates(x, y);
      r = new Rectangle(38.0, 38.0);
   }

    /**
     * get the specific square
     * @return Rectangle
     */
   public Rectangle getRectangle() {
      return r;
   }

    /**
     * is the position empty or not
     * @return boolean
     */
   public boolean positonIsEmpty() {
      return port == null && island == null && ship == null;
   }

    /**
     * get the port in that position
     * @return Port
     */
   public Port getPort() {
      return port;
   }

    /**
     * does the position have neither a port or and island
     * @return boolean
     */
   public boolean positionHasNoPortOrIsland() {
      return port == null && island == null;
   }

    /**
     * does the position have a ship
     * @return boolean
     */
   public boolean ifPosHasShip() {
      return ship == null;
   }

    /**
     * does the position have a town
     * @return boolean
     */
   public boolean ifPositionHasAPort() {

      return port == null;
   }

    /**
     * setting the name of the island
     * @param i where the island is stored
     */
   public void setIsland(Island i) {
      island = i;
   }

    /**
     * setting the ship
     * @param s where the ship is stored
     */
   public void setShip(Ship s) {
      ship = s;
   }

    /**
     * getting a specific ship
     * @return Ship
     */
   public Ship getShip() {
      return ship;
   }

    /**
     * if the position has a ship
     * @return boolean
     */
   public boolean hasShip() {
      return ship != null;
   }

    /**
     * return the island
     * @return Island
     */
   public Island getIsland() {
      return island;
   }

    /**
     * if the position has a island
     * @return boolean
     */
   public boolean hasIsland() {
      return island == null;
   }

    /**
     * setting the port
     * @param t where the port is stored
     */
   public void setPort(Port t) {
      port = t;
   }

    /**
     * setting the new coordinates
     * @param col where the x coordinate is stored
     * @param row where the y coordinate is stored
     */
   public void setCoordinates(int col, int row) {
      pos.setColumn(col);
      pos.setRow(row);
   }

    /**
     * getting the current coordinates
     * @return Coordinates
     */
   public Coordinates getCoordinates() {
      return pos;
   }

    /**
     * removing the ship from its position
     */ 
   public void removeShipFromCurrentPos() {
      ship = null;
   }
   /*
    @Override
    public int compareTo(Position o) {
        return (pos.getColumn() == o.getCoordinates().getColumn() && pos.getRow() == o.getCoordinates().getRow()) ? 1 : 0;
    }*/
}
