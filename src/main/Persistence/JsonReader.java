package persistence;

import model.Event;
import model.EventLog;
import model.Inventory;
import model.Sort;
import model.Sort.SortType;
import model.exceptions.ItemCreationException;
import model.items.Item;
import model.items.ItemCreator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads inventory from JSON data stored in file
// Method implementations mostly taken from workroom demo
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads inventory from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Inventory read() throws IOException {
        EventLog.getInstance().logEvent(new Event("Inventory is being loaded..."));
        
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseInventory(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses inventory from JSON object and returns it
    private Inventory parseInventory(JSONObject jsonObject) {
        String name = jsonObject.getString("character");
        Sort sort = processSort(jsonObject.getString("sort"));

        Inventory inventory = new Inventory(name);
        addItems(inventory, jsonObject);
        inventory.setSort(sort);

        EventLog.getInstance().logEvent(new Event(inventory.getCharacter() + "'s inventory has loaded!"));

        return inventory;
    }

    private Sort processSort(String string) {
        if (string.equals("Unsorted")) {
            return new Sort();
        } else {
            String[] splitString = string.split(" ");
            Boolean order;
            if (splitString[1].equals("dsc")) {
                order = true;
            }  else {
                order = false;
            }
            return new Sort(SortType.valueOf(splitString[0]), order);
        }
    }

    // MODIFIES: inventory
    // EFFECTS: parses items from JSON object and adds them to inventory
    private void addItems(Inventory inventory, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("items");
        for (Object json : jsonArray) {
            JSONObject nextItem = (JSONObject) json;
            try {
                addItem(inventory, nextItem);
            } catch (ItemCreationException e) {
                // Invalid item won't get recorded
            }
        }
    }

    // MODIFIES: inventory
    // EFFECTS: parses item from JSON object and adds it to inventory
    private void addItem(Inventory inventory, JSONObject jsonObject) throws ItemCreationException {
        String name = jsonObject.getString("name");
        String type = jsonObject.getString("type");
        int value = jsonObject.getInt("value");
        int weight = jsonObject.getInt("weight");
        String desc = jsonObject.getString("desc");
        Boolean favourite = jsonObject.getBoolean("favourite");

        Item item = ItemCreator.createItemFromInput(name, type, value, weight, desc, favourite);
        inventory.addItem(item);
    }
}
