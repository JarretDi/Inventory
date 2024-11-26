package persistence;

import org.json.JSONObject;

import model.Event;
import model.EventLog;
import model.Inventory;

import java.io.*;

// Represents a writer that writes JSON representation of inventory to file
// All method specifications are from workroom demo
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs a writer to save an inventory under given character
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file
    // cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of workroom to file
    public void write(Inventory inventory) {
        EventLog.getInstance().logEvent(new Event(inventory.getCharacter() + "'s inventory is being saved..."));
        JSONObject json = inventory.toJson();
        saveToFile(json.toString(TAB));
        EventLog.getInstance().logEvent(new Event(inventory.getCharacter() + " has been successfully saved!"));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
