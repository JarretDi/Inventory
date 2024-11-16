package model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import model.exceptions.InvalidSortException;
import model.items.Item;
import persistence.Writable;

// Represents the inventory of a character, consisting of an
// arbitrary amount of items as well as the count of each identical element
public class Inventory implements Writable {
    private String character;
    private ArrayList<Item> inventory;
    private Sort sort;

    // EFFECT: creates an unsorted Inventory with no items inside of it, given a character's name
    public Inventory(String character) {
        this.character = character;
        this.inventory = new ArrayList<>();
        this.sort = new Sort();
    }

    // EFFECT: creates an unsorted Inventory with no items inside of it, with a default name as "User"
    public Inventory() {
        this.character = "User";
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

    // MODIFIES: this, Sort
    // EFFECT: if given sort is unsort, throws new InvalidSortException
    // else sorts an inventory according to sort, and order is whether
    // descending(true) or ascending(false)
    // additionally changes the sort type to given sort
    public void sort(Sort sort) throws InvalidSortException {
        if (sort.isUnsorted()) {
            throw new InvalidSortException();
        }
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

    // MODIFIES: this
    // EFFECTS: given a new inventory, modifies current inventory until it matches given one
    public void setInventory(Inventory newInventory) {
        inventory.clear();
        setCharacter(newInventory.getCharacter());
        for (Item item:newInventory.getInventory()) {
            inventory.add(item);
        }
        setSort(newInventory.getSort());
    }

    public void setCharacter(String character) {
        this.character = character;
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

    public String getCharacter() {
        return this.character;
    }

    public Sort getSort() {
        return this.sort;
    }

    public ArrayList<Item> getInventory() {
        return this.inventory;
    }

    // EFFECTS: returns a processed inventory in which:
    // favourited items are put first
    // order within the two divisions is by current sort
    // duplicate items are removed
    public ArrayList<Item> getProcessedInventory() {
        ArrayList<Item> favouriteItems = new ArrayList<>();
        ArrayList<Item> nonFavouriteItems = new ArrayList<>();

        for (Item item : inventory) {
            if (item.isFavourite()) {
                if (!favouriteItems.contains(item)) {
                    favouriteItems.add(item);
                } else {
                    continue;
                }
            } else {
                if (!nonFavouriteItems.contains(item)) {
                    nonFavouriteItems.add(item);
                } else {
                    continue;
                }
            }
        }

        favouriteItems.addAll(nonFavouriteItems);
        return favouriteItems;
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
        JSONObject json = new JSONObject();
        json.put("character", character);
        json.put("items", itemsToJson());

        String sortString;
        if (sort.isUnsorted()) {
            sortString = "Unsorted";
        } else if (sort.getOrder()) {
            sortString = sort.getSort() + " dsc";
        } else {
            sortString = sort.getSort() + " asc";
        }

        json.put("sort", sortString);

        return json;
    }

    // EFFECTS: returns items in this workroom as a JSON array
    private JSONArray itemsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Item item : inventory) {
            jsonArray.put(item.toJson());
        }

        return jsonArray;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((character == null) ? 0 : character.hashCode());
        result = prime * result + ((inventory == null) ? 0 : inventory.hashCode());
        result = prime * result + ((sort == null) ? 0 : sort.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Inventory other = (Inventory) obj;
        if (character == null) {
            if (other.character != null)
                return false;
        } else if (!character.equals(other.character))
            return false;
        if (inventory == null) {
            if (other.inventory != null)
                return false;
        } else if (!inventory.equals(other.inventory))
            return false;
        if (sort == null) {
            if (other.sort != null)
                return false;
        } else if (!sort.equals(other.sort))
            return false;
        return true;
    }
}
