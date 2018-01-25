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
import static org.junit.Assert.*;
import uk.ac.aber.cs221.group13.gui.*;
import uk.ac.aber.cs221.group13.gameobjects.*;
/**
 *
 * @author jon
 */
public class PortTest {
    
    public PortTest() {
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
     * Test of getCrewCards method, of class Port.
     */
    @Test
    public void testGetCrewCards() {
        System.out.println("getCrewCards");
        Port instance = new Port("London");
        ArrayList<CrewCard> expResult = new ArrayList();
        ArrayList<CrewCard> result = instance.getCrewCards();
        assertTrue(expResult.equals(result));
    }

    /**
     * Test of addNewTreasure method, of class Port.
     */
    @Test
    public void testAddNewTreasure() {
        System.out.println("addNewTreasure");
        Treasure t = new Treasure("Rubies");
        Port instance = new Port("London");
        instance.addNewTreasure(t);
        assertTrue(instance.getTreasure().get(0).equals(t));
    }

    /**
     * Test of getName method, of class Port.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        Port instance = new Port("London");
        String expResult = "London";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTreasure method, of class Port.
     */
    @Test
    public void testGetTreasure() {
        System.out.println("getTreasure");
        Port instance = new Port("London");
        ArrayList<Treasure> expResult = new ArrayList();
        ArrayList<Treasure> result = instance.getTreasure();
        assertTrue(expResult.equals(result));
    }

    /**
     * Test of isHomeTown method, of class Port.
     */
    /*@Test
    public void testIsHomeTown() {
        System.out.println("isHomeTown");
        Port instance = new Port("London");
        boolean expResult = true;
        boolean result = instance.isHomePort();
        assertEquals(expResult, result);
    }*/

    /**
     * Test of ifHasPlayer method, of class Port.
     */
    /*@Test
    public void testIfHasPlayer() {
        System.out.println("ifHasPlayer");
        Port instance = new Port("London");
        boolean expResult = false;
        boolean result = instance.ifHasPlayer();
        assertEquals(expResult, result);
    }*/

    /**
     * Test of makeHomeTown method, of class Port.
     */
    /*@Test
    public void testMakeHomeTown() {
        System.out.println("makeHomeTown");
        Port instance = new Port("London");
        instance.makeHomePort();
        assertTrue(instance.ifHasPlayer());
    }*/

    /**
     * Test of getCoordinates method, of class Port.
     */
    @Test
    public void testGetCoordinates() {
        System.out.println("getCoordinates");
        Port instance = new Port("London");
        Coordinates result = instance.getCoordinates();
        assertTrue(result instanceof Coordinates);
    }
    
}
