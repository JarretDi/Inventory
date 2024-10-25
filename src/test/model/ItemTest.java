package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Exceptions.ItemCreationException;
import model.items.Armour;
import model.items.Consumable;
import model.items.Currency;
import model.items.Item;
import model.items.ItemCreator;
import model.items.Misc;
import model.items.Weapon;

public class ItemTest {
    Item testItem;

    @BeforeEach
    void setup() {
        testItem = new Misc("Test Item", 20, 5, "A test item.");
    }

    @Test
    void testConstructor() {
        assertEquals("Test Item", testItem.getName());
        assertEquals("Misc", testItem.getType());
        assertEquals(20, testItem.getValue());
        assertEquals(5, testItem.getWeight());
        assertEquals("A test item.", testItem.getDescription());
        assertEquals(testItem.isFavourite(), false);
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
    void testInvalidItemCreator() {
        ItemCreator itemCreator = new ItemCreator();
        try {
            Item testItem1 = ItemCreator.createItemFromInput("A", "Spell");
            fail();
        } catch (ItemCreationException e) {
            //pass
        }
        try {
            Item testItem2 = ItemCreator.createItemFromInput("A", "Attack", 15, 6, "Attack");
            fail();
        } catch (ItemCreationException e) {
            //pass
        }
    }

    @Test
    void testCorrectTypesItemCreator() {
        ItemCreator itemCreator = new ItemCreator();
        try {
            Item testW1 = ItemCreator.createItemFromInput("A", "Weapon");
            Item testW2 = ItemCreator.createItemFromInput("A", "Weapon", 5, 10, "t");
            Item testA1 = ItemCreator.createItemFromInput("B", "Armour");
            Item testA2 = ItemCreator.createItemFromInput("B", "Armour", 10, 5, "te");
            Item testCo1 = ItemCreator.createItemFromInput("C", "Consumable");
            Item testCo2 = ItemCreator.createItemFromInput("C", "Consumable", 20, 10, "tes");
            Item testM1 = ItemCreator.createItemFromInput("D", "Misc");
            Item testM2 = ItemCreator.createItemFromInput("D", "Misc", 10, 5, "test");
            Item testCu1 = ItemCreator.createItemFromInput("E", "Currency");
            Item testCu2 = ItemCreator.createItemFromInput("E", "Currency", 100, 20, "");

            assertEquals(testW1.getType(), "Weapon");
            assertEquals(testW2.getType(), "Weapon");
            assertEquals(testA1.getType(), "Armour");
            assertEquals(testA2.getType(), "Armour");
            assertEquals(testCo1.getType(), "Consumable");
            assertEquals(testCo2.getType(), "Consumable");
            assertEquals(testM1.getType(), "Misc");
            assertEquals(testM2.getType(), "Misc");
            assertEquals(testCu1.getType(), "Currency");
            assertEquals(testCu2.getType(), "Currency");
        } catch (ItemCreationException e) {
            fail();
        }
    }

    @Test
    void testGetTypePriority() {
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

    @Test
    void testGetPriority() {
        assertEquals(testItem.getPriority(new Sort("Name", true)), testItem.getName().compareTo(" "));
        assertEquals(testItem.getPriority(new Sort("Name", false)), -testItem.getName().compareTo(" "));

        assertEquals(testItem.getPriority(new Sort("Type", true)), 4);
        assertEquals(testItem.getPriority(new Sort("Type", false)), -4);

        assertEquals(testItem.getPriority(new Sort("Value", true)), 20);
        assertEquals(testItem.getPriority(new Sort("Value", false)), -20);

        assertEquals(testItem.getPriority(new Sort("Weight", true)), 5);
        assertEquals(testItem.getPriority(new Sort("Weight", false)), -5);
    }

    @Test
    void testSetFavourite() {
        testItem.setFavourite();
        assertEquals(testItem.isFavourite(), true);
    }

    @Test
    void testUnsetFavourite() {
        testItem.setFavourite();
        testItem.setUnfavourite();
        assertEquals(testItem.isFavourite(), false);
    }
}
