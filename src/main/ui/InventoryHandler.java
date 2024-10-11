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

    // MODIFIES: Inventory
    // EFFECT: Creates and adds an item using user input
    // If the list is unsorted, adds item to end, otherwise puts in correct place
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

        switch (type) {
            case "Weapon":
                item = new Weapon(name, itemValue, itemWeight, desc);
                break;
            case "w":
                item = new Weapon(name, itemValue, itemWeight, desc);
                break;
            case "Armour":
                item = new Armour(name, itemValue, itemWeight, desc);
                break;
            case "a":
                item = new Armour(name, itemValue, itemWeight, desc);
                break;
            case "Consumable":
                item = new Consumable(name, itemValue, itemWeight, desc);
                break;
            case "Misc":
                item = new Misc(name, itemValue, itemWeight, desc);
                break;
            case "m":
                item = new Misc(name, itemValue, itemWeight, desc);
                break;
            case "":
                item = new Misc(name, itemValue, itemWeight, desc);
                break;
            case "Currency":
                item = new Currency(name, itemValue, itemWeight, desc);
                break;
            default:
                System.out.println("You shouldn't get here");
                item = new Weapon(name);
        }

        inventory.addItemSorted(item);

        System.out.println(name + " has been successfully created!");
        printDivider();
    }

    private boolean validtype(String type) {
        switch (type) {
            case "Weapon":
                return true;
            case "w":
                return true;
            case "Armour":
                return true;
            case "a":
                return true;
            case "Consumable":
                return true;
            case "Misc":
                return true;
            case "m":
                return true;
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
