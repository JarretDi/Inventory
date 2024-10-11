package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SortTest {
    Sort testSort;
    Sort testUnsort;

    @BeforeEach
    void setup() {
        testSort = new Sort("Name", true);
        testUnsort = new Sort();
    }

    @Test
    void testConstructor() {
        assertEquals(testSort.getSort(), "Name");
        assertEquals(testSort.getOrder(), true);
    }

    @Test
    void testEmptyConstructor() {
        assertEquals(testUnsort.getSort(), null);
        assertEquals(testUnsort.getOrder(), null);
    }

    @Test
    void testIsUnsorted() {
        assertEquals(testSort.isUnsorted(), false);
        assertEquals(testUnsort.isUnsorted(), true);
    }

}
