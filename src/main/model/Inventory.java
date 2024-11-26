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

    // EFFECT: creates an unsorted Inventory with no items inside of it, given a
    // character's name
    public Inventory(String character) {
        this.character = character;
        this.inventory = new ArrayList<>();
        this.sort = new Sort();
    }

    // EFFECT: creates an unsorted Inventory with no items inside of it, with a
    // default name as "User"
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
        EventLog.getInstance().logEvent(new Event(item.getName() + " added unsorted to inventory."));
    }

    // MODIFIES: this
    // EFFECT: removes first instance of given item from the inventory
    public void removeItem(Item item) {
        if (item == null) {
            return;
        }
        this.inventory.remove(item);
        EventLog.getInstance().logEvent(new Event(item.getName() + " removed from inventory."));
    }

    // MODIFIES: this
    // EFFECT: removes ALL instances of given item from the inventory
    public void removeAllItem(Item item) {
        while (inventory.contains(item)) {
            inventory.remove(item);
        }
        EventLog.getInstance()
                .logEvent(new Event("All " + item.getName() + " removed from " + character + "'s inventory."));
    }

    // MODIFIES: this
    // EFFECT: removes all items from inventory
    public void clearInventory() {
        inventory.clear();
        EventLog.getInstance().logEvent(new Event(character + "'s inventory has been cleared"));
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
        EventLog.getInstance().logEvent(
                new Event("Inventory sorted by " + sort.getSort() + ", " + (sort.getOrder() ? "dsc" : "asc")));
    }

    // MODIFIES: this
    // EFFECT: adds given item to this inventory in its correct position
    // according to sort. If two are identical, inserts at the next point
    // where they aren't. If list is unsorted, simply adds to end
    public void addItemSorted(Item item) {
        if (this.sort.isUnsorted() || inventory.size() == 0) {
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

        EventLog.getInstance()
                .logEvent(new Event(item.getName() + " added sorted by " + sort.getSort() + " to inventory."));
    }

    // MODIFIES: this
    // EFFECTS: removes all of given item, then adds in a number of new items equal
    // to quantity
    public void replaceAllWith(Item item, Item replacement, int quantity) {
        removeAllItem(item);
        for (int i = 1; i <= quantity; i++) {
            addItemSorted(replacement);
        }
        EventLog.getInstance()
                    .logEvent(new Event("All " + item.getName() + " has been changed to " + replacement.getName() + "."));
    }

    // MODIFIES: this
    // EFFECTS: given a new inventory, modifies current inventory until it matches
    // given one
    public void setInventory(Inventory newInventory) {
        String initialName = character;
        inventory.clear();
        setCharacter(newInventory.getCharacter());
        for (Item item : newInventory.getInventory()) {
            inventory.add(item);
        }
        setSort(newInventory.getSort());

        EventLog.getInstance()
                .logEvent(new Event(initialName + "'s inventory has been set to " + character + "'s inventory"));
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
        for (Item item : inventory) {
            item.setFavourite();
        }
        EventLog.getInstance().logEvent(new Event("All items have been set to favourite"));
    }

    // MODIFIES: this, Item
    // EFFECT: sets all items in inventory to unfavourite
    public void setAllUnfavourite() {
        for (Item item : inventory) {
            item.setUnfavourite();
        }
        EventLog.getInstance().logEvent(new Event("All items have been set to favourite"));
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
        return inventory.size();
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
}
