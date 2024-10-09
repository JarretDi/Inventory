package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ItemTest {
    Item testItem;

    @BeforeEach
    void setup() {
        testItem = new Item("Test Item", "Misc", 1, 5, "A test item.");
    }

    @Test
    void testConstructor() {
        assertEquals("Test Item", testItem.getName());
        assertEquals("Misc", testItem.getType());
        assertEquals(1, testItem.getValue());
        assertEquals(5, testItem.getWeight());
        assertEquals("A test item.", testItem.getDescription());
    }

    @Test
    void testPriority() {
        Item testItem1 = new Item("A", "Weapon", 0, 0, "A test item.");
        Item testItem2 = new Item("A", "Armour", 0, 0, "A test item.");
        Item testItem3 = new Item("A", "Consumable", 0, 0, "A test item.");
        Item testItem4 = new Item("A", "Misc", 0, 0, "A test item.");
        Item testItem5 = new Item("A", "Currency", 0, 0, "A test item.");
        assertEquals(testItem1.getTypePriority(), 1);
        assertEquals(testItem2.getTypePriority(), 2);
        assertEquals(testItem3.getTypePriority(), 3);
        assertEquals(testItem4.getTypePriority(), 4);
        assertEquals(testItem5.getTypePriority(), 5);
    }
}
