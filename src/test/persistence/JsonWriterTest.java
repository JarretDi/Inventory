package persistence;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import model.Inventory;
import model.Sort;
import model.items.Armour;
import model.items.Currency;
import model.items.Item;
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
    void testWriterInventory() {
        try {
            testInventory = new Inventory("Tester1");
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

            assertEquals("Tester1", testInventory.getCharacter());
            assertEquals(2, testInventory.getNumItems());
            
            Item item0 = testInventory.getItem(0);
            assertEquals(testWeapon.getName(), item0.getName());
            assertEquals(testWeapon.getType(), item0.getType());
            assertEquals(testWeapon.getValue(), item0.getValue());
            assertEquals(testWeapon.getWeight(), item0.getWeight());
            assertEquals(testWeapon.getDescription(), item0.getDescription());

            Item item1 = testInventory.getItem(1);
            assertEquals(testArmour.getName(), item1.getName());
            assertEquals(testArmour.getType(), item1.getType());
            assertEquals(testArmour.getValue(), item1.getValue());
            assertEquals(testArmour.getWeight(), item1.getWeight());
            assertEquals(testArmour.getDescription(), item1.getDescription());

            assertTrue(testInventory.getSort().isUnsorted());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void testWriterSortedInventory() {
        try {
            testInventory = new Inventory("Tester2");
            Armour testArmour = new Armour("test armour", 30, 15, "Some test armour.");
            Misc testMisc = new Misc("test misc", 1, 3, "Some misc.");
            Currency testCurrency = new Currency("test currency", 100, 5, "Some test currency.");
            
            testInventory.addItem(testArmour);
            testInventory.addItem(testMisc);
            testInventory.addItem(testCurrency);
            testInventory.sort(new Sort("Type", true));

            testJsonWriter = new JsonWriter("./data/testWriterSortedInventory.json");
            testJsonWriter.open();
            testJsonWriter.write(testInventory);
            testJsonWriter.close();

            JsonReader reader = new JsonReader("./data/testWriterSortedInventory.json");
            testInventory = reader.read();

            assertEquals("Tester2", testInventory.getCharacter());
            assertEquals(3, testInventory.getNumItems());
            
            Item item0 = testInventory.getItem(0);
            assertEquals(testArmour.getName(), item0.getName());
            assertEquals(testArmour.getType(), item0.getType());
            assertEquals(testArmour.getValue(), item0.getValue());
            assertEquals(testArmour.getWeight(), item0.getWeight());
            assertEquals(testArmour.getDescription(), item0.getDescription());

            Item item1 = testInventory.getItem(1);
            assertEquals(testMisc.getName(), item1.getName());
            assertEquals(testMisc.getType(), item1.getType());
            assertEquals(testMisc.getValue(), item1.getValue());
            assertEquals(testMisc.getWeight(), item1.getWeight());
            assertEquals(testMisc.getDescription(), item1.getDescription());
            
            Item item2 = testInventory.getItem(2);
            assertEquals(testCurrency.getName(), item2.getName());
            assertEquals(testCurrency.getType(), item2.getType());
            assertEquals(testCurrency.getValue(), item2.getValue());
            assertEquals(testCurrency.getWeight(), item2.getWeight());
            assertEquals(testCurrency.getDescription(), item2.getDescription());

            assertEquals(testInventory.getSort().getSort(), "Type");
            assertEquals(testInventory.getSort().getOrder(), true);
        } catch (IOException e) {
            fail();
        }
    }
}
