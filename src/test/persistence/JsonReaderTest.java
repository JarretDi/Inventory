package persistence;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import model.Inventory;
import model.Sort;
import model.Sort.SortType;
import model.items.Armour;
import model.items.Currency;
import model.items.Item;
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
            assertEquals("Tester", testInventory.getCharacter());
            assertEquals(0, testInventory.getNumItems());
            assertEquals(null, testInventory.getSort().getSort());
            assertEquals(null, testInventory.getSort().getOrder());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void testReaderInventory() {
        testReader = new JsonReader("./data/testReaderInventory.json");
        Weapon testWeapon = new Weapon("test weapon", 15, 10, "A test weapon.");
        testWeapon.setFavourite();
        Armour testArmour = new Armour("test armour", 30, 15, "Some test armour.");
        testArmour.setFavourite();

        try {
            Inventory testInventory = testReader.read();
            assertEquals("Tester1", testInventory.getCharacter());
            assertEquals(2, testInventory.getNumItems());

            Item item0 = testInventory.getItem(0);
            assertEquals(testWeapon.getName(), item0.getName());
            assertEquals(testWeapon.getType(), item0.getType());
            assertEquals(testWeapon.getValue(), item0.getValue());
            assertEquals(testWeapon.getWeight(), item0.getWeight());
            assertEquals(testWeapon.getDescription(), item0.getDescription());
            assertEquals(testWeapon.isFavourite(), item0.isFavourite());

            Item item1 = testInventory.getItem(1);
            assertEquals(testArmour.getName(), item1.getName());
            assertEquals(testArmour.getType(), item1.getType());
            assertEquals(testArmour.getValue(), item1.getValue());
            assertEquals(testArmour.getWeight(), item1.getWeight());
            assertEquals(testArmour.getDescription(), item1.getDescription());
            assertEquals(testArmour.isFavourite(), item1.isFavourite());

            assertEquals(null, testInventory.getSort().getSort());
            assertEquals(null, testInventory.getSort().getOrder());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void testReaderSortedInventory() {
        testReader = new JsonReader("./data/testReaderSortedInventory.json");
        Misc testMisc = new Misc("test misc", 1, 3, "Some misc.");
        Armour testArmour = new Armour("test armour", 30, 15, "Some test armour.");
        Currency testCurrency = new Currency("test currency", 100, 5, "Some test currency.");

        try {
            Inventory testInventory = testReader.read();
            assertEquals("Tester2", testInventory.getCharacter());
            assertEquals(3, testInventory.getNumItems());

            Item item0 = testInventory.getItem(0);
            assertEquals(testMisc.getName(), item0.getName());
            assertEquals(testMisc.getType(), item0.getType());
            assertEquals(testMisc.getValue(), item0.getValue());
            assertEquals(testMisc.getWeight(), item0.getWeight());
            assertEquals(testMisc.getDescription(), item0.getDescription());
            assertEquals(testMisc.isFavourite(), item0.isFavourite());

            Item item1 = testInventory.getItem(1);
            assertEquals(testArmour.getName(), item1.getName());
            assertEquals(testArmour.getType(), item1.getType());
            assertEquals(testArmour.getValue(), item1.getValue());
            assertEquals(testArmour.getWeight(), item1.getWeight());
            assertEquals(testArmour.getDescription(), item1.getDescription());
            assertEquals(testMisc.isFavourite(), item1.isFavourite());

            Item item2 = testInventory.getItem(2);
            assertEquals(testCurrency.getName(), item2.getName());
            assertEquals(testCurrency.getType(), item2.getType());
            assertEquals(testCurrency.getValue(), item2.getValue());
            assertEquals(testCurrency.getWeight(), item2.getWeight());
            assertEquals(testCurrency.getDescription(), item2.getDescription());
            assertEquals(testMisc.isFavourite(), item2.isFavourite());

            assertEquals(SortType.Value, testInventory.getSort().getSort());
            assertFalse(testInventory.getSort().getOrder());
        } catch (IOException e) {
            fail();
        }
    }
}
