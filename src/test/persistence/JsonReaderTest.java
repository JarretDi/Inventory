package persistence;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import model.Inventory;
import model.Sort;
import model.items.Armour;
import model.items.Currency;
import model.items.Misc;
import model.items.Weapon;

public class JsonReaderTest {
    JsonReader testReader;

    @Test
    void testFileNotFound() {
        testReader = new JsonReader("./data/error404filenotfound.json");
        try {
            Inventory testInventory = testReader.read();
            fail();
        } catch (IOException e) {
            // Succeed
        }
    }

    @Test
    void testReaderEmptyInventory() {
        testReader = new JsonReader("./data/testReaderEmptyInventory.json");
        try {
            Inventory testInventory = testReader.read();
            assertEquals("Tester", testInventory.getName());
            assertEquals(0, testInventory.getNumItems());
            assertEquals(new Sort(), testInventory.getSort());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void testReaderInventory() {
        testReader = new JsonReader("./data/testReaderInventory.json");
        Weapon testWeapon = new Weapon("test weapon", 15, 10, "A test weapon.");
        Armour testArmour = new Armour("test armour", 30, 15, "Some test armour.");

        try {
            Inventory testInventory = testReader.read();
            assertEquals("Tester1", testInventory.getName());
            assertEquals(2, testInventory.getNumItems());
            assertEquals(testWeapon, testInventory.getItem(0));
            assertEquals(testArmour, testInventory.getItem(1));
            assertEquals(null, testInventory.getSort().getSort());
            assertEquals(null, testInventory.getSort().getOrder());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void testReaderSortedInventory() {
        testReader = new JsonReader("./data/testReaderInventory.json");
        Misc testMisc = new Misc("test misc", 1, 3, "Some misc.");
        Armour testArmour = new Armour("test armour", 30, 15, "Some test armour.");
        Currency testCurrency = new Currency("test currency", 100, 5, "Some test currency.");

        try {
            Inventory testInventory = testReader.read();
            assertEquals("Tester2", testInventory.getName());
            assertEquals(3, testInventory.getNumItems());
            assertEquals(testMisc, testInventory.getItem(0));
            assertEquals(testArmour, testInventory.getItem(1));
            assertEquals(testCurrency, testInventory.getItem(3));
            assertEquals(new Sort("Value", true), testInventory.getSort());
        } catch (IOException e) {
            fail();
        }
    }
}
