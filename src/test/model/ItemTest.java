package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.items.Armour;
import model.items.Consumable;
import model.items.Currency;
import model.items.Item;
import model.items.Misc;
import model.items.Weapon;

public class ItemTest {
    Item testItem;

    @BeforeEach
    void setup() {
        testItem = new Misc("Test Item", 1, 5, "A test item.");
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
    void testCorrectTypes() {
        Item testItem1 = new Weapon("A");
        Item testItem2 = new Armour("B");
        Item testItem3 = new Consumable("C");
        Item testItem4 = new Misc("D");
        Item testItem5 = new Currency("E");

        assertEquals(testItem1.getType(), "Weapon");
        assertEquals(testItem2.getType(), "Armour");
        assertEquals(testItem3.getType(), "Consumable");
        assertEquals(testItem4.getType(), "Misc");
        assertEquals(testItem5.getType(), "Currency");
    }

    @Test
    void testPriority() {
        Item testItem1 = new Weapon("A");
        Item testItem2 = new Armour("B");
        Item testItem3 = new Consumable("C");
        Item testItem4 = new Misc("D");
        Item testItem5 = new Currency("E");
        
        assertEquals(testItem1.getTypePriority(), 1);
        assertEquals(testItem2.getTypePriority(), 2);
        assertEquals(testItem3.getTypePriority(), 3);
        assertEquals(testItem4.getTypePriority(), 4);
        assertEquals(testItem5.getTypePriority(), 5);
    }
}
