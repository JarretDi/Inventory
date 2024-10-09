package model.items;

public class Consumable extends Item {
    private String type;

    // Constructs a weapon with given name, type, value, weight, desc
    public Consumable(String name, int value, int weight, String desc) {
        super(name, value, weight, desc);
        this.type = "Consumable";
    }

    // Constructs a weapon with given name and default (0/empty) parameters
    public Consumable(String name) {
        super(name, 0, 0, "");
        this.type = "Consumable";
    }
    
    @Override
    public String getType() {
        return this.type;
    }

    public int getTypePriority() {
        return 5;
    }
}

