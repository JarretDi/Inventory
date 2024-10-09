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

        assertEquals(testSortInventory.getSort().getSort(), "Name");
        assertEquals(testSortInventory.getSort().getOrder(), true);
    }

    @Test
    void testSortReverseAlphabetical() {
        assertEquals(emptySort.getSort(), testInventory.getSort().getSort());
        assertEquals(emptySort.getOrder(), testInventory.getSort().getOrder());
        testSortInventory.sort(new Sort("Name", false));

        assertEquals(testSortInventory.getItem(0), testSort4);
        assertEquals(testSortInventory.getItem(1), testSort3);
        assertEquals(testSortInventory.getItem(2), testSort2);
        assertEquals(testSortInventory.getItem(3), testSort1);

        assertEquals(testSortInventory.getSort().getSort(), "Name");
        assertEquals(testSortInventory.getSort().getOrder(), false);
    }

    @Test
    void testSortType() {
        assertEquals(emptySort.getSort(), testInventory.getSort().getSort());
        assertEquals(emptySort.getOrder(), testInventory.getSort().getOrder());
        testSortInventory.sort(new Sort("Type", true));

        assertEquals(testSortInventory.getItem(0), testSort1);
        assertEquals(testSortInventory.getItem(1), testSort3);
        assertEquals(testSortInventory.getItem(2), testSort2);
        assertEquals(testSortInventory.getItem(3), testSort4);

        assertEquals(testSortInventory.getSort().getSort(), "Type");
        assertEquals(testSortInventory.getSort().getOrder(), true);
    }

    @Test
    void testSortReverseType() {
        assertEquals(emptySort.getSort(), testInventory.getSort().getSort());
        assertEquals(emptySort.getOrder(), testInventory.getSort().getOrder());
        testSortInventory.sort(new Sort("Type", false));

        assertEquals(testSortInventory.getItem(0), testSort4);
        assertEquals(testSortInventory.getItem(1), testSort2);
        assertEquals(testSortInventory.getItem(2), testSort3);
        assertEquals(testSortInventory.getItem(3), testSort1);

        assertEquals(testSortInventory.getSort().getSort(), "Type");
        assertEquals(testSortInventory.getSort().getOrder(), false);
    }

    @Test
    void testSortValue() {
        assertEquals(emptySort.getSort(), testInventory.getSort().getSort());
        assertEquals(emptySort.getOrder(), testInventory.getSort().getOrder());
        testSortInventory.sort(new Sort("Value", true));

        assertEquals(testSortInventory.getItem(0), testSort4);
        assertEquals(testSortInventory.getItem(1), testSort3);
        assertEquals(testSortInventory.getItem(2), testSort2);
        assertEquals(testSortInventory.getItem(3), testSort1);

        assertEquals(testSortInventory.getSort().getSort(), "Value");
        assertEquals(testSortInventory.getSort().getOrder(), true);
    }

    @Test
    void testSortReverseValue() {
        assertEquals(emptySort.getSort(), testInventory.getSort().getSort());
        assertEquals(emptySort.getOrder(), testInventory.getSort().getOrder());
        testSortInventory.sort(new Sort("Value", false));

        assertEquals(testSortInventory.getItem(0), testSort1);
        assertEquals(testSortInventory.getItem(1), testSort2);
        assertEquals(testSortInventory.getItem(2), testSort3);
        assertEquals(testSortInventory.getItem(3), testSort4);

        assertEquals(testSortInventory.getSort().getSort(), "Value");
        assertEquals(testSortInventory.getSort().getOrder(), false);
    }

    @Test
    void testSortWeight() {
        assertEquals(emptySort.getSort(), testInventory.getSort().getSort());
        assertEquals(emptySort.getOrder(), testInventory.getSort().getOrder());
        testSortInventory.sort(new Sort("Weight", true));

        assertEquals(testSortInventory.getItem(0), testSort2);
        assertEquals(testSortInventory.getItem(1), testSort1);
        assertEquals(testSortInventory.getItem(2), testSort3);
        assertEquals(testSortInventory.getItem(3), testSort4);

        assertEquals(testSortInventory.getSort().getSort(), "Weight");
        assertEquals(testSortInventory.getSort().getOrder(), true);
    }

    @Test
    void testSortReverseWeight() {
        assertEquals(emptySort.getSort(), testInventory.getSort().getSort());
        assertEquals(emptySort.getOrder(), testInventory.getSort().getOrder());
        testSortInventory.sort(new Sort("Weight", false));

        assertEquals(testSortInventory.getItem(0), testSort4);
        assertEquals(testSortInventory.getItem(1), testSort3);
        assertEquals(testSortInventory.getItem(2), testSort2);
        assertEquals(testSortInventory.getItem(3), testSort1);

        assertEquals(testSortInventory.getSort().getSort(), "Weight");
        assertEquals(testSortInventory.getSort().getOrder(), false);
    }

    @Test
    void testAddItemSorted(){
        Item testAddItem = new Item("Bc", "Consumable", 9, 10, "");
        testSortInventory.sort(new Sort("Name", true));
        testSortInventory.addItemSorted(testAddItem);

        assertEquals(testSortInventory.getNumItems(), 5);
        assertEquals(testSortInventory.getItem(2), testAddItem);
    }

    @Test
    void testAdd2ItemSorted(){
        Item testAddItem = new Item("Bc", "Weapon", 9, 10, "");
        Item testAddItem2 = new Item("DC", "Currency", 9, 10, "");
        testSortInventory.sort(new Sort("Type", true));
        testSortInventory.addItemSorted(testAddItem);
        testSortInventory.addItemSorted(testAddItem2);

        assertEquals(testSortInventory.getNumItems(), 6);
        assertEquals(testSortInventory.getItem(1), testAddItem);
        assertEquals(testSortInventory.getItem(5), testAddItem2);
    }
}
