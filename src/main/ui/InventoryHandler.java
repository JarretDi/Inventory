package ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import model.Inventory;
import model.Sort;
import model.Sort.SortType;
import model.exceptions.InvalidNumberException;
import model.exceptions.InvalidSortException;
import model.exceptions.InvalidTypeException;
import model.exceptions.ItemCreationException;
import model.items.Item;
import model.items.ItemCreator;
import persistence.JsonReader;
import persistence.JsonWriter;

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
    public InventoryHandler(Inventory inventory) {
        init(inventory);

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
    private void init(Inventory inventory) {
        this.inventory = inventory;
        this.scanner = new Scanner(System.in);
        this.isProgramRunning = true;
    }

    // FROM: flashcard lab
    // EFFECTS: displays and processes inputs for the main menu
    private void handleMenu() {
        displayMenu();
        while (true) {
            String input = this.scanner.nextLine();
            try {
                processMenuCommands(input);
                break;
            } catch (InvalidOptionException e) {
                System.out.println("Invalid option inputted. Please try again.");
            }
        }
    }

    // FROM: flashcard lab
    // EFFECTS: displays a list of commands that can be used in the main menu
    private void displayMenu() {
        printDivider();
        System.out.println("Please select an option:\n");
        System.out.println("v: View all items");
        System.out.println("a: Add a new item");
        System.out.println("s: Save your inventory");
        System.out.println("d: Load a character's inventory");
        System.out.println("q: Exit the application");
        printDivider();
    }

    // FROM: flashcard lab
    // EFFECTS: processes the user's input in the main menu
    private void processMenuCommands(String input) throws InvalidOptionException {
        switch (input) {
            case "v":
                viewInventory();
                break;
            case "a":
                addNewItem();
                viewInventory();
                break;
            case "s":
                saveInventory();
                break;
            case "d":
                if (confirm()) {
                    loadInventory();
                }
                break;
            case "q":
                quitApplication();
                break;
            default:
                throw new InvalidOptionException();
        }
        printDivider();
    }

    // MODIFIES: this, Inventory
    // EFFECTS: handles input when viewing inventory
    private void viewInventory() {
        ArrayList<Item> items = inventory.getProcessedInventory();
        printInventory(items);

        displayInventoryCommands();

        String input = this.scanner.nextLine();
        processInventoryCommands(input, items);
    }

    // MODIFIES: this, inventory
    // EFFECTS: if user hasn't entered a name, prompts a name, then saves data to a
    // file
    // with the given name (does check if valid)
    private void saveInventory() {
        while (inventory.getCharacter() == "User") {
            System.out.println("Please enter whose inventory this is");
            String character = scanner.nextLine();
            if (validName(character)) {
                inventory.setCharacter(character.strip());
                break;
            } else {
                System.out.println("Invalid name, please try again.");
            }
        }

        System.out.println("Saving to " + inventory.getCharacter() + "'s Inventory");

        JsonWriter writer = new JsonWriter("./data/user/" + inventory.getCharacter() + ".json");
        try {
            writer.open();
            writer.write(inventory);
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Something went wrong, please try again.");
        }

        System.out.println("Inventory successfully saved under: " + inventory.getCharacter() + "!");

    }

    // EFFECT: returns true if the given character name only contains letters
    private boolean validName(String character) {
        char[] letters = character.toCharArray();
        for (char c : letters) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    // MODIFIES: this
    // EFFECT: finds a file to load and then loads it
    private void loadInventory() {
        File directory = new File("./data/user/");
        File[] files = directory.listFiles();
        if (files.length == 0) {
            System.out.println("There are no files to load!");
            return;
        }

        System.out.println("Please select a file to load:");
        int i = 1;
        for (File file : files) {
            System.out.println(i + ". " + file.getName());
            i++;
        }

        int index = processValidIndex(files.length);

        try {
            String path = files[index - 1].getPath();
            JsonReader reader = new JsonReader(path);
            Inventory newInventory = reader.read();
            inventory.setInventory(newInventory);
        } catch (IOException e) {
            System.out.println("Something went wrong, please try again.");
            return;
        }

        System.out.println(inventory.getCharacter() + "'s Inventory has been successfully loaded!");
    }

    private Boolean confirm() {
        System.out
                .println("Are you sure? Unsaved data will not be kept. Enter 'yes' if you would like to continue.");
        String confirmation = scanner.nextLine();
        return confirmation.equals("yes");
    }

    // EFFECT: Given a length, looks for user input until input is within the length
    private int processValidIndex(int length) {
        int index = -1;
        while (index < 0) {
            String indexString = scanner.nextLine();
            try {
                int index0 = Integer.parseInt(indexString);
                if (index0 <= 0 | index0 > length) {
                    throw new InvalidNumberException();
                }
                index = index0;
            } catch (NumberFormatException | InvalidNumberException ne) {
                System.out.println("Please enter a valid index.");
            }
        }
        return index;
    }

    // EFFECTS: prints out all items in inventory, with quantity if duplicates
    // favorited items get printed first
    private void printInventory(ArrayList<Item> items) {
        String name = inventory.getCharacter();
        int i = 1;
        if (inventory.getSort().isUnsorted()) {
            System.out.println(name + "'s Inventory:");
        } else {
            System.out.println(name + "'s Inventory (sorted by " + inventory.getSort().getSort() + "):");
        }
        printDivider();

        for (Item item : items) {
            if (item.isFavourite()) {
                System.out
                        .println(i + ". <f> " + item.getName() + " x " + inventory.getCount(item) + ": "
                                + item.getType());
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
    // favourite(expand) all items and its opposite
    // sort items in an inventory by a category
    // view an item by typing in its index
    private void processInventoryCommands(String input, ArrayList<Item> items) {
        switch (input) {
            case "z":
                inventory.setAllFavourite();
                break;
            case "x":
                inventory.setAllUnfavourite();
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
                } catch (NumberFormatException | IndexOutOfBoundsException oob) {
                    System.out.println("Invalid option inputted. Please enter a valid index.");
                }
        }
        printDivider();
        viewInventory();
    }

    // MODIFIES: this, inventory
    // EFFECT: Identifies the sort the user inputs and then sorts the inventory
    // according to that
    private void sortInventory() {
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

        System.out.println("Please enter order, dsc or asc, default dsc");

        while (sortOrder == null) {
            try {
                sortOrder = processSortOrder(this.scanner.nextLine());
            } catch (InvalidSortException e) {
                System.out.println("Invalid option inputted. Please try again.");
            }
        }

        try {
            inventory.sort(new Sort(SortType.valueOf(sortType), sortOrder));
            System.out.println("Inventory has been successfully sorted by " + sortType);
        } catch (InvalidSortException e) {
            System.out.println("Something went wrong, please try again");
        }
        printDivider();
    }

    // MODIFIES: this, Inventory, Item
    // EFFECT: given an individual item, displays possible user interactions such
    // as:
    // expand/shrink this item, increase/decrease the quantity of the item
    private void processItem(Item item) {
        printItem(item);
        displayItemCommands();

        while (true) {
            String input = this.scanner.nextLine();
            try {
                processItemCommands(input, item);
                break;
            } catch (InvalidOptionException e) {
                System.out.println("Invalid option inputted. Please try again.");
            }
        }
    }

    // EFFECT: prints all the info of the expanded form of the item, even if it is
    // not favourited
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
    // can set/unset favourite and increase/decrease quantity of item
    private void processItemCommands(String input, Item item) throws InvalidOptionException {
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
            default:
                throw new InvalidOptionException();
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
                        throw new InvalidNumberException();
                    }
                } catch (NumberFormatException | InvalidNumberException ne) {
                    System.out.println("Invalid option inputted. Please enter a non-negative number.");
                }
            }
        }

        for (int i = 0; i < intInput; i++) {
            inventory.addItemSorted(item);
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
                        throw new InvalidNumberException();
                    }
                } catch (NumberFormatException | InvalidNumberException ne) {
                    System.out.println("Invalid option inputted. Please enter a non-negative number.");
                }
            }
        }

        for (int i = 0; i < intInput; i++) {
            inventory.removeItem(item);
        }

        System.out.println(intInput + " items were succesfully removed!");
    }

    // MODIFIES: this, Inventory
    // EFFECT: Creates and adds an item using user input
    // If the list is unsorted, adds item to end, otherwise puts in correct place
    private void addNewItem() {
        Item item = null;

        System.out.println("Please enter the name of the item:");
        String name = this.scanner.nextLine();

        System.out.println("Please enter the type of the item:");
        System.out.println("Can be a Weapon, Armour, Consumable, Misc or Currency");
        String type = processType();

        System.out.println("Please enter the value of the item, or default 0:");
        int itemValue = processValidInt();

        System.out.println("Please enter the weight of the item, or default 0:");
        int itemWeight = processValidInt();

        System.out.println("Please enter the description of the item:");
        String desc = this.scanner.nextLine();

        try {
            item = ItemCreator.createItemFromInput(name, type, itemValue, itemWeight, desc);
        } catch (ItemCreationException e) {
            System.out.println("Something went wrong. Please try creating the item again.");
            return;
        }

        inventory.addItemSorted(item);

        System.out.println(name + " has been successfully created!");
        printDivider();
    }

    private int processValidInt() {
        int num = -1;
        while (num < 0) {
            String value = this.scanner.nextLine();
            if (value.equals("")) {
                num = 0;
            } else {
                try {
                    num = Integer.parseInt(value);
                    if (num < 0) {
                        throw new InvalidNumberException();
                    }
                } catch (NumberFormatException | InvalidNumberException ne) {
                    System.out.println("Invalid option inputted. Please enter a valid value.");
                }
            }
        }
        return num;
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
                return false;
            case "des":
            case "dec":
            case "dsc":
            case "d":
            case "":
                return true;
            default:
                throw new InvalidSortException();
        }
    }

    // EfFECT: Throws an InvalidTypeException if input is an invalid type
    private String processType() {
        String type = null;
        while (type == null) {
            try {
                type = processTypes(this.scanner.nextLine());
            } catch (InvalidTypeException te) {
                System.out.println("Invalid option inputted. Please enter a type.");
            }
        }
        return type;
    }

    private String processTypes(String type) throws InvalidTypeException {
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

    private class InvalidOptionException extends Exception {

    }
}
