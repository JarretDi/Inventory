package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InventoryTest {
    Inventory testInventory;
    Item testItem;
    Item testWeapon;
    
    @BeforeEach
    void runBefore() {
        testInventory = new Inventory();
        testItem = new Item("Test Item", "Misc", 1, "A test item.");
        testWeapon = new Item("Test Weapon", "Weapon", 10, "A test weapon.");
    }

    @Test
    void testConstructor() {
        assertEquals(0, testInventory.getNumItems());
    }

    @Test
    void testAddItem() {
        testInventory.addItem(testItem);
        assertEquals(1, testInventory.getNumItems());
    }

    @Test
    void testAddMultipleItems() {
        testInventory.addItem(testItem);
        testInventory.addItem(testItem);
        testInventory.addItem(testWeapon);
        assertEquals(3, testInventory.getNumItems());
    }

    @Test
    void testRemoveItem(){
        testInventory.addItem(testItem);
        testInventory.removeItem(testItem);
        assertEquals(0, testInventory.getNumItems());
    }

    @Test
    void testRemoveMultItems(){
        testInventory.addItem(testItem);
        testInventory.addItem(testWeapon);

        testInventory.removeItem(testItem);
        assertEquals(1, testInventory.getNumItems());
        testInventory.removeItem(testItem);
        assertEquals(1, testInventory.getNumItems());
        testInventory.removeItem(testWeapon);
        assertEquals(0, testInventory.getNumItems());
    }

    @Test
    void testRemoveAllItems(){
        testInventory.addItem(testItem);
        testInventory.addItem(testItem);
        testInventory.addItem(testWeapon);
        testInventory.removeAllItem(testItem);
        assertEquals(1, testInventory.getNumItems());
    }

    @Test
    void testClearInventory(){
        testInventory.addItem(testItem);
        testInventory.addItem(testItem);
        testInventory.addItem(testWeapon);
        testInventory.clearInventory();
        assertEquals(0, testInventory.getNumItems());
    }
}
