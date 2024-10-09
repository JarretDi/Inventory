package model.items;

// Represents an item having a:
// Name, type, value, weight, description
public abstract class Item {
    protected String name;
    protected int value;
    protected int weight;
    protected String desc;
    protected Boolean favourite;

    // EFFECT: creates an item with given name, type, value and description
    public Item(String name, int value, int weight, String desc) {
        this.name = name;
        this.value = value;
        this.weight = weight;
        this.desc = desc;
        this.favourite = false;
    }

    public abstract String getType();

    public abstract int getPriority();

    public String getName() {
        return this.name;
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

    public Boolean getFavourite() {
        return this.favourite;
    }
}
