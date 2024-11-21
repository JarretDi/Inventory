package ui;

import java.awt.Image;
import javax.swing.ImageIcon;

import model.exceptions.InvalidTypeException;
import model.items.Item;

/*
Represents a helper class handling calls from the GUI to get certain items
As well as a helper to scale those images
*/
public class ItemImageHandler {
    // EFFECTS: returns the image that each item should have based on type
    public static ImageIcon getImageByType(Item item, int size) throws InvalidTypeException {
        switch (item.getType()) {
            case "Weapon":
                return scaleToButtonDim(new ImageIcon("data/images/—Pngtree—sword_6045741.png"), size);
            case "Armour":
                return scaleToButtonDim(new ImageIcon("data/images/Shield1.png"), size);
            case "Consumable":
                return scaleToButtonDim(new ImageIcon("data/images/Potion1.png"), size);
            case "Currency":
                return scaleToButtonDim(new ImageIcon("data/images/currency-1590337_1280.png"), size);
            case "Misc":
                return scaleToButtonDim(new ImageIcon("data/images/Scroll1.png"), size);
            default:
                throw new InvalidTypeException();
        }
    }

    // EFFECTS: helper to scale down any image to a reasonable size for use in the GUI
    // Adapted from https://coderanch.com/t/331731/java/Resize-ImageIcon
    public static ImageIcon scaleToButtonDim(ImageIcon icon, int buttonDimensions) {
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(buttonDimensions, buttonDimensions,
                java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
