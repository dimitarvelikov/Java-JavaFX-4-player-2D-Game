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
public class ChanceCardTest {
    
    public ChanceCardTest() {
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
     * Test of getCardNumber method, of class ChanceCard.
     */
    @Test
    public void testGetCardNumber() {
        System.out.println("getCardNumber");
        ChanceCard instance = new ChanceCard(1,"Test");
        int expResult = 1;
        int result = instance.getCardNumber();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCardDescription method, of class ChanceCard.
     */
    @Test
    public void testGetCardDescription() {
        System.out.println("getCardDescription");
        ChanceCard instance = new ChanceCard(1,"Test");
        String expResult = "Test";
        String result = instance.getCardDescription();
        assertEquals(expResult, result);
    }
    
}
