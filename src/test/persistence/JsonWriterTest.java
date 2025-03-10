package persistence;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import model.Inventory;
import model.Sort;
import model.Sort.SortType;
import model.exceptions.InvalidSortException;
import model.items.Armour;
import model.items.Currency;
import model.items.Item;
import model.items.Misc;
import model.items.Weapon;

public class JsonWriterTest {
    JsonWriter testJsonWriter;
    Inventory testInventory;

    @Test
    void testFileNotFound() {
        try {
            testInventory = new Inventory();
            testJsonWriter = new JsonWriter("./data/my\0illegal:fileName.json");
            testJsonWriter.open();
            fail();
        } catch (IOException e) {
            // Succeed
        }
    }

    @Test
    void testWriterEmptyInventory() {
        try {
            testInventory = new Inventory("Tester");
            testJsonWriter = new JsonWriter("./data/testWriterEmptyInventory.json");
            testJsonWriter.open();
            testJsonWriter.write(testInventory);
            testJsonWriter.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyInventory.json");
            testInventory = reader.read();
            assertEquals("Tester", testInventory.getCharacter());
            assertEquals(0, testInventory.getNumItems());
            assertTrue(testInventory.getSort().isUnsorted());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void testWriterEmptySortedInventory() {
        try {
            testInventory = new Inventory("Tester");
            testInventory.sort(new Sort(SortType.Weight, true));
            testJsonWriter = new JsonWriter("./data/testWriterEmptySortedInventory.json");
            testJsonWriter.open();
            testJsonWriter.write(testInventory);
            testJsonWriter.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptySortedInventory.json");
            testInventory = reader.read();
            assertEquals("Tester", testInventory.getCharacter());
            assertEquals(0, testInventory.getNumItems());

            assertEquals(SortType.Weight, testInventory.getSort().getSort());
            assertTrue(testInventory.getSort().getOrder());
        } catch (IOException e) {
            fail();
        } catch (InvalidSortException e) {
            fail();
        }
    }

    @Test
    void testWriterInventory() {
        try {
            testInventory = new Inventory("Tester1");
            Weapon testWeapon = new Weapon("test weapon", 15, 10, "A test weapon.", false);
            Armour testArmour = new Armour("test armour", 30, 15, "Some test armour.", false);
            testWeapon.setFavourite();
            testArmour.setFavourite();
            testInventory.addItem(testWeapon);
            testInventory.addItem(testArmour);

            testJsonWriter = new JsonWriter("./data/testWriterInventory.json");
            testJsonWriter.open();
            testJsonWriter.write(testInventory);
            testJsonWriter.close();

            JsonReader reader = new JsonReader("./data/testWriterInventory.json");
            testInventory = reader.read();

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
            assertEquals(testArmour.isFavourite(), item0.isFavourite());

            assertTrue(testInventory.getSort().isUnsorted());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void testWriterSortedInventory() {
        try {
            testInventory = new Inventory("Tester2");
            Armour testArmour = new Armour("test armour", 30, 15, "Some test armour.", false);
            Misc testMisc = new Misc("test misc", 1, 3, "Some misc.", false);
            Currency testCurrency = new Currency("test currency", 100, 5, "Some test currency.", false);
            
            testInventory.addItem(testArmour);
            testInventory.addItem(testMisc);
            testInventory.addItem(testCurrency);
            try {
                testInventory.sort(new Sort(SortType.Type, false));
            } catch (InvalidSortException e) {
                fail();
            }

            testJsonWriter = new JsonWriter("./data/testWriterSortedInventory.json");
            testJsonWriter.open();
            testJsonWriter.write(testInventory);
            testJsonWriter.close();

            JsonReader reader = new JsonReader("./data/testWriterSortedInventory.json");
            testInventory = reader.read();

            assertEquals("Tester2", testInventory.getCharacter());
            assertEquals(3, testInventory.getNumItems());
            
            Item item0 = testInventory.getItem(0);
            assertEquals(testCurrency.getName(), item0.getName());
            assertEquals(testCurrency.getType(), item0.getType());
            assertEquals(testCurrency.getValue(), item0.getValue());
            assertEquals(testCurrency.getWeight(), item0.getWeight());
            assertEquals(testCurrency.getDescription(), item0.getDescription());
            assertEquals(testCurrency.isFavourite(), item0.isFavourite());

            Item item1 = testInventory.getItem(1);
            assertEquals(testMisc.getName(), item1.getName());
            assertEquals(testMisc.getType(), item1.getType());
            assertEquals(testMisc.getValue(), item1.getValue());
            assertEquals(testMisc.getWeight(), item1.getWeight());
            assertEquals(testMisc.getDescription(), item1.getDescription());
            assertEquals(testMisc.isFavourite(), item1.isFavourite());
            
            Item item2 = testInventory.getItem(2);
            assertEquals(testArmour.getName(), item2.getName());
            assertEquals(testArmour.getType(), item2.getType());
            assertEquals(testArmour.getValue(), item2.getValue());
            assertEquals(testArmour.getWeight(), item2.getWeight());
            assertEquals(testArmour.getDescription(), item2.getDescription());
            assertEquals(testArmour.isFavourite(), item2.isFavourite());

            assertEquals(testInventory.getSort().getSort(), SortType.Type);
            assertFalse(testInventory.getSort().getOrder());
        } catch (IOException e) {
            fail();
        }
    }
}
