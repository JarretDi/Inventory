package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;

public class SortTest {
    Sort testSort;
    Sort testUnsort;

    @BeforeEach
    void setup() {
        testSort = new Sort("Name", true);
        testUnsort = new Sort();
    }

    void testConstructor(){
        assertEquals(testSort.getSort(), "Name");
        assertEquals(testSort.getOrder(), true);
    }

    void testEmptyConstructor(){
        assertEquals(testUnsort.getSort(), null);
        assertEquals(testUnsort.getOrder(), null);
    }

}
