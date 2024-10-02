package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ItemTest {
    Item testItem;

    @BeforeEach
    void setup(){
        testItem = new Item("Test Item", "Misc", 1, "A test item.");
    }

    @Test
    void testConstructor(){
        assertEquals("Test Item", testItem.getName());
        assertEquals("Misc", testItem.getType());
        assertEquals(1, testItem.getValue());
        assertEquals("A test item.", testItem.getDescription());
    }
}
