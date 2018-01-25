/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group.project;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;
import uk.ac.aber.cs221.group13.gui.*;
import uk.ac.aber.cs221.group13.gameobjects.*;
/**
 *
 * @author jon
 */
public class IslandTest {
    
    public IslandTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getTopOfThePackChanceCard method, of class Island.
     */
    /*@Test
    public void testGetTopOfThePackChanceCard() throws IOException {
        System.out.println("getTopOfThePackChanceCard");
        Island instance = new Island(" ");
        ChanceCard result = instance.getTopOfThePackChanceCard();
        assertTrue(result instanceof ChanceCard);
    }*/

    /**
     * Test of addChanceCardToTheTopOfThePack method, of class Island.
     */
    /*@Test
    public void testAddChanceCardToTheTopOfThePack() throws IOException {
        System.out.println("addChanceCardToTheTopOfThePack");
        ChanceCard card = new ChanceCard(40,"Test");
        Island instance = new Island("Test");
        instance.addChanceCardToTheTopOfThePack(card);
        assertTrue(card.equals(instance.getTopOfThePackChanceCard()));
    }*/

    /**
     * Test of getCrewCards method, of class Island.
     */
    @Test
    public void testGetCrewCards() throws IOException {
        System.out.println("getCrewCards");
        Island instance = new Island("Test");
        ArrayList<CrewCard> result = instance.getCrewCards();
        assertTrue(result instanceof ArrayList);
    }

    /**
     * Test of getName method, of class Island.
     */
    @Test
    public void testGetName() throws IOException {
        System.out.println("getName");
        Island instance = new Island("Test");
        String expResult = "Test";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCoordinates method, of class Island.
     */
    @Test
    public void testGetCoordinates() throws IOException{
        System.out.println("getCoordinates");
        Island instance = new Island("Test");
        ArrayList<Coordinates> result = instance.getCoordinates();
        assertTrue(result instanceof ArrayList);
    }

    /**
     * Test of getTreasure method, of class Island.
     */
    @Test
    public void testGetTreasure() throws IOException{
        System.out.println("getTreasure");
        Island instance = new Island("Test");
        ArrayList<Treasure> result = instance.getTreasure();
        assertTrue(result instanceof ArrayList);
    }
    
}
