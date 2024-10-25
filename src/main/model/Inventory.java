package model;

import java.util.ArrayList;

import org.json.JSONObject;

import model.items.Item;
import persistence.Writable;

// Represents the inventory of a character, consisting of an
// arbitrary amount of items as well as the count of each identical element
public class Inventory implements Writable {
    private String name;
    private ArrayList<Item> inventory;
    private Sort sort;

    // EFFECT: creates an unsorted Inventory with no items inside of it, given a character's name
    public Inventory(String name) {
        this.name = name;
        this.inventory = new ArrayList<>();
        this.sort = new Sort();
    }

    // EFFECT: creates an unsorted Inventory with no items inside of it, with a default name as "User"
    public Inventory() {
        this.name = "User";
        this.inventory = new ArrayList<>();
        this.sort = new Sort();
    }

    // MODIFIES: this, Sort
    // EFFECT: adds given item to this inventory
    // additionally, changes the inventory's type to unsorted
    public void addItem(Item item) {
        this.inventory.add(item);
        this.sort.setUnsorted();
    }

    // MODIFIES: this
    // EFFECT: removes first instance of given item from the inventory
    public void removeItem(Item item) {
        this.inventory.remove(item);
    }

    // MODIFIES: this
    // EFFECT: removes ALL instances of given item from the inventory
    public void removeAllItem(Item item) {
        while (inventory.contains(item)) {
            inventory.remove(item);
        }
    }

    // MODIFIES: this
    // EFFECT: removes all items from inventory
    public void clearInventory() {
        inventory.clear();
    }

    // REQUIRES: sort is not the Unsort (empty sort)
    // MODIFIES: this, Sort
    // EFFECT: sorts an inventory according to sort, and order is whether
    // ascending(true) or descending(false)
    // additionally changes the sort type to given sort
    public void sort(Sort sort) {
        inventory.sort((item1, item2) -> {
            return item1.getPriority(sort) - item2.getPriority(sort);
        });
        setSort(sort);
    }

    // MODIFIES: this
    // EFFECT: adds given item to this inventory in its correct position
    // according to sort. If two are identical, inserts at the next point
    // where they aren't. If list is unsorted, simply adds to end
    public void addItemSorted(Item item) {
        if (this.sort.isUnsorted()) {
            addItem(item);
            return;
        }
        int len = inventory.size();
        int itemPriority = item.getPriority(sort);

        for (int i = 0; i < len; i++) {
            int currentItemPriority = inventory.get(i).getPriority(sort);

            if (itemPriority < currentItemPriority) {
                inventory.add(i, item);
                break;
            } else if (i + 1 == len) {
                inventory.add(item);
            } else if (itemPriority - currentItemPriority == 0) {
                continue;
            }
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    // MODIFIES: this, Item
    // EFFECT: sets all items in inventory to favourite
    public void setAllFavourite() {
        for (Item item:inventory) {
            item.setFavourite();
        }
    }

    // MODIFIES: this, Item
    // EFFECT: sets all items in inventory to unfavourite
    public void setAllUnfavourite() {
        for (Item item:inventory) {
            item.setUnfavourite();
        }
    }

    public String getName() {
        return this.name;
    }

    public Sort getSort() {
        return this.sort;
    }

    public ArrayList<Item> getInventory() {
        return this.inventory;
    }

    public Item getItem(int index) {
        return inventory.get(index);
    }

    // EFFECT: returns number of times given item appears in Inventory
    public int getCount(Item item) {
        int count = 0;
        for (Item i : inventory) {
            if (i.equals(item)) {
                count += 1;
            }
        }
        return count;
    }

    public int getNumItems() {
        int count = 0;
        for (Item i : inventory) {
            count += 1;
        }
        return count;
    }

    // EFFECTS: returns the current inventory converted to a Json object
    @Override
    public JSONObject toJson() {
        return null;
    }
}
