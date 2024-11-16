package model.items;

import model.exceptions.InvalidNumberException;
import model.exceptions.InvalidTypeException;
import model.exceptions.ItemCreationException;

// Represents a class used to create a subtype of item when given a type as a String
public class ItemCreator {

    // EFFECT: helper that creates and returns an item based on parameters
    // throws: InvalidTypeException if given invalid type
    // InvalidNumberException if given values for value/weight are non-negative
    public static Item createItemFromInput(String name, String type, int itemValue, int itemWeight, String desc)
            throws InvalidNumberException, InvalidTypeException {
        if (itemValue < 0 || itemWeight < 0) {
            throw new InvalidNumberException();
        }
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
                throw new InvalidTypeException();
        }
    }

    // EFFECT: helper that creates and returns an item based on only name and type
    public static Item createItemFromInput(String name, String type) throws InvalidTypeException {
        switch (type) {
            case "Weapon":
            case "w":
                return new Weapon(name);
            case "Armour":
            case "a":
                return new Armour(name);
            case "Consumable":
                return new Consumable(name);
            case "Currency":
                return new Currency(name);
            case "Misc":
            case "m":
            case "":
                return new Misc(name);
            default:
                throw new InvalidTypeException();
        }
    }
}
