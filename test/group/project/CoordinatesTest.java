/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group.project;

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
public class CoordinatesTest {
    
    public CoordinatesTest() {
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
     * Test of getRow method, of class Coordinates.
     */
    @Test
    public void testGetRow() {
        System.out.println("getRow");
        Coordinates instance = new Coordinates(1,1);
        assertEquals(1, instance.getRow());
    }

    /**
     * Test of setRow method, of class Coordinates.
     */
    @Test
    public void testSetRow() {
        System.out.println("setRow");
        int newRow = 0;
        Coordinates instance = new Coordinates(1,1);
        instance.setRow(newRow);
        assertTrue(instance.getRow()==newRow);
    }

    /**
     * Test of toString method, of class Coordinates.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Coordinates instance = new Coordinates(1,1);
        String result = instance.toString();
        assertTrue(result instanceof String);
    }

    /**
     * Test of getColumn method, of class Coordinates.
     */
    @Test
    public void testGetColumn() {
        System.out.println("getColumn");
        Coordinates instance = new Coordinates(1,1);
        assertEquals(1, instance.getColumn());
    }

    /**
     * Test of setColumn method, of class Coordinates.
     */
    @Test
    public void testSetColumn() {
        System.out.println("setColumn");
        int newColumn = 0;
        Coordinates instance = new Coordinates(1,1);
        instance.setColumn(newColumn);
        assertTrue(instance.getColumn()==newColumn);
    }
    
}
