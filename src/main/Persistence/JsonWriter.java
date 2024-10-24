package Persistence;

import org.json.JSONObject;

import model.Inventory;

// Represents a writer that writes JSON representation of inventory to file
// All method specifications are from workroom demo
public class JsonWriter {

    // EFFECTS: constructs a writer to save an inventory under given character
    public JsonWriter(String character) {

    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() {

    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of workroom to file
    public void write(Inventory inventory) {
        
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        
    }
}
