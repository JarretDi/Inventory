package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InventoryTest {
    Inventory testInventory;
    Item testItem;
    Item testWeapon;

    Inventory testSortInventory;
    Item testSort1;
    Item testSort2;
    Item testSort3;
    Item testSort4;

    Sort emptySort;
    
    @BeforeEach
    void runBefore() {
        testInventory = new Inventory();
        testItem = new Item("Test Item", "Misc", 1, 5, "A test item.");
        testWeapon = new Item("Test Weapon", "Weapon", 10, 15, "A test weapon.");

        testSortInventory = new Inventory();
        emptySort = new Sort();
        testSort1 = new Item("A", "Weapon", 40, 2, "A test item.");
        testSort2 = new Item("B", "Consumable", 30, 2, "A test item.");
        testSort3 = new Item("C", "Armour", 20, 3, "A test item.");
        testSort4 = new Item("D", "Misc", 10, 4, "A test item.");
        testSortInventory.addItem(testSort2);
        testSortInventory.addItem(testSort4);
        testSortInventory.addItem(testSort1);
        testSortInventory.addItem(testSort3);
    }

    @Test
    void testConstructor() {
        assertEquals(0, testInventory.getNumItems());
        assertEquals(emptySort.getSort(), testInventory.getSort().getSort());
        assertEquals(emptySort.getOrder(), testInventory.getSort().getOrder());
    }

    @Test
    void testAddItem() {
        testInventory.addItem(testItem);
        assertEquals(1, testInventory.getNumItems());
        assertEquals(emptySort.getSort(), testInventory.getSort().getSort());
        assertEquals(emptySort.getOrder(), testInventory.getSort().getOrder());
    }

    @Test
    void testAddMultipleItems() {
        testInventory.addItem(testItem);
        testInventory.addItem(testItem);
        testInventory.addItem(testWeapon);
        assertEquals(3, testInventory.getNumItems());
        assertEquals(emptySort.getSort(), testInventory.getSort().getSort());
        assertEquals(emptySort.getOrder(), testInventory.getSort().getOrder());
    }

    @Test
    void testRemoveItem() {
        testInventory.addItem(testItem);
        testInventory.removeItem(testItem);
        assertEquals(0, testInventory.getNumItems());
    }

    @Test
    void testRemoveMultItems() {
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
    void testRemoveAllItems() {
        testInventory.addItem(testItem);
        testInventory.addItem(testItem);
        testInventory.addItem(testWeapon);
        testInventory.removeAllItem(testItem);
        assertEquals(1, testInventory.getNumItems());
    }

    @Test
    void testClearInventory() {
        testInventory.addItem(testItem);
        testInventory.addItem(testItem);
        testInventory.addItem(testWeapon);
        testInventory.clearInventory();
        assertEquals(0, testInventory.getNumItems());
    }

    @Test
    void testSortAlphabetical() {
        testSortInventory.sort(new Sort("Name", true));

        assertEquals(testSortInventory.getItem(0), testSort1);
        assertEquals(testSortInventory.getItem(1), testSort2);
        assertEquals(testSortInventory.getItem(2), testSort3);
        assertEquals(testSortInventory.getItem(3), testSort4);
    }

    @Test
    void testSortReverseAlphabetical() {
        testSortInventory.sort(new Sort("Name", false));

        assertEquals(testSortInventory.getItem(0), testSort4);
        assertEquals(testSortInventory.getItem(1), testSort3);
        assertEquals(testSortInventory.getItem(2), testSort2);
        assertEquals(testSortInventory.getItem(3), testSort1);
    }

    @Test
    void testSortType() {
        testSortInventory.sort(new Sort("Type", true));

        assertEquals(testSortInventory.getItem(0), testSort1);
        assertEquals(testSortInventory.getItem(1), testSort3);
        assertEquals(testSortInventory.getItem(2), testSort2);
        assertEquals(testSortInventory.getItem(3), testSort4);
    }

    @Test
    void testSortReverseType() {
        testSortInventory.sort(new Sort("Type", false));

        assertEquals(testSortInventory.getItem(0), testSort4);
        assertEquals(testSortInventory.getItem(1), testSort2);
        assertEquals(testSortInventory.getItem(2), testSort3);
        assertEquals(testSortInventory.getItem(3), testSort1);
    }

    @Test
    void testSortValue() {
        testSortInventory.sort(new Sort("Value", true));

        assertEquals(testSortInventory.getItem(0), testSort4);
        assertEquals(testSortInventory.getItem(1), testSort3);
        assertEquals(testSortInventory.getItem(2), testSort2);
        assertEquals(testSortInventory.getItem(3), testSort1);
    }

    @Test
    void testSortReverseValue() {
        testSortInventory.sort(new Sort("Value", false));

        assertEquals(testSortInventory.getItem(0), testSort1);
        assertEquals(testSortInventory.getItem(1), testSort2);
        assertEquals(testSortInventory.getItem(2), testSort3);
        assertEquals(testSortInventory.getItem(3), testSort4);
    }

    @Test
    void testSortWeight() {
        testSortInventory.sort(new Sort("Weight", true));

        assertEquals(testSortInventory.getItem(0), testSort2);
        assertEquals(testSortInventory.getItem(1), testSort1);
        assertEquals(testSortInventory.getItem(2), testSort3);
        assertEquals(testSortInventory.getItem(3), testSort4);
    }

    @Test
    void testSortReverseWeight() {
        testSortInventory.sort(new Sort("Weight", false));

        assertEquals(testSortInventory.getItem(0), testSort4);
        assertEquals(testSortInventory.getItem(1), testSort3);
        assertEquals(testSortInventory.getItem(2), testSort1);
        assertEquals(testSortInventory.getItem(3), testSort2);
    }
}
