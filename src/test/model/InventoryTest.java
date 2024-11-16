package model;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Sort.SortType;
import model.exceptions.InvalidSortException;
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
        testInventory = new Inventory("Tester");
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
        assertEquals("Tester", testInventory.getCharacter());
        assertEquals(0, testInventory.getNumItems());
        assertEquals(emptySort.getSort(), testInventory.getSort().getSort());
        assertEquals(emptySort.getOrder(), testInventory.getSort().getOrder());

        assertEquals(testInventory.getInventory().size(), 0);
    }

    @Test
    void testCharacter() {
        assertEquals("Tester", testInventory.getCharacter());
        testInventory.setCharacter("Tess ter");
        assertEquals("Tess ter", testInventory.getCharacter());
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
        assertEquals(2, testInventory.getCount(testItem));
        assertEquals(1, testInventory.getCount(testWeapon));

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
    void testSetAllFavourite() {
        testSortInventory.setAllFavourite();

        assertTrue(testSortInventory.getItem(0).isFavourite());
        assertTrue(testSortInventory.getItem(1).isFavourite());
        assertTrue(testSortInventory.getItem(2).isFavourite());
        assertTrue(testSortInventory.getItem(3).isFavourite());
    }

    @Test
    void testSetAllUnfavourite() {
        testSortInventory.setAllFavourite();
        testSortInventory.setAllUnfavourite();

        assertFalse(testSortInventory.getItem(0).isFavourite());
        assertFalse(testSortInventory.getItem(1).isFavourite());
        assertFalse(testSortInventory.getItem(2).isFavourite());
        assertFalse(testSortInventory.getItem(3).isFavourite());
    }

    @Test
    void testUnsortedSort() {
        try {
            testSortInventory.sort(new Sort());
            fail();
        } catch (InvalidSortException e) {
            assertTrue(testSortInventory.getSort().isUnsorted());
        }
    }

    @Test
    void testSortAlphabetical() {
        assertEquals(emptySort.getSort(), testInventory.getSort().getSort());
        assertEquals(emptySort.getOrder(), testInventory.getSort().getOrder());
        assertEquals(testSortInventory.getNumItems(), 4);

        try {
            testSortInventory.sort(new Sort(SortType.Name, true));
        } catch (InvalidSortException e) {
            fail();
        }

        assertEquals(testSortInventory.getNumItems(), 4);

        assertEquals(testSortInventory.getItem(0), testSort1);
        assertEquals(testSortInventory.getItem(1), testSort2);
        assertEquals(testSortInventory.getItem(2), testSort3);
        assertEquals(testSortInventory.getItem(3), testSort4);
        
        assertEquals(testSortInventory.getSort().getSort(), SortType.Name);
        assertEquals(testSortInventory.getSort().getOrder(), true);
    }

    @Test
    void testSortReverseAlphabetical() {
        assertEquals(emptySort.getSort(), testInventory.getSort().getSort());
        assertEquals(emptySort.getOrder(), testInventory.getSort().getOrder());
        assertEquals(testSortInventory.getNumItems(), 4);

        try {
            testSortInventory.sort(new Sort(SortType.Name, false));
        } catch (InvalidSortException e) {
            fail();
        }

        assertEquals(testSortInventory.getNumItems(), 4);

        assertEquals(testSortInventory.getItem(0), testSort4);
        assertEquals(testSortInventory.getItem(1), testSort3);
        assertEquals(testSortInventory.getItem(2), testSort2);
        assertEquals(testSortInventory.getItem(3), testSort1);

        assertEquals(testSortInventory.getSort().getSort(), SortType.Name);
        assertEquals(testSortInventory.getSort().getOrder(), false);
    }

    @Test
    void testSortType() {
        assertEquals(emptySort.getSort(), testInventory.getSort().getSort());
        assertEquals(emptySort.getOrder(), testInventory.getSort().getOrder());
        assertEquals(testSortInventory.getNumItems(), 4);

        try {
            testSortInventory.sort(new Sort(SortType.Type, true));
        } catch (InvalidSortException e) {
            fail();
        }

        assertEquals(testSortInventory.getNumItems(), 4);

        assertEquals(testSortInventory.getItem(0), testSort1);
        assertEquals(testSortInventory.getItem(1), testSort3);
        assertEquals(testSortInventory.getItem(2), testSort2);
        assertEquals(testSortInventory.getItem(3), testSort4);

        assertEquals(testSortInventory.getSort().getSort(), SortType.Type);
        assertEquals(testSortInventory.getSort().getOrder(), true);
    }

    @Test
    void testSortReverseType() {
        assertEquals(emptySort.getSort(), testInventory.getSort().getSort());
        assertEquals(emptySort.getOrder(), testInventory.getSort().getOrder());
        assertEquals(testSortInventory.getNumItems(), 4);

        try {
            testSortInventory.sort(new Sort(SortType.Type, false));
        } catch (InvalidSortException e) {
            fail();
        }

        assertEquals(testSortInventory.getNumItems(), 4);

        assertEquals(testSortInventory.getItem(0), testSort4);
        assertEquals(testSortInventory.getItem(1), testSort2);
        assertEquals(testSortInventory.getItem(2), testSort3);
        assertEquals(testSortInventory.getItem(3), testSort1);

        assertEquals(testSortInventory.getSort().getSort(), SortType.Type);
        assertEquals(testSortInventory.getSort().getOrder(), false);
    }

    @Test
    void testSortValue() {
        assertEquals(emptySort.getSort(), testInventory.getSort().getSort());
        assertEquals(emptySort.getOrder(), testInventory.getSort().getOrder());
        assertEquals(testSortInventory.getNumItems(), 4);

        try {
            testSortInventory.sort(new Sort(SortType.Value, true));
        } catch (InvalidSortException e) {
            fail();
        }

        assertEquals(testSortInventory.getNumItems(), 4);

        assertEquals(testSortInventory.getItem(3), testSort4);
        assertEquals(testSortInventory.getItem(2), testSort3);
        assertEquals(testSortInventory.getItem(1), testSort2);
        assertEquals(testSortInventory.getItem(0), testSort1);

        assertEquals(testSortInventory.getSort().getSort(), SortType.Value);
        assertEquals(testSortInventory.getSort().getOrder(), true);
    }

    @Test
    void testSortReverseValue() {
        assertEquals(emptySort.getSort(), testInventory.getSort().getSort());
        assertEquals(emptySort.getOrder(), testInventory.getSort().getOrder());
        assertEquals(testSortInventory.getNumItems(), 4);

        try {
            testSortInventory.sort(new Sort(SortType.Value, false));
        } catch (InvalidSortException e) {
            fail();
        }

        assertEquals(testSortInventory.getNumItems(), 4);

        assertEquals(testSortInventory.getItem(3), testSort1);
        assertEquals(testSortInventory.getItem(2), testSort2);
        assertEquals(testSortInventory.getItem(1), testSort3);
        assertEquals(testSortInventory.getItem(0), testSort4);

        assertEquals(testSortInventory.getSort().getSort(), SortType.Value);
        assertEquals(testSortInventory.getSort().getOrder(), false);
    }

    @Test
    void testSortWeight() {
        assertEquals(emptySort.getSort(), testInventory.getSort().getSort());
        assertEquals(emptySort.getOrder(), testInventory.getSort().getOrder());
        assertEquals(testSortInventory.getNumItems(), 4);

        try {
            testSortInventory.sort(new Sort(SortType.Weight, true));
        } catch (InvalidSortException e) {
            fail();
        }

        assertEquals(testSortInventory.getNumItems(), 4);

        assertEquals(testSortInventory.getItem(0), testSort4);
        assertEquals(testSortInventory.getItem(1), testSort3);
        assertEquals(testSortInventory.getItem(2), testSort2);
        assertEquals(testSortInventory.getItem(3), testSort1);

        assertEquals(testSortInventory.getSort().getSort(), SortType.Weight);
        assertEquals(testSortInventory.getSort().getOrder(), true);
    }

    @Test
    void testSortReverseWeight() {
        assertEquals(emptySort.getSort(), testInventory.getSort().getSort());
        assertEquals(emptySort.getOrder(), testInventory.getSort().getOrder());
        assertEquals(testSortInventory.getNumItems(), 4);

        try {
            testSortInventory.sort(new Sort(SortType.Weight, false));
        } catch (InvalidSortException e) {
            fail();
        }

        assertEquals(testSortInventory.getNumItems(), 4);

        assertEquals(testSortInventory.getItem(0), testSort2);
        assertEquals(testSortInventory.getItem(1), testSort1);
        assertEquals(testSortInventory.getItem(2), testSort3);
        assertEquals(testSortInventory.getItem(3), testSort4);
        

        assertEquals(testSortInventory.getSort().getSort(), SortType.Weight);
        assertEquals(testSortInventory.getSort().getOrder(), false);
    }

    @Test
    void testSortTwice() {
        assertEquals(testSortInventory.getNumItems(), 4);

        try {
            testSortInventory.sort(new Sort(SortType.Weight, false));
            testSortInventory.sort(new Sort(SortType.Name, true));
        } catch (InvalidSortException e) {
            fail();
        }

        assertEquals(testSortInventory.getNumItems(), 4);

        assertEquals(testSortInventory.getItem(0), testSort1);
        assertEquals(testSortInventory.getItem(1), testSort2);
        assertEquals(testSortInventory.getItem(2), testSort3);
        assertEquals(testSortInventory.getItem(3), testSort4);

        assertEquals(testSortInventory.getSort().getSort(), SortType.Name);
        assertEquals(testSortInventory.getSort().getOrder(), true);
    }

    @Test
    void testAddItemSortedToUnsorted() {
        testInventory.addItemSorted(testItem);

        assertEquals(testInventory.getNumItems(), 1);
        assertEquals(testInventory.getItem(0), testItem);
    }

    @Test
    void testAddItemSorted() {
        Item testAddItem = new Consumable("Bc", 9, 10, "");
        try {
            testSortInventory.sort(new Sort(SortType.Name, true));
        } catch (InvalidSortException e) {
            fail();
        }
        testSortInventory.addItemSorted(testAddItem);

        assertEquals(testSortInventory.getNumItems(), 5);
        assertEquals(testSortInventory.getItem(2), testAddItem);
    }

    @Test
    void testAdd2ItemSorted() {
        Item testAddItem = new Weapon("Bc", 9, 10, "");
        Item testAddItem2 = new Currency("DC", 9, 10, "");
        try {
            testSortInventory.sort(new Sort(SortType.Type, true));
        } catch (InvalidSortException e) {
            fail();
        }
        testSortInventory.addItemSorted(testAddItem);
        testSortInventory.addItemSorted(testAddItem2);

        assertEquals(testSortInventory.getNumItems(), 6);
        assertEquals(testSortInventory.getItem(1), testAddItem);
        assertEquals(testSortInventory.getItem(5), testAddItem2);
    }

    @Test
    void testAddItemReverseSorted() {
        Item testAddItem = new Consumable("Bc", 15, 10, "");
        try {
            testSortInventory.sort(new Sort(SortType.Value, false));
        } catch (InvalidSortException e) {
            fail();
        }
        testSortInventory.addItemSorted(testAddItem);

        assertEquals(testSortInventory.getNumItems(), 5);
        assertEquals(testSortInventory.getItem(1), testAddItem);
    }

    @Test
    void testAdd2ItemReverseSorted() {
        Item testAddItem = new Weapon("Bc", 9, 100, "");
        Item testAddItem2 = new Currency("DC", 9, 0, "");
        try {
            testSortInventory.sort(new Sort(SortType.Weight, false));
        } catch (InvalidSortException e) {
            fail();
        }
        testSortInventory.addItemSorted(testAddItem);
        testSortInventory.addItemSorted(testAddItem2);

        assertEquals(testSortInventory.getNumItems(), 6);
        assertEquals(testSortInventory.getItem(5), testAddItem);
        assertEquals(testSortInventory.getItem(0), testAddItem2);
    }

    @Test
    void testGetProcessedInventory() {
        testSortInventory.addItem(testSort3);
        testSortInventory.addItem(testSort2);
        testSort2.setFavourite();
        testSort4.setFavourite();

        try {
            testSortInventory.sort(new Sort(SortType.Name, true));
        } catch (InvalidSortException e) {
            fail();
        }
        ArrayList<Item> processedInventory = testSortInventory.getProcessedInventory();

        assertEquals(4, processedInventory.size());
        assertEquals(testSort2, processedInventory.get(0));
        assertEquals(testSort4, processedInventory.get(1));
        assertEquals(testSort1, processedInventory.get(2));
        assertEquals(testSort3, processedInventory.get(3));

        testSortInventory.setAllFavourite();

        processedInventory = testSortInventory.getProcessedInventory();
        assertEquals(4, processedInventory.size());
        assertEquals(testSort1, processedInventory.get(0));
        assertEquals(testSort2, processedInventory.get(1));
        assertEquals(testSort3, processedInventory.get(2));
        assertEquals(testSort4, processedInventory.get(3));
    }

    @Test
    void testSetInventory() {
        testInventory.setInventory(testSortInventory);
        assertEquals(testInventory, testSortInventory);
    }

    @Test
    void testSetInventory2() {
        testSortInventory.setInventory(testInventory);
        assertEquals(testInventory, testSortInventory);
    }
}
