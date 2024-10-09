package model;

import java.util.ArrayList;

import model.items.Item;

// Represents the inventory of a character, consisting of an
// arbitrary amount of items as well as the count of each identical element
public class Inventory {
    private ArrayList<Item> inventory;
    private Sort sort;

    // EFFECT: creates an unsorted Inventory with no items inside of it
    public Inventory() {
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
        this.sort = sort;
    }

    // REQUIRES: Inventory is already sorted
    // MODIFIES: this
    // EFFECT: adds given item to this inventory in its correct position
    // according to sort. If two are identical, inserts at the next point
    // where they aren't
    public void addItemSorted(Item item) {
        int len = inventory.size();

        int itemPriority = item.getPriority(sort);

        for (int i = 0; i < len; i++) {
            int currentItemPriority = inventory.get(i).getPriority(sort);

            if (itemPriority < currentItemPriority) {
                inventory.add(i, item);
                break;
            } else if (i+1 == len) {
                inventory.add(item);
            } else if (itemPriority - currentItemPriority == 0) {
                continue;
            }
        }
    }

    public Sort getSort() {
        return this.sort;
    }

    public Item getItem(int index) {
        return inventory.get(index);
    }

    // EFFECT: returns number of times given item appears in Inventory
    public int getItemCount(Item item) {
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
}
