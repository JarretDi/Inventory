package ui;

import model.Inventory;

public class Main {
    public static void main(String[] args) throws Exception {
        Inventory inventory = new Inventory();
        InventoryGUI inventoryGUI = new InventoryGUI(inventory);
        InventoryHandlerUI inventoryHandler = new InventoryHandlerUI(inventory);
    }
}
