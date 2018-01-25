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
public class ShipTest {
    
    public ShipTest() {
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
     * Test of setIsAtPort method, of class Ship.
     */
    @Test
    public void testSetIsAtPort() {
        System.out.println("setIsAtPort");
        boolean isAtPort = false;
        Port testPort = new Port("London");
        Ship instance = new Ship(testPort);
        instance.setIsAtPort(isAtPort);
        assertTrue(instance.isAtPort()==false);
    }

    /**
     * Test of getShipTurn method, of class Ship.
     */
    /*@Test
    public void testGetShipTurn() {
        System.out.println("getShipTurn");
        Port testPort = new Port("London");
        Ship instance = new Ship(testPort);
        Coordinates expResult = null;
        Coordinates result = instance.getShipTurn();
        assertEquals(expResult, result);
    }

    /**
     * Test of setShipTurn method, of class Ship.
     */
    @Test
    public void testSetShipTurn() {
        System.out.println("setShipTurn");
        Coordinates c = new Coordinates(1,2);
        Port testPort = new Port("London");
        Ship instance = new Ship(testPort);
        instance.setShipTurn(c);
    }

    /**
     * Test of isAtPort method, of class Ship.
     */
    @Test
    public void testIsAtPort() {
        System.out.println("isAtPort");
        Port testPort = new Port("London");
        Ship instance = new Ship(testPort);
        boolean expResult = false;
        boolean result = instance.isAtPort();
        assertFalse((expResult==result));
    }

    /**
     * Test of getHomeTown method, of class Ship.
     */
    @Test
    public void testGetHomeTown() {
        System.out.println("getHomeTown");
        Port testPort = new Port("London");
        Ship instance = new Ship(testPort);
        Port result = instance.getHomePort();
        assertEquals(testPort, result);
    }

    /**
     * Test of setHomeTown method, of class Ship.
     */
    @Test
    public void testSetHomeTown() {
        System.out.println("setHomeTown");
        Port testPort = new Port("London");
        Ship instance = new Ship(testPort);
        instance.setHomePort(testPort);
    }

    /**
     * Test of hasTreasure method, of class Ship.
     */
    @Test
    public void testHasTreasure() {
        System.out.println("hasTreasure");
        Port testPort = new Port("London");
        Ship instance = new Ship(testPort);
        boolean expResult = false;
        boolean result = instance.hasTreasure();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCoordinates method, of class Ship.
     */
    @Test
    public void testGetCoordinates() {
        System.out.println("getCoordinates");
        Port testPort = new Port("London");
        Ship instance = new Ship(testPort);
        Coordinates expResult = testPort.getCoordinates();
        Coordinates result = instance.getCoordinates();
        assertTrue((testPort.getCoordinates().getColumn()==instance.getCoordinates().getColumn())&&(testPort.getCoordinates().getRow()==instance.getCoordinates().getRow()));
    }
    /**
     * Test of setCoordinates method, of class Ship.
     */
    @Test
    public void testSetCoordinates() {
        System.out.println("setCoordinates");
        Port testPort = new Port("London");
        Ship instance = new Ship(testPort);
        Coordinates pos = new Coordinates(1,3);
        instance.setCoordinates(pos);
    }

    /**
     * Test of getTreasure method, of class Ship.
     */
    @Test
    public void testGetTreasure() {
        System.out.println("getTreasure");
        Port testPort = new Port("London");
        Ship instance = new Ship(testPort);
        ArrayList<Treasure> expResult = new ArrayList(2);
        ArrayList<Treasure> result = instance.getTreasure();
        assertTrue(expResult.equals(result));
    }
    
}
