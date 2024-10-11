package ui;

import java.util.ArrayList;
import java.util.Scanner;

import model.Inventory;
import model.Sort;
import model.items.Armour;
import model.items.Consumable;
import model.items.Currency;
import model.items.Item;
import model.items.Misc;
import model.items.Weapon;

// The UI for all of inventory and items
// Can: view added items, add items, sort items, quit
// When viewing items, can 'select' items to display all of its information,
// and can remove and set favourites from there
// Lots of the code are taken from the Flashcard Reviewer lab, will note it down

public class InventoryHandler {
    Inventory inventory;
    Scanner scanner;

    Boolean isProgramRunning;

    // FROM: flashcard lab
    // EFFECTS: creates inventory object and initializes main loop
    public InventoryHandler() {
        init();

        printDivider();
        System.out.println("Welcome to the Inventory Manager app!");
        printDivider();

        while (this.isProgramRunning) {
            handleMenu();
        }
    }

    // FROM: flashcard lab
    // MODIFIES: this
    // EFFECTS: initializes the application with the starting values
    public void init() {
        this.inventory = new Inventory();
        this.scanner = new Scanner(System.in);
        this.isProgramRunning = true;
    }

    // FROM: flashcard lab
    // EFFECTS: displays and processes inputs for the main menu
    public void handleMenu() {
        displayMenu();
        String input = this.scanner.nextLine();
        processMenuCommands(input);
    }

    // FROM: flashcard lab
    // EFFECTS: displays a list of commands that can be used in the main menu
    public void displayMenu() {
        System.out.println("Please select an option:\n");
        System.out.println("v: View all items");
        System.out.println("a: Add a new item");
        System.out.println("q: Exit the application");
        printDivider();
    }

    // FROM: flashcard lab
    // EFFECTS: processes the user's input in the main menu
    public void processMenuCommands(String input) {
        printDivider();
        switch (input) {
            case "v":
                viewInventory();
                break;
            case "a":
                addNewItem();
                viewInventory();
                break;
            case "q":
                quitApplication();
                break;
            default:
                System.out.println("Invalid option inputted. Please try again.");
        }
        printDivider();
    }

    // MODIFIES: Inventory
    // EFFECTS: handles input when viewing inventory
    private void viewInventory() {
        ArrayList<Item> items = processItems();
        printInventory(items);

        displayInventoryCommands();

        String input = this.scanner.nextLine();
        processInventoryCommands(input, items);
    }

