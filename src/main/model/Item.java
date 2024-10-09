package model;

// Represents an item with a specific type, having a:
// Name, type, value, weight, description
// Type can be a weapon, armour, consumable, misc or currency
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

    // EFFECT: used for sorting, returns intended priority for types, misc is default
    public int getTypePriority() {
        int priority = 4; // value for misc
        switch(this.type){
            case "Weapon":
                priority = 1;
                break;
            case "Armour":
                priority = 2;
                break;
            case "Consumable":
                priority = 3;
                break;
            case "Currency":
                priority = 5;
                break;
        }
        return priority;
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
