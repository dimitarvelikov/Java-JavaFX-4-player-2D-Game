/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group.project;

import javafx.scene.shape.Rectangle;
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
public class PositionTest {
    
    public PositionTest() {
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
     * Test of getRectangle method, of class Position.
     */
    /*@Test
    public void testGetRectangle() {
        System.out.println("getRectangle");
        Position instance = null;
        Rectangle expResult = null;
        Rectangle result = instance.getRectangle();
        assertEquals(expResult, result);
    }*///Cannot test becuase junit cannot test javafx packages

    /**
     * Test of positonIsEmpty method, of class Position.
     */
    @Test
    public void testPositonIsEmpty() {
        System.out.println("positonIsEmpty");
        Position instance = new Position(1,1);
        boolean result = instance.positonIsEmpty();
        assertTrue(result==true);
    }

    /**
     * Test of getTown method, of class Position.
     */
    @Test
    public void testGetTown() {
        System.out.println("getTown");
        Position instance = new Position(1,1);
        Port testPort = new Port("London");
        instance.setPort(testPort);
        Port result = instance.getPort();
        assertTrue(result.equals(testPort));
    }

    /**
     * Test of positionHasNoTownOrIsland method, of class Position.
     */
    @Test
    public void testPositionHasNoTownOrIsland() {
        System.out.println("positionHasNoTownOrIsland");
        Position instance = new Position(1,1);
        boolean expResult = true;
        boolean result = instance.positionHasNoPortOrIsland();
        assertEquals(expResult, result);
    }

    /**
     * Test of ifPosHasShip method, of class Position.
     */
    @Test
    public void testIfPosHasShip() {
        System.out.println("ifPosHasShip");
        Position instance = new Position(1,1);
        boolean expResult = true;
        boolean result = instance.ifPosHasShip();
        assertEquals(expResult, result);
    }

    /**
     * Test of ifPositionHasATown method, of class Position.
     */
    @Test
    public void testIfPositionHasATown() {
        System.out.println("ifPositionHasATown");
        Position instance = new Position(1,1);
        boolean expResult = true;
        boolean result = instance.ifPositionHasAPort();
        assertEquals(expResult, result);
    }

    /**
     * Test of setIsland method, of class Position.
     */
    @Test
    public void testSetIsland() {
        System.out.println("setIsland");
        Island i = new Island();
        Position instance = new Position(1,1);
        instance.setIsland(i);
        assertTrue(instance.hasIsland()==false);
    }

    /**
     * Test of setShip method, of class Position.
     */
    @Test
    public void testSetShip() {
        System.out.println("setShip");
        Ship s = new Ship();
        Position instance = new Position(1,1);
        instance.setShip(s);
        assertTrue(instance.hasShip()==true);
    }

    /**
     * Test of getShip method, of class Position.
     */
    @Test
    public void testGetShip() {
        System.out.println("getShip");
        Position instance = new Position(1,1);
        Ship expResult = new Ship();
        instance.setShip(expResult);
        Ship result = instance.getShip();
        assertTrue(result.equals(result));
    }

    /**
     * Test of hasShip method, of class Position.
     */
    @Test
    public void testHasShip() {
        System.out.println("hasShip");
        Position instance = new Position(1,1);
        boolean expResult = false;
        boolean result = instance.hasShip();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIsland method, of class Position.
     */
    @Test
    public void testGetIsland() {
        System.out.println("getIsland");
        Position instance = new Position(1,1);
        Island expResult = new Island();
        instance.setIsland(expResult);
        Island result = instance.getIsland();
        assertTrue(expResult.equals(result));
    }

    /**
     * Test of hasIsland method, of class Position.
     */
    @Test
    public void testHasIsland() {
        System.out.println("hasIsland");
        Position instance = new Position(1,1);
        boolean expResult = true;
        boolean result = instance.hasIsland();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTown method, of class Position.
     */
    @Test
    public void testSetTown() {
        System.out.println("setTown");
        Port t = new Port();
        Position instance = new Position(1,1);
        instance.setPort(t);
        assertTrue(t.equals(instance.getPort()));
    }

    /**
     * Test of setCoordinates method, of class Position.
     */
    @Test
    public void testSetCoordinates() {
        System.out.println("setCoordinates");
        int col = 0;
        int row = 0;
        Position instance = new Position(1,1);
        instance.setCoordinates(col, row);
        assertTrue((instance.getCoordinates().getColumn()==col)&&(instance.getCoordinates().getRow()==row));
    }

    /**
     * Test of getCoordinates method, of class Position.
     */
    @Test
    public void testGetCoordinates() {
        System.out.println("getCoordinates");
        Position instance = new Position(1,1);
        instance.setCoordinates(3,3);
        Coordinates result = instance.getCoordinates();
        assertTrue(result instanceof Coordinates);
    }

    /**
     * Test of removeShipFromCurrentPos method, of class Position.
     */
    @Test
    public void testRemoveShipFromCurrentPos() {
        System.out.println("removeShipFromCurrentPos");
        Position instance = new Position(1,1);
        Ship testShip = new Ship();
        instance.setShip(testShip);
        assertTrue(instance.hasShip()==true);
        instance.removeShipFromCurrentPos();
        assertTrue(instance.hasShip()==false);
    }
    
}
