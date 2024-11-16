package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.Inventory;
import model.exceptions.InvalidNumberException;
import model.exceptions.InvalidTypeException;
import model.items.Item;
import model.items.ItemCreator;

public class ItemEditorGUI extends JInternalFrame {
    private Inventory inventory;
    private JDesktopPane desktop;

    private JInternalFrame addItemPane;
    private JButton addButton;
    private JTextField nameField;
    private JComboBox<String> typeBox;
    private JTextField valueField;
    private JTextField weightField;
    private JTextField quantityField;
    private JTextArea descArea;

    private String name;
    private String type;
    private int value;
    private int weight;
    private String desc;
    private int quantity;

    public ItemEditorGUI(Inventory inventory, JDesktopPane desktop) {
        this.inventory = inventory;
        this.desktop = desktop;
        showMenu();
    }

    // EFFECTS: Helper to setup fields for item creation as well as lay it out nice
    // visually
    private void showMenu() {
        addItemPane = new JInternalFrame("Item Creator", false, true, false, false);

        nameField = new JTextField("Name", 10);
        String[] types = { "Type:", "Weapon", "Armour", "Consumable", "Currency", "Misc" };
        typeBox = new JComboBox<String>(types);
        valueField = new JTextField("Value", 10);
        weightField = new JTextField("Weight", 10);
        quantityField = new JTextField("Quantity", 10);
        descArea = new JTextArea("Description:", 8, 20);
        descArea.setAutoscrolls(true);

        addButton = new JButton("Add");
        addButton.addActionListener(new SubmitItemAction());

        addItemPane.setLayout(new GridBagLayout());

        drawAddItemPane(new GridBagConstraints());

        addItemPane.setVisible(true);
        desktop.add(addItemPane);
    }

    private void drawAddItemPane(GridBagConstraints c) {
        c.gridx = 0;
        c.gridy = 0;
        addItemPane.add(typeBox, c);

        c.gridx = 0;
        c.gridy = 1;
        addItemPane.add(nameField, c);

        c.gridx = 0;
        c.gridy = 2;
        addItemPane.add(valueField, c);

        c.gridx = 0;
        c.gridy = 3;
        addItemPane.add(weightField, c);

        c.gridx = 0;
        c.gridy = 4;
        addItemPane.add(quantityField, c);

        c.gridx = 0;
        c.gridy = 5;
        addItemPane.add(addButton, c);

        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 6;
        addItemPane.add(descArea, c);

        addItemPane.pack();
    }

    public void acceptInput() {
        name = nameField.getText();
        type = typeBox.getSelectedItem() == "Type:" ? "Misc" : (String) typeBox.getSelectedItem();

        String valueString = valueField.getText();
        if (valueString.equals("Value") || valueString.equals("")) {
            value = 0;
        } else {
            value = Integer.parseInt(valueString);
        }

        String weightString = weightField.getText();
        if (weightString.equals("Weight") || weightString.equals("")) {
            weight = 0;
        } else {
            weight = Integer.parseInt(weightString);
        }

        String quantityString = quantityField.getText();
        if (quantityString.equals("Quantity") || quantityString.equals("")) {
            quantity = 1;
        } else {
            quantity = Integer.parseInt(quantityString);
        }

        desc = descArea.getText();
    }

    private class SubmitItemAction extends AbstractAction {
        public SubmitItemAction() {
            super("Add");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            acceptInput();

            try {
                if (value < 0 || weight < 0 || quantity < 1) {
                    throw new NumberFormatException();
                }
                Item item = ItemCreator.createItemFromInput(name, type, value, weight, desc);
                for (int i = 0; i < quantity; i++) {
                    inventory.addItemSorted(item);
                }
            } catch (InvalidNumberException e1) {
                JOptionPane.showMessageDialog(null, "Please enter valid inputs for value weight and quantity",
                        "System Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (InvalidTypeException e1) {
                JOptionPane.showMessageDialog(null, "Something went wrong, please try again", "System Error",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                addItemPane.dispose();
            }
        }
    }
}
