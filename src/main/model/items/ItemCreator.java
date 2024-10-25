package model.items;

import Exceptions.ItemCreationException;

// Represents a class used to create a subtype of item when given a type as a String
public class ItemCreator {
    // EFFECT: helper that creates and returns an item based on parameters
    public static Item createItemFromInput(String name, String type, int itemValue, int itemWeight, String desc)
            throws ItemCreationException {
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
}
