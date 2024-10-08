package model;

// Represents an item with a specific type, having a:
// Name, type, value, weight, description
// Type can be a currency, weapon, armour, consumable or misc
public class Item {
    private String name;
    private String type;
    private int value;
    private int weight;
    private String desc;

    // EFFECT: creates an item with given name, type, value and description
    public Item(String name, String type, int value, int weight, String desc) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.weight = weight;
        this.desc = desc;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public int getValue() {
        return this.value;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public String getDescription() {
        return this.desc;
    }
}
