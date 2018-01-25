package uk.ac.aber.cs221.group13.gameobjects;

import java.util.ArrayList;

/**
 *
 * @author dpv
 */
public class Ship {

   private Coordinates pos;
   private ArrayList<Treasure> treasure;// = new Treasure[2];
   private Port homePort;
   private boolean isAtPort;
   private Coordinates shipTurn;

   public Ship(Port homePort) {
      isAtPort = true;
      treasure = new ArrayList(2);
      this.homePort = homePort;
      pos = new Coordinates(homePort.getCoordinates().getColumn(), homePort.getCoordinates().getRow());
   }

   //empty constructor for the JUnit
   public Ship() {
   }

   public void setIsAtPort(boolean isAtPort) {
      this.isAtPort = isAtPort;
   }

   public Coordinates getShipTurn() {
      return shipTurn;
   }

   public void setShipTurn(Coordinates c) {
      shipTurn = c;
   }

   public boolean isAtPort() {
      return isAtPort;
   }

   public Port getHomePort() {
      return homePort;
   }

   public void setHomePort(Port t) {
      homePort = t;
   }

   public boolean hasTreasure() {
      return !treasure.isEmpty();
   }

   public Coordinates getCoordinates() {
      return pos;
   }

   public void setCoordinates(Coordinates pos) {
      this.pos = pos;
   }

   public ArrayList<Treasure> getTreasure() {
      return treasure;
   }
}
