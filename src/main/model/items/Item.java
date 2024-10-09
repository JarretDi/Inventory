package model.items;

// Represents an item having a:
// Name, type, value, weight, description
public abstract class Item {
    protected String name;
    protected int value;
    protected int weight;
    protected String desc;
    protected Boolean favourite;

    // EFFECT: creates an unfavourited item with given name, value and description
    public Item(String name, int value, int weight, String desc) {
        this.name = name;
        this.value = value;
        this.weight = weight;
        this.desc = desc;
        this.favourite = false;
    }

    public abstract String getType();

    // EFFECT: returns an items priority in terms of types,
    //         Weapon, Armour, Consumable, Misc, Currency
    //         1       2       3           4     5
    public abstract int getTypePriority();

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

    public void setFavourite() {
        this.favourite = true;
    }

    public void setUnfavourite() {
        this.favourite = false;
    }
}
