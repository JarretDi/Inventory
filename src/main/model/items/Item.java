package model.items;

import org.json.JSONObject;

import model.Sort;
import persistence.Writable;

// Represents an item having a:
// Name, type, value, weight, description
public abstract class Item implements Writable {
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
    // Weapon, Armour, Consumable, Misc, Currency
    // 1 2 3 4 5
    public abstract int getTypePriority();

    // REQUIRES: given sort != unsort
    // EFFECT: returns a 'priority' integer of this item given a specific sort
    // priority affects the order of the sort
    public int getPriority(Sort currentSort) {
        int priority = 0;
        int mod;

        if (currentSort.getOrder()) {
            mod = 1;
        } else {
            mod = -1;
        }

        switch (currentSort.getSort()) {
            case Name:
                priority = this.name.compareTo(" ") * mod;
                break;
            case Type:
                priority = getTypePriority() * mod;
                break;
            case Value:
                priority = this.value * mod * -1;
                break;
            default:
                priority = this.weight * mod * -1;
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

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("type", getType());
        json.put("value", value);
        json.put("weight", weight);
        json.put("desc", desc);
        json.put("favourite", favourite);

        return json;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + value;
        result = prime * result + weight;
        result = prime * result + ((desc == null) ? 0 : desc.hashCode());
        result = prime * result + ((favourite == null) ? 0 : favourite.hashCode());
        return result;
    }

    @Override
    @SuppressWarnings("methodlength")
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Item other = (Item) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (value != other.value) {
            return false;
        }
        if (weight != other.weight) {
            return false;
        }
        if (desc == null) {
            if (other.desc != null) {
                return false;
            }
        } else if (!desc.equals(other.desc)) {
            return false;
        }
        if (favourite == null) {
            if (other.favourite != null) {
                return false;
            }
        } else if (!favourite.equals(other.favourite)) {
            return false;
        }
        return true;
    }
}
