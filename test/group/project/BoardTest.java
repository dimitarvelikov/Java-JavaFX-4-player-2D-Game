/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group.project;

import java.util.Queue;
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
public class BoardTest {
    
    public BoardTest() {
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
     * Test of getCrewCardPack method, of class Board.
     */
   /* @Test
    public void testGetCrewCardPack() throws IOException {
        System.out.println("getCrewCardPack");
        Board instance = new Board();
        assertTrue(instance.getCrewCardPack() instanceof Queue);
    }*/

    /**
     * Test of addCrewCardsAndTreasureToPorts method, of class Board.
     */
    /*@Test
    public void testAddCrewCardsAndTreasureToPorts() throws IOException {
        System.out.println("addCrewCardsAndTreasureToPorts");
        Board instance = new Board();
        instance.addCrewCardsAndTreasureToPorts();
    }*/ //Cannot be tested

    /**
     * Test of generateHomeTown method, of class Board.
     */
    /*@Test
    public void testGenerateHomeTown()throws IOException  {
        System.out.println("generateHomeTown");
        Board instance = new Board();
        assertTrue(instance.generateHomeTown() instanceof Port);
    }*/

    /**
     * Test of getAllIslands method, of class Board.
     */
    @Test
    public void testGetAllIslands()throws IOException  {
        System.out.println("getAllIslands");
        Board instance = new Board();
        Island[] result = instance.getAllIslands();
        assertTrue(instance.getAllIslands() instanceof Island[]);
    }

    /**
     * Test of getAllPorts method, of class Board.
     */
    @Test
    public void testGetAllPorts()throws IOException  {
        System.out.println("getAllPorts");
        Board instance = new Board();
        assertTrue(instance.getAllPorts() instanceof Port[]);
    }

    /**
     * Test of getTown method, of class Board.
     */
    @Test
    public void testGetTown() throws IOException {
        System.out.println("getTown");
        Board instance = new Board();
        assertTrue(instance.getPort(0) instanceof Port);
    }

    /**
     * Test of getIsland method, of class Board.
     */
    @Test
    public void testGetIsland() throws IOException {
        System.out.println("getIsland");
        Board instance = new Board();
        assertTrue(instance.getIsland(0) instanceof Island);
    }
    
}
