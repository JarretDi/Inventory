package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.items.Armour;
import model.items.Consumable;
import model.items.Currency;
import model.items.Item;
import model.items.Misc;
import model.items.Weapon;

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
        testItem = new Misc("Test Item", 1, 5, "A test item.");
        testWeapon = new Weapon("Test Weapon", 10, 15, "A test weapon.");

        testSortInventory = new Inventory();
        emptySort = new Sort();
        testSort1 = new Weapon("A", 40, 2, "A test item.");
        testSort2 = new Consumable("B", 30, 2, "A test item.");
        testSort3 = new Armour("C", 20, 3, "A test item.");
        testSort4 = new Currency("D", 10, 4, "A test item.");
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

        assertEquals(testInventory.getInventory().size(), 0);
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
        assertEquals(2, testInventory.getItemCount(testItem));
        assertEquals(1, testInventory.getItemCount(testWeapon));

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
        assertEquals(emptySort.getSort(), testInventory.getSort().getSort());
        assertEquals(emptySort.getOrder(), testInventory.getSort().getOrder());
        assertEquals(testSortInventory.getNumItems(), 4);

        testSortInventory.sort(new Sort("Name", true));

        assertEquals(testSortInventory.getNumItems(), 4);

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
        assertEquals(testSortInventory.getNumItems(), 4);

        testSortInventory.sort(new Sort("Name", false));

        assertEquals(testSortInventory.getNumItems(), 4);

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
        assertEquals(testSortInventory.getNumItems(), 4);

        testSortInventory.sort(new Sort("Type", true));

        assertEquals(testSortInventory.getNumItems(), 4);

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
        assertEquals(testSortInventory.getNumItems(), 4);

        testSortInventory.sort(new Sort("Type", false));

        assertEquals(testSortInventory.getNumItems(), 4);

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
        assertEquals(testSortInventory.getNumItems(), 4);

        testSortInventory.sort(new Sort("Value", true));

        assertEquals(testSortInventory.getNumItems(), 4);

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
        assertEquals(testSortInventory.getNumItems(), 4);

        testSortInventory.sort(new Sort("Value", false));

        assertEquals(testSortInventory.getNumItems(), 4);

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
        assertEquals(testSortInventory.getNumItems(), 4);

        testSortInventory.sort(new Sort("Weight", true));

        assertEquals(testSortInventory.getNumItems(), 4);

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
        assertEquals(testSortInventory.getNumItems(), 4);

        testSortInventory.sort(new Sort("Weight", false));

        assertEquals(testSortInventory.getNumItems(), 4);

        assertEquals(testSortInventory.getItem(0), testSort4);
        assertEquals(testSortInventory.getItem(1), testSort3);
        assertEquals(testSortInventory.getItem(2), testSort2);
        assertEquals(testSortInventory.getItem(3), testSort1);

        assertEquals(testSortInventory.getSort().getSort(), "Weight");
        assertEquals(testSortInventory.getSort().getOrder(), false);
    }

    @Test
    void testSortTwice() {
        assertEquals(testSortInventory.getNumItems(), 4);

        testSortInventory.sort(new Sort("Weight", false));
        testSortInventory.sort(new Sort("Name", true));

        assertEquals(testSortInventory.getNumItems(), 4);

        assertEquals(testSortInventory.getItem(0), testSort1);
        assertEquals(testSortInventory.getItem(1), testSort2);
        assertEquals(testSortInventory.getItem(2), testSort3);
        assertEquals(testSortInventory.getItem(3), testSort4);

        assertEquals(testSortInventory.getSort().getSort(), "Name");
        assertEquals(testSortInventory.getSort().getOrder(), true);
    }

    @Test
    void testAddItemSorted() {
        Item testAddItem = new Consumable("Bc", 9, 10, "");
        testSortInventory.sort(new Sort("Name", true));
        testSortInventory.addItemSorted(testAddItem);

        assertEquals(testSortInventory.getNumItems(), 5);
        assertEquals(testSortInventory.getItem(2), testAddItem);
    }

    @Test
    void testAdd2ItemSorted() {
        Item testAddItem = new Weapon("Bc", 9, 10, "");
        Item testAddItem2 = new Currency("DC", 9, 10, "");
        testSortInventory.sort(new Sort("Type", true));
        testSortInventory.addItemSorted(testAddItem);
        testSortInventory.addItemSorted(testAddItem2);

        assertEquals(testSortInventory.getNumItems(), 6);
        assertEquals(testSortInventory.getItem(1), testAddItem);
        assertEquals(testSortInventory.getItem(5), testAddItem2);
    }

    @Test
    void testAddItemReverseSorted() {
        Item testAddItem = new Consumable("Bc", 15, 10, "");
        testSortInventory.sort(new Sort("Value", false));
        testSortInventory.addItemSorted(testAddItem);

        assertEquals(testSortInventory.getNumItems(), 5);
        assertEquals(testSortInventory.getItem(3), testAddItem);
    }

    @Test
    void testAdd2ItemReverseSorted() {
        Item testAddItem = new Weapon("Bc", 9, 100, "");
        Item testAddItem2 = new Currency("DC", 9, 0, "");
        testSortInventory.sort(new Sort("Weight", false));
        testSortInventory.addItemSorted(testAddItem);
        testSortInventory.addItemSorted(testAddItem2);

        assertEquals(testSortInventory.getNumItems(), 6);
        assertEquals(testSortInventory.getItem(0), testAddItem);
        assertEquals(testSortInventory.getItem(5), testAddItem2);
    }
}
