package model;

import java.util.ArrayList;

// Represents the inventory of a character, consisting of an
// arbitrary amount of items as well as the count of each identical element
public class Inventory {
    private ArrayList<Item> inventory;

    // EFFECT: creates an Inventory with no items inside of it
    public Inventory() {
        this.inventory = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECT: adds given item to this inventory, if already inside increases item quantity
    public void addItem(Item item){
        this.inventory.add(item);
    }

    // REQUIRES: given item is in inventory
    // MODIFIES: this
    // EFFECT: removes first instance of given item from the inventory
    public void removeItem(Item item){
        this.inventory.remove(item);
    }

    // REQUIRES: given item is in inventory
    // MODIFIES: this
    // EFFECT: removes ALL instances of given item from the inventory
    public void removeAllItem(Item item){
        while (inventory.contains(item)){
            inventory.remove(item);
        }
    }

    // MODIFIES: this
    // EFFECT: removes all items from inventory
    public void clearInventory(){
        inventory.clear();
    }

    // REQUIRES: given item is in inventory
    // EFFECT: returns number of times given item appears in Inventory
    public int getItemCount(Item item){
        int count = 0;
        for (Item i:inventory){
            if (i.equals(item)){
                count += 1;
            }
        }
        return count;
    }

    public int getNumItems(){
        int count = 0;
        for (Item i:inventory){
            count += 1;
        }
        return count;
    }
}
