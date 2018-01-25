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
public class CrewCardTest {
    
    public CrewCardTest() {
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
     * Test of isBlack method, of class CrewCard.
     */
    @Test
    public void testIsBlack() {
        System.out.println("isBlack");
        CrewCard instance = new CrewCard(2,false);
        boolean expResult = false;
        boolean result = instance.isBlack();
        assertEquals(expResult, result);
    }

    /**
     * Test of setColor method, of class CrewCard.
     */
   /*@Test
    public void testSetColor() {
        System.out.println("setColor");
        boolean color = false;
        CrewCard instance = new CrewCard();
        instance.setColor(color);
        assertEquals(instance.isBlack(),color);
    }*/

    /**
     * Test of getValue method, of class CrewCard.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        CrewCard instance = new CrewCard(0,true);
        int expResult = 0;
        int result = instance.getValue();
        assertEquals(expResult, result);
    }

    /**
     * Test of setValue method, of class CrewCard.
     */
    /*@Test
    public void testSetValue() {
        System.out.println("setValue");
        int value = 0;
        CrewCard instance = new CrewCard();
        instance.setValue(value);
        assertEquals(instance.getValue(),value);
    }*/
    
}
