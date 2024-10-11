package model.items;

import model.Sort;

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

    // REQUIRES: given sort != unsort
    // EFFECT: returns a 'priority' integer of this item given a specific sort
    //         priority affects the order of the sort
    public int getPriority(Sort currentSort) {
        int priority = 0;

        String sort = currentSort.getSort();
        Boolean order = currentSort.getOrder();
        int mod;

        if (order) {
            mod = 1;
        } else {
            mod = -1;
        }

        switch(sort) {
            case "Name":
                priority = this.name.compareTo(" ") * mod;
                break;
            case "Type":
                priority = getTypePriority() * mod;
                break;
            case "Value":
                priority = this.value * mod;
                break;
            case "Weight":
                priority = this.weight * mod;
                break;
        }

        return priority;
    }

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

    public Boolean isFavourite() {
        return this.favourite;
    }

    public void setFavourite() {
        this.favourite = true;
    }

    public void setUnfavourite() {
        this.favourite = false;
    }
}
