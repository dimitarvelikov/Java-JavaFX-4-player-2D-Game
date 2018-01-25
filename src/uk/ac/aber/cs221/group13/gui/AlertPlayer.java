/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.aber.cs221.group13.gui;

import uk.ac.aber.cs221.group13.gameobjects.Player;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

/**
 *
 * @author dpv
 */
public class AlertPlayer {

   private final Alert alert;
   private TextArea textArea = new TextArea();

   public AlertPlayer() {
      alert = new Alert(Alert.AlertType.WARNING);
      alert.setResizable(true);
      textArea.setEditable(false);
      textArea.setWrapText(true);
   }

   public Alert getAlert() {
      return alert;
   }

   public void alertPositionNotAvailable() {
      clearAlert();
      alert.setTitle("Error");
      alert.setHeaderText("This position is not available !");
      alert.setContentText("Please one of the highlighted positions !");
      alert.showAndWait();
   }

   public void playerAlreadymoved() {
      clearAlert();
      alert.setTitle("WARNING");
      alert.setHeaderText("You have made your move !");
      alert.setContentText("Please select the End turn button !");
      alert.showAndWait();
   }

   public void gameHasNotStarted() {
      clearAlert();
      alert.setTitle("WARNING");
      alert.setHeaderText("The game has not started !");
      alert.setContentText("Please select the Start game button !");
      alert.showAndWait();
   }

   public boolean crossingPositionFight(String attacker, String attacked) {
      clearAlert();
      alert.getButtonTypes().clear();
      alert.setHeaderText(attacker + " would you like to attack player " + attacked);
      ButtonType yes = new ButtonType("Yes");
      ButtonType no = new ButtonType("No");
      alert.getButtonTypes().setAll(yes, no);
      Optional<ButtonType> result = alert.showAndWait();
      return result.get() == yes;
   }

   public void customAlert(String header, String body, ButtonType customBtn) {
      clearAlert();
      alert.setHeaderText(header);
      textArea.setText(body);
      textArea.setPrefColumnCount(40);
      textArea.setPrefRowCount(body.length() / 20);

      alert.getDialogPane().setContent(textArea);
      //      alert.getDialogPane().setContent(new Label(textArea));
      //   alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
      //     alert.getDialogPane().setPrefWidth(500);
      //   if (customBtn != null) {
      //     alert.getButtonTypes().clear();
      //    alert.getButtonTypes().add(customBtn);
      //}
      alert.showAndWait();
   }

   public void announceWinner(Player winner, Player looser) {
      clearAlert();
      alert.setAlertType(Alert.AlertType.INFORMATION);
      alert.setHeaderText(winner.getNickname() + " has fight power : " + winner.getFightPower() + ", " + looser.getNickname() + " has fight power: " + looser.getFightPower());
      alert.setContentText("The winner is :" + winner.getNickname());
      alert.getButtonTypes().clear();
      alert.getButtonTypes().add(new ButtonType("Close"));
      alert.showAndWait();
   }

   public boolean warnForFight(Player plUnderAttack) {
      clearAlert();
      alert.setAlertType(Alert.AlertType.CONFIRMATION);
      alert.setTitle("WARNING");
      alert.setHeaderText("The selected position is occupied by " + plUnderAttack.getNickname());
      alert.setContentText("Do you want to proceed and enter a sea battle ?");

      ButtonType attack = new ButtonType("Move and attack");
      ButtonType selectNewPos = new ButtonType("Select a new position");
      alert.getButtonTypes().setAll(attack, selectNewPos);
      Optional<ButtonType> result = alert.showAndWait();

      return result.get() == attack;
   }

   public boolean useChanceCardOrTrade() {
      clearAlert();
      alert.setAlertType(Alert.AlertType.CONFIRMATION);
      alert.getButtonTypes().clear();
      alert.setHeaderText("You can either trade with port or use a chance card !");
      ButtonType chanceCard = new ButtonType("Use chance card");
      ButtonType trade = new ButtonType("Trade with port");
      alert.getButtonTypes().setAll(chanceCard, trade);
      Optional<ButtonType> result = alert.showAndWait();
      return result.get() == chanceCard;
   }

   //chance card 2
   public String selectPlayer(Player[] allPlayers, Player currentPlayer) {
      clearAlert();
      alert.setHeaderText("Chance card 2 - Select a player who will then give you 3 Crew Cards.");
      String[] playerNames = new String[3];
      int numPlayer = 0;

      for (int x = 0; x < 4; ++x) {
         if (!allPlayers[x].getNickname().equals(currentPlayer.getNickname())) {
            playerNames[numPlayer] = allPlayers[x].getNickname();
            ++numPlayer;
         }
      }
      ButtonType[] players = new ButtonType[3];
      for (int pl = 0; pl < playerNames.length; ++pl) {
         players[pl] = new ButtonType(playerNames[pl]);
      }

      alert.getButtonTypes().setAll(players[0], players[1], players[2]);
      Optional<ButtonType> result = alert.showAndWait();
      if (result.get() == players[0]) {
         return playerNames[0];
      } else if (result.get() == players[1]) {
         return playerNames[1];
      } else if (result.get() == players[2]) {
         return playerNames[2];
      }

      return null;
   }

   public void enoughTreasureTransfer() {
      clearAlert();
      alert.setHeaderText("You cannot take any more treasure !");
   }

   public boolean chanceCardUsageConfirmation(int cardNum, String text) {
      clearAlert();
      alert.getButtonTypes().clear();
      alert.setHeaderText("You are holding chance card: " + cardNum);
      alert.setContentText("Would you like to use it ?");
      ButtonType yes = new ButtonType("Yes");
      ButtonType no = new ButtonType("No");
      alert.getButtonTypes().setAll(yes, no);
      Optional<ButtonType> result = alert.showAndWait();
      return result.get() == yes;

   }

   private void clearAlert() {
      alert.getDialogPane().setContent(null);
      alert.getButtonTypes().clear();
      alert.setHeaderText("");
      alert.setContentText("");
      alert.setAlertType(Alert.AlertType.INFORMATION);
      alert.getButtonTypes().add(new ButtonType("Ok"));
   }

   public void islandHasNoTreasureOrCrewCards(String islandName) {
      clearAlert();
      alert.setHeaderText(islandName + " has no treasure or chance cards !");
      alert.showAndWait();
   }

   //chance card 11-17
   public boolean treasureOrChanceCard() {
      clearAlert();
      alert.getButtonTypes().clear();
      alert.setHeaderText("Select");
      alert.setContentText("What do you wish to take ?");
      ButtonType treasure = new ButtonType("Treasure");
      ButtonType crewCards = new ButtonType("CrewCards");
      alert.getButtonTypes().addAll(treasure, crewCards);

      Optional<ButtonType> result = alert.showAndWait();
      return result.get() == treasure;

   }

   public void samePositionClicked() {
      clearAlert();
      alert.setHeaderText("You cannot keep your current position !");
      // alert.getButtonTypes().add(new ButtonType("Ok"));
      alert.showAndWait();
   }

   public void shipBattleDraw(int fightPower) {
      clearAlert();
      alert.setHeaderText("Both players have equal fight power " + fightPower);
      alert.setContentText("No treasure or chance card is lost ! Both players need to move their ships.");
      alert.showAndWait();
   }
}
