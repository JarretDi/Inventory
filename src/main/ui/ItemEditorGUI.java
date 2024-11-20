package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

import model.Inventory;
import model.exceptions.InvalidNumberException;
import model.exceptions.InvalidTypeException;
import model.exceptions.ItemCreationException;
import model.items.Item;
import model.items.ItemCreator;

/*
Represents a class handling the GUI and functionality to customize items
Intended to be created from two sources, either when creating a new item,
or editing an existing one
*/
public class ItemEditorGUI extends JInternalFrame {
    private Inventory inventory;
    private InventoryPanelGUI inventoryPanel;
    private Item initialItem;

    private JButton addButton;

    private JTextField nameField;
    private JComboBox<String> typeBox;
    private JTextField valueField;
    private JTextField weightField;
    private JTextField quantityField;
    private JTextArea descArea;
    private JCheckBox favouriteBox;

    private String name;
    private String type;
    private int value;
    private int weight;
    private String desc;
    private int quantity;
    private Boolean favourite;

    // EFFECTS: Constructs a new addItemMenu panel with an inventory and inventory
    // panel to put it into
    // this constructor creates an item from zero
    public ItemEditorGUI(Inventory inventory, InventoryPanelGUI inventoryPanel) {
        super("Item Creator", false, true, false, false);

        this.inventory = inventory;
        this.inventoryPanel = inventoryPanel;
        this.initialItem = null;
        showMenu();
    }

    // EFFECTS: Helper to setup fields for item creation as well as lay it out nice
    // visually
    // All fields are set to defaults
    private void showMenu() {
        nameField = new JTextField("Name", 10);
        String[] types = { "Type:", "Weapon", "Armour", "Consumable", "Currency", "Misc" };
        typeBox = new JComboBox<String>(types);
        valueField = new JTextField("Value", 10);
        weightField = new JTextField("Weight", 10);
        quantityField = new JTextField("Quantity", 10);
        descArea = new JTextArea("Description:", 8, 20);
        favouriteBox = new JCheckBox("f?");

        addButton = new JButton("Add");
        addButton.addActionListener(new SubmitItemAction());

        setLayout(new GridBagLayout());

        drawEditor(new GridBagConstraints());

        setVisible(true);
    }

    // EFFECTS: Constructs a new addItemMenu panel with an inventory and inventory
    // panel to put it into
    // this constructor starts with an initial item to edit
    public ItemEditorGUI(Item initialItem, Inventory inventory, InventoryPanelGUI inventoryPanel) {
        super("Item Editor", false, true, false, false);
        this.inventory = inventory;
        this.inventoryPanel = inventoryPanel;
        this.initialItem = initialItem;
        showMenu(initialItem);
    }

    // EFFECTS: Helper to setup fields for item creation as well as lay it out nice
    // visually
    // Fields are determined from initial item given
    private void showMenu(Item initialItem) {
        nameField = new JTextField(initialItem.getName(), 10);
        String[] types = { initialItem.getType(), "Weapon", "Armour", "Consumable", "Currency", "Misc" };
        typeBox = new JComboBox<String>(types);
        valueField = new JTextField(Integer.toString(initialItem.getValue()), 10);
        weightField = new JTextField(Integer.toString(initialItem.getWeight()), 10);
        quantityField = new JTextField(Integer.toString(inventory.getCount(initialItem)), 10);
        descArea = new JTextArea(initialItem.getDescription(), 8, 20);
        favouriteBox = new JCheckBox("f?", initialItem.isFavourite());

        addButton = new JButton("Edit");
        addButton.addActionListener(new SubmitItemAction());

        setLayout(new GridBagLayout());

        drawEditor(new GridBagConstraints());

        setVisible(true);
    }

    // EFFECTS: draws and packs the editor window using a somewhat confusing
    // gridBagLayout
    // Updating c adapted from https://www.geeksforgeeks.org/java-awt-gridbaglayout-class/
    private void drawEditor(GridBagConstraints c) {
        add(typeBox, c);

        c.gridy = 1;
        add(nameField, c);

        c.gridy = 2;
        add(valueField, c);

        c.gridy = 3;
        add(weightField, c);

        c.gridy = 4;
        add(quantityField, c);

        c.gridy = 5;
        JPanel submitPanel = new JPanel();
        submitPanel.add(addButton);
        submitPanel.add(favouriteBox);
        add(submitPanel, c);

        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 6;
        JScrollPane descScroll = new JScrollPane(descArea);
        add(descScroll, c);

        pack();
    }

    // Handles submit functionality for when the item has finished being customized
    private class SubmitItemAction extends AbstractAction {
        // EFFECTS: Constructs a SubmitItemAction
        public SubmitItemAction() {
            super();
        }

        // MODIFIES: inventoryPanel, inventory
        // EFFECTS: When button is pressed:
        // reads fields from text boxes,
        // creates an item from given fields, adds it to inventory
        // then calls for an update on inventoryPanel
        @Override
        public void actionPerformed(ActionEvent e) {
            acceptInput();
            try {
                if (quantity < 1) {
                    throw new InvalidNumberException();
                }
                inventory.removeAllItem(initialItem);
                Item item = ItemCreator.createItemFromInput(name, type, value, weight, desc);
                if (favourite) {
                    item.setFavourite();
                }
                for (int i = 1; i <= quantity; i++) {
                    inventory.addItemSorted(item);
                }
            } catch (ItemCreationException e1) {
                JOptionPane.showMessageDialog(null, "Please enter valid inputs for value, weight and quantity",
                        "System Error",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                dispose();
                inventoryPanel.redrawItemPane();
            }
        }

        // MODIFIES: this
        // EFFECTS: helper for above method
        // reads from user input and updates fields in accordance to it
        public void acceptInput() {
            name = nameField.getText();
            type = typeBox.getSelectedItem() == "Type:" ? "Misc" : (String) typeBox.getSelectedItem();

            String valueString = valueField.getText();
            value = (valueString.equals("Value") || valueString.equals("")) ? 0 : Integer.parseInt(valueString);

            String weightString = weightField.getText();
            weight = (weightString.equals("Weight") || weightString.equals("")) ? 0 : Integer.parseInt(weightString);

            String quantityString = quantityField.getText();
            quantity = (quantityString.equals("Quantity") || quantityString.equals("")) ? 1
                    : Integer.parseInt(quantityString);

            favourite = favouriteBox.isSelected();

            desc = descArea.getText();
        }
    }
}
