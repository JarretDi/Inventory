package persistence;

import org.json.JSONObject;

// From JSON demo
public interface Writable {
    // EFFECTS: return this as a JSON object
    JSONObject toJson();
}
