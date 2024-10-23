package ui;

import java.util.ArrayList;
import java.util.Scanner;

import Exceptions.InvalidSortException;
import Exceptions.InvalidTypeException;
import Exceptions.ItemCreationException;
import Exceptions.NeedsPositiveNumberException;
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
    private void init() {
        this.inventory = new Inventory();
        this.scanner = new Scanner(System.in);
        this.isProgramRunning = true;
    }

    // FROM: flashcard lab
    // EFFECTS: displays and processes inputs for the main menu
    private void handleMenu() {
        displayMenu();
        String input = this.scanner.nextLine();
        processMenuCommands(input);
    }

    // FROM: flashcard lab
    // EFFECTS: displays a list of commands that can be used in the main menu
    private void displayMenu() {
        System.out.println("Please select an option:\n");
        System.out.println("v: View all items");
        System.out.println("a: Add a new item");
        System.out.println("q: Exit the application");
        printDivider();
    }

    // FROM: flashcard lab
    // EFFECTS: processes the user's input in the main menu
    private void processMenuCommands(String input) {
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

    // MODIFIES: this, Inventory
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
            if (item.isFavourite()) {
                System.out
                .println(i + ". <f> " + item.getName() + " x " + inventory.getCount(item) + ": " + item.getType());
                System.out.println("Value: " + item.getValue() + " | Weight: " + item.getWeight());
                System.out.println('"' + item.getDescription() + '"');
            } else {
                System.out
                .println(i + ". " + item.getName() + " x " + inventory.getCount(item) + ": " + item.getType());
            }
            printDivider();
            i++;
        }
    }

    // EFFECT: displays available inventory commands
    private void displayInventoryCommands() {
        System.out.println("Please select an item number or a command below:\n");
        System.out.println("z: Favourite all items");
        System.out.println("x: Unfavourite all items");
        System.out.println("s: Sort your items");
        System.out.println("q: Exit this menu");
        printDivider();
    }

    // MODIFIES: this, Inventory
    // EFFECT: processes commands related to managing inventory including:
    //         favourite(expand) all items and its opposite
    //         sort items in an inventory by a category
    //         view an item by typing in its index
    private void processInventoryCommands(String input, ArrayList<Item> items) {
        switch (input) {
            case "z":
                for (Item item : items) {
                    item.setFavourite();
                }
                break;
            case "x":
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
                try {
                    int index = Integer.parseInt(input);
                    processItem(items.get(index - 1));
                } catch (NumberFormatException ne) {
                    System.out.println("Invalid option inputted. Please try again.");
                } catch (IndexOutOfBoundsException oob) {
                    System.out.println("Invalid option inputted. Please enter a valid index.");
                }
        }
        printDivider();
        viewInventory();
    }

    // MODIFIES: this, inventory
    // EFFECT: Identifies the sort the user inputs and then sorts the inventory according to that
    private void sortInventory() {
        Sort sort;
        String sortType = null;
        Boolean sortOrder = null;

        System.out.println("Please enter the type of sort:");
        System.out.println("Sort can be Name, Type, Value or Weight");
        printDivider();
        while (sortType == null) {
            try {
                sortType = processSort(this.scanner.nextLine());
            } catch (InvalidSortException e) {
                System.out.println("Invalid option inputted. Please try again.");
            }
        }

        System.out.println("Please enter order, asc or dsc, default asc");

        while (sortOrder == null) {
            try {
               sortOrder = processSortOrder(this.scanner.nextLine());
            } catch (InvalidSortException e) {
                System.out.println("Invalid option inputted. Please try again.");
            }
        }

        sort = new Sort(sortType, sortOrder);

        inventory.sort(sort);

        System.out.println("Inventory has been successfully sorted by " + sortType);
        printDivider();
    }

    // MODIFIES: this, Inventory, Item
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
        String favouriteMark;
        if (item.isFavourite()) {
            favouriteMark = "<f> ";
        } else {
            favouriteMark = "";
        }
        System.out
                .println(favouriteMark + item.getName() + " x " + inventory.getCount(item) + ": " + item.getType());
        System.out.println("Value: " + item.getValue() + " | Weight: " + item.getWeight());
        System.out.println('"' + item.getDescription() + '"');

        printDivider();
    }

    // EFFECT: prints out all available commands for this item
    private void displayItemCommands() {
        System.out.println("Please select an item number or a command below:\n");
        System.out.println("z: Favourite this item");
        System.out.println("x: Unfavourite this item");
        System.out.println("a: Increase the quantity of this item");
        System.out.println("s: Decrease the quantity of this item");
        System.out.println("q: Exit this menu");
    }

    // MODIFIES: this, Inventory, Item
    // EFFECT: Processes user input for the items 
    //         can set/unset favourite and increase/decrease quantity of item
    private void processItemCommands(String input, Item item) {
        switch (input) {
            case "z":
                item.setFavourite();
                System.out.println(item + " has been set as favourite!");
                break;
            case "x":
                item.setUnfavourite();
                System.out.println(item + " has been set as unfavourite...");
                break;
            case "a":
                increaseQuantity(item);
                break;
            case "s":
                decreaseQuantity(item);
                break;
            case "q":
                return;
        }
    }

    // MODIFIES: this, inventory
    // EFFECT: given a user input, will add that number more to inventory
    private void increaseQuantity(Item item) {
        int intInput = -1;
        System.out.println("Please enter how much more you wish to add (default 1):");

        while (intInput < 0) {
            String input = scanner.nextLine();
            if (input.equals("")) {
                intInput = 1;
            } else {
                try {
                    intInput = Integer.parseInt(input);
                    if (intInput < 0) {
                        throw new NeedsPositiveNumberException();
                    }
                    for (int i = 0; i < intInput; i++) {
                        inventory.addItemSorted(item);
                    }
                } catch (NumberFormatException ne) {
                    System.out.println("Invalid option inputted. Please enter a number.");
                } catch (NeedsPositiveNumberException e) {
                    System.out.println("Invalid option inputted. Please enter a non-negative number.");
                }
            }
        }

        System.out.println(intInput + " items were succesfully added!");
    }

    // MODIFIES: this, inventory
    // EFFECT: given a user input, will add that number more to inventory
    private void decreaseQuantity(Item item) {
        int intInput = -1;
        System.out.println("Please enter how much more you wish to remove (default all):");

        while (intInput < 0) {
            String input = scanner.nextLine();
            if (input.equals("")) {
                inventory.removeAllItem(item);
                System.out.println("All items were succesfully removed!");
                return;
            } else {
                try {
                    intInput = Integer.parseInt(input);
                    if (intInput < 0) {
                        throw new NeedsPositiveNumberException();
                    }
                    for (int i = 0; i < intInput; i++) {
                        inventory.removeItem(item);
                    }
                } catch (NumberFormatException ne) {
                    System.out.println("Invalid option inputted. Please enter a number.");
                } catch (NeedsPositiveNumberException e) {
                    System.out.println("Invalid option inputted. Please enter a non-negative number.");
                }
            }
        }

        System.out.println(intInput + " items were succesfully removed!");
    }


    // MODIFIES: this, Inventory
    // EFFECT: Creates and adds an item using user input
    // If the list is unsorted, adds item to end, otherwise puts in correct place
    @SuppressWarnings("methodlength")
    private void addNewItem() {
        Item item;

        System.out.println("Please enter the name of the item:");
        String name = this.scanner.nextLine();

        System.out.println("Please enter the type of the item:");
        System.out.println("Can be a Weapon, Armour, Consumable, Misc or Currency");
        String type = null;
        while (type == null) {
            try {
                type = processType(this.scanner.nextLine());
            } catch (InvalidTypeException te) {
               System.out.println("Invalid option inputted. Please enter a type.");
            }
        }

        System.out.println("Please enter the value of the item, or default 0:");
        int itemValue = -1;
        while (itemValue < 0) {
            String value = this.scanner.nextLine();
            if (value.equals("") || (value == "0")) {
                itemValue = 0;
            } else {
                try {
                    itemValue = Integer.parseInt(value);
                    if (itemValue < 0) {
                        throw new NeedsPositiveNumberException();
                    }
                } catch (NumberFormatException ne) {
                    System.out.println("Invalid option inputted. Please enter a number.");
                } catch (NeedsPositiveNumberException e) {
                    System.out.println("Invalid option inputted. Please enter a non-negative value.");
                }
            }
        }

        System.out.println("Please enter the weight of the item, or default 0:");
        int itemWeight = -1;
        while (itemWeight < 0) {
            String weight = this.scanner.nextLine();
            if (weight.equals("") || (weight == "0")) {
                itemWeight = 0;
            } else {
                try {
                    itemWeight = Integer.parseInt(weight);
                    if (itemWeight < 0) {
                        throw new NeedsPositiveNumberException();
                    }
                } catch (NumberFormatException ne) {
                    System.out.println("Invalid option inputted. Please enter a number.");
                } catch (NeedsPositiveNumberException e) {
                    System.out.println("Invalid option inputted. Please enter a non-negative weight.");
                }
            }
        }

        System.out.println("Please enter the description of the item:");
        String desc = this.scanner.nextLine();

        item = null;
        try {
            item = createItemFromInput(name, type, itemValue, itemWeight, desc);
        } catch (ItemCreationException e) {
            System.out.println("Something went wrong. Please try creating the item again.");
            return;
        }

        inventory.addItemSorted(item);

        System.out.println(name + " has been successfully created!");
        printDivider();
    }

    // REQUIRES: all given inputs are valid parameters for an item
    // EFFECT: helper that creates and returns an item based on parameters
    private Item createItemFromInput(String name, String type, int itemValue, int itemWeight, String desc) throws ItemCreationException {
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
                throw new ItemCreationException();
        }
    }

    // EFFECT: Returns the sort back if valid
    // Throws an InvalidSortException if input is an invalid sort
    private String processSort(String sort) throws InvalidSortException {
        switch (sort) {
            case "Name":
            case "n":
                return "Name";
            case "Type":
            case "t":
                return "Type";
            case "Value":
            case "v":
                return "Value";
            case "Weight":
            case "w":
                return "Weight";
            default:
                throw new InvalidSortException();
        }
    }

    // EFFECT: Returns the sort order as a boolean if valid
    // Throws an InvalidSortException if input is an invalid sort order
    private Boolean processSortOrder(String order) throws InvalidSortException {
        switch (order) {
            case "asc":
            case "a":
                return true;
            case "des":
            case "dec":
            case "dsc":
            case "d":
                return false;
            default:
                throw new InvalidSortException();
        }
    }

    // EfFECT: Throws an InvalidTypeException if input is an invalid type
    private String processType(String type) throws InvalidTypeException {
        switch (type) {
            case "Weapon":
            case "w":
                return "Weapon";
            case "Armour":
            case "a":
                return "Armour";
            case "Consumable":
                return "Consumable";
            case "Misc":
            case "m":
            case "":
                return "Misc";
            case "Currency":
                return "Currency";
            default:
                throw new InvalidTypeException();
        }
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
