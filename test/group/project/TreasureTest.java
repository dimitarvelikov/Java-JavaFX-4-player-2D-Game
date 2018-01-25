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
public class TreasureTest {
    
    public TreasureTest() {
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
     * Test of getValue method, of class Treasure.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        Treasure instance = new Treasure("Rubies");
        int expResult = 5;
        int result = instance.getValue();
        assertEquals(expResult, result);
    }

    /**
     * Test of getType method, of class Treasure.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        Treasure instance = new Treasure("Rubies");
        String expResult = "Rubies";
        String result = instance.getType();
        assertEquals(expResult, result);
    }

    /**
     * Test of setValue method, of class Treasure.
     */
    /*@Test
    public void testSetValue() {
        System.out.println("setValue");
        int newVal = 1;
        Treasure instance = new Treasure("Barrels of rum");
        instance.setValue(newVal);
        assertEquals(newVal,instance.getValue());
    }*/
    
}
