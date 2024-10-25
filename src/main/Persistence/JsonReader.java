package persistence;

import java.io.IOException;

import model.Inventory;

// Represents a reader that reads inventory from JSON data stored in file
// Method names from workroom demo
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads inventory from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Inventory read() throws IOException {
        return null;
    }
}
