package model;

// Represents the inventory of a character, consisting of an
// arbitrary amount of items as well as the count of each identical element
public class Inventory {

    // EFFECT: creates an Inventory with no items inside of it
    public Inventory() {
        
    }

    // MODIFIES: this
    // EFFECT: adds given item to this inventory, if already inside increases item quantity
    public void addItem(){

    }

    // REQUIRES: given item is in inventory
    // MODIFIES: this
    // EFFECT: removes first instance of given item from the inventory
    public void removeItem(Item item){

    }

    // REQUIRES: given item is in inventory
    // MODIFIES: this
    // EFFECT: removes ALL instances of given item from the inventory
    public void removeAllItem(Item item){

    }

    // REQUIRES: given item is in inventory
    // EFFECT: returns number of times given item appears in Inventory
    public int getItemCount(Item item){
        return 0;
    }

    public int getNumItems(){
        return 0;
    }
}