    // EFFECTS: processes an inventory into a more printable form
    private ArrayList<Item> processItems() {
        ArrayList<Item> favouriteItems = new ArrayList<>();
        ArrayList<Item> nonFavouriteItems = new ArrayList<>();

        for (Item item : inventory.getInventory()) {
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

    // EFFECTS: prints out all items in inventory, with quantity if duplicates
    // favorited items get printed first
    private void printInventory(ArrayList<Item> items) {
        int i = 1;
        if (inventory.getSort().isUnsorted()) {
            System.out.println("Inventory:");
        } else {
            System.out.println("Inventory (sorted by " + inventory.getSort().getSort() + "):");
        }
        printDivider();

        for (Item item : items) {
            System.out
                    .println(i + ". " + item.getName() + " x " + inventory.getItemCount(item) + ": " + item.getType());
            if (item.isFavourite()) {
                System.out.println("Value: " + item.getValue() + " | Weight: " + item.getWeight());
                System.out.println('"' + item.getDescription() + '"');
            }
            printDivider();
            i++;
        }
    }

    // EFFECT: displays available inventory commands
    private void displayInventoryCommands() {
        System.out.println("Please select an item number or a command below:\n");
        System.out.println("v: Expand all items");
        System.out.println("c: Shrink all items");
        System.out.println("s: Sort your items");
        System.out.println("q: Exit this menu");
        printDivider();
    }

    // MODIFIES: Inventory
    // EFFECT: processes commands related to managing inventory including:
    //         favourite(expand) all items and its opposite
    //         sort items in an inventory by a category
    //         view an item by typing in its index
    private void processInventoryCommands(String input, ArrayList<Item> items) {
        switch (input) {
            case "v":
                for (Item item : items) {
                    item.setFavourite();
                }
                break;
            case "c":
                for (Item item : items) {
                    item.setUnfavourite();
                }
                break;
            case "s":
                sortInventory();
                break;
            case "q":
                return;
            default:
                int index = Integer.parseInt(input);
                processItem(items.get(index - 1));
        }
        printDivider();
        viewInventory();
    }

    // MODIFIES: inventory
    // EFFECT: Identifies the sort the user inputs and then sorts the inventory according to that
    private void sortInventory() {
        Sort sort;
        Boolean sortOrder;

        System.out.println("Please enter the type of sort:");
        System.out.println("Sort can be Name, Type, Value or Weight");
        printDivider();

        String sortType = this.scanner.nextLine();
        // check if valid

        System.out.println("Please enter order, asc or dec, default asc");

        String order = this.scanner.nextLine();

        if ((order.equals("asc")) || (order.equals(""))) {
            sortOrder = true;
        } else if (order.equals("des")) {
            sortOrder = false;
        } else {
            // you shouldn't be here
            sortOrder = null;
        }

        sort = new Sort(sortType, sortOrder);

        inventory.sort(sort);

        System.out.println("Inventory has been successfully sorted by " + sortType);
        printDivider();
    }

    // MODIFIES: Inventory, Item
    // EFFECT: given an individual item, displays possible user interactions such as:
    //         expand/shrink this item, increase/decrease the quantity of the item
    private void processItem(Item item) {
        printItem(item);
        displayItemCommands();

        String input = this.scanner.nextLine();
        processItemCommands(input, item);
    }

    // EFFECT: prints all the  info of the expanded form of the item, even if it is not favourited
    private void printItem(Item item) {
        System.out
                .println(item.getName() + " x " + inventory.getItemCount(item) + ": " + item.getType());
        System.out.println("Value: " + item.getValue() + " | Weight: " + item.getWeight());
        System.out.println('"' + item.getDescription() + '"');

        printDivider();
    }

    // EFFECT: prints out all available commands for this item
    private void displayItemCommands() {
        System.out.println("Please select an item number or a command below:\n");
        System.out.println("e: Favourite this item");
        System.out.println("s: Unfavourite this item");
        System.out.println("a: Increase the quantity of this item");
        System.out.println("r: Decrease the quantity of this item");
        System.out.println("q: Exit this menu");
    }

    // MODIFIES: Inventory, Item
    // EFFECT: Processes user input for the items 
    //         can set/unset favourite and increase/decrease quantity of item
    private void processItemCommands(String input, Item item) {
        switch (input) {
            case "e":
                item.setFavourite();
                break;
            case "s":
                item.setUnfavourite();
                break;
            case "a":
                increaseQuantity(item);
                break;
            case "r":
                decreaseQuantity(item);
                break;
            case "q":
                return;
        }
    }

    // MODIFIES: inventory
    // EFFECT: given a user input, will add that number more to inventory
    private void increaseQuantity(Item item) {

    }

    // MODIFIES: inventory
    // EFFECT: given a user input, will add that number more to inventory
    private void decreaseQuantity(Item item) {

    }

    // MODIFIES: Inventory
    // EFFECT: Creates and adds an item using user input
    // If the list is unsorted, adds item to end, otherwise puts in correct place
    @SuppressWarnings("methodlength")
    public void addNewItem() {
        Item item;

        System.out.println("Please enter the name of the item:");
        String name = this.scanner.nextLine();

        System.out.println("Please enter the type of the item:");
        System.out.println("Can be a Weapon, Armour, Consumable, Misc or Currency");
        String type = this.scanner.nextLine();
        if (!validtype(type)) {
            System.out.println("Not a type");
            return;
        }

        System.out.println("Please enter the value of the item, or default 0:");
        String value = this.scanner.nextLine();
        int itemValue;
        if (value.equals("")) {
            itemValue = 0;
        } else {
            itemValue = Integer.parseInt(value);
        }

        System.out.println("Please enter the value of the item, or default 0:");
        String weight = this.scanner.nextLine();
        int itemWeight;
        if (weight.equals("")) {
            itemWeight = 0;
        } else {
            itemWeight = Integer.parseInt(value);
        }

        System.out.println("Please enter the description of the item:");
        String desc = this.scanner.nextLine();

        item = createItemFromInput(name, type, itemValue, itemWeight, desc);

        inventory.addItemSorted(item);

        System.out.println(name + " has been successfully created!");
        printDivider();
    }

    private Item createItemFromInput(String name, String type, int itemValue, int itemWeight, String desc) {
        switch (type) {
            case "Weapon":
            case "w":
                return new Weapon(name, itemValue, itemWeight, desc);
            case "Armour":
            case "a":
                return new Armour(name, itemValue, itemWeight, desc);
            case "Consumable":
                return new Consumable(name, itemValue, itemWeight, desc);
            case "Currency":
                return new Currency(name, itemValue, itemWeight, desc);
            case "Misc":
            case "m":
            case "":
                return new Misc(name, itemValue, itemWeight, desc);
            default:
                System.out.println("You shouldn't get here");
                return new Misc("Error"); // just so the method returns an Item for compiler
        }
    }

    // EfFECT: Returns true if input is a valid type
    private boolean validtype(String type) {
        switch (type) {
            case "Weapon":
            case "w":
                return true;
            case "Armour":
            case "a":
                return true;
            case "Consumable":
                return true;
            case "Misc":
            case "m":
            case "":
                return true;
            case "Currency":
                return true;
        }
        return false;
    }

    // FROM: flashcard lab
    // MODIFIES: this
    // EFFECTS: prints a closing message and marks the program as not running
    public void quitApplication() {
        System.out.println("Thanks for using the Inventory Manager app!");
        System.out.println("Have a good day!");
        this.isProgramRunning = false;
    }

    private void printDivider() {
        System.out.println("_____________________");
    }
}
