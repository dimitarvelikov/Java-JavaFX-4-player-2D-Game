/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.aber.cs221.group13.gameobjects;

/**
 *
 * @author dpv
 */
public class ChanceCard {

   private final int cardNo;
   private final String description;

   public ChanceCard(int number, String description) {
      cardNo = number;
      this.description = description;
   }

   public int getCardNumber() {
      return cardNo;
   }

   public String getCardDescription() {
      return description;
   }
}
