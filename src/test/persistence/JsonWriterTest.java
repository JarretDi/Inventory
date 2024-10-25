package persistence;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import model.Inventory;
import model.Sort;
import model.items.Armour;
import model.items.Currency;
import model.items.Misc;
import model.items.Weapon;
import persistence.JsonWriter;

public class JsonWriterTest {
    JsonWriter testJsonWriter;
    Inventory testInventory;

    @Test
    void testFileNotFound() {
        try {
            testInventory = new Inventory();
            testJsonWriter = new JsonWriter("./data/Invalid.json");
            testJsonWriter.open();
            fail();
        } catch (IOException e) {
            // Succeed
        }
    }

    @Test
    void testWriterEmptyInventory() {
        try {
            testInventory = new Inventory();
            testJsonWriter = new JsonWriter("./data/testWriterEmptyInventory.json");
            testJsonWriter.open();
            testJsonWriter.write(testInventory);
            testJsonWriter.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyInventory.json");
            testInventory = reader.read();
            assertEquals("Tester", testInventory.getName());
            assertEquals(0, testInventory.getNumItems());
            assertEquals(new Sort(), testInventory.getSort());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void testWriterInventory() {
        try {
            testInventory = new Inventory();
            Weapon testWeapon = new Weapon("test weapon", 15, 10, "A test weapon.");
            Armour testArmour = new Armour("test armour", 30, 15, "Some test armour.");
            testInventory.addItem(testWeapon);
            testInventory.addItem(testArmour);

            testJsonWriter = new JsonWriter("./data/testWriterInventory.json");
            testJsonWriter.open();
            testJsonWriter.write(testInventory);
            testJsonWriter.close();

            JsonReader reader = new JsonReader("./data/testWriterInventory.json");
            testInventory = reader.read();

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
    void testWriterSortedInventory() {
        try {
            testInventory = new Inventory();
            Armour testArmour = new Armour("test armour", 30, 15, "Some test armour.");
            Misc testMisc = new Misc("test misc", 1, 3, "Some misc.");
            Currency testCurrency = new Currency("test currency", 100, 5, "Some test currency.");
            
            testInventory.addItem(testArmour);
            testInventory.addItem(testMisc);
            testInventory.addItem(testCurrency);
            testInventory.sort(new Sort("Type", false));

            testJsonWriter = new JsonWriter("./data/testWriterSortedInventory.json");
            testJsonWriter.open();
            testJsonWriter.write(testInventory);
            testJsonWriter.close();

            JsonReader reader = new JsonReader("./data/testWriterSortedInventory.json");
            testInventory = reader.read();

            assertEquals("Tester2", testInventory.getName());
            assertEquals(3, testInventory.getNumItems());
            assertEquals(testArmour, testInventory.getItem(0));
            assertEquals(testMisc, testInventory.getItem(1));
            assertEquals(testCurrency, testInventory.getItem(1));
            assertEquals(null, testInventory.getSort().getSort());
            assertEquals(null, testInventory.getSort().getOrder());
        } catch (IOException e) {
            fail();
        }
    }
}
