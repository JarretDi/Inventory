package ui;

import javax.swing.*;

import model.Inventory;
import model.exceptions.ItemCreationException;
import model.items.Item;
import model.items.ItemCreator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// Represents a class handling the GUI of an Inventory
// Structure adapted mainly from AlarmController
public class InventoryGUI extends JFrame {
    private static final String STATUS_OK = "System OK";
    private Inventory inventory;
    private JLabel statusLabel;
    private JDesktopPane desktop;
    private JInternalFrame controlPanel;

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    // Sets up the desktop and main control panels, adapted from alarm controller
    public InventoryGUI(Inventory inventory) {
        super("Inventory UI");

        this.inventory = inventory;

        statusLabel = new JLabel(STATUS_OK);
        add(statusLabel, BorderLayout.NORTH);

        desktop = new JDesktopPane();
        setContentPane(desktop);
        setTitle("Inventory Handler");
        setSize(WIDTH, HEIGHT);
        desktop.addMouseListener(new DesktopFocusAction());

        controlPanel = new JInternalFrame("Control Panel", true, false, false, false);
        controlPanel.setLayout(new BorderLayout());
        addButtonPanel();
        controlPanel.pack();
        controlPanel.setVisible(true);

        desktop.add(controlPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Helper to add control buttons.
     */
    private void addButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 10));
        buttonPanel.add(new JButton(new AddItemAction()));

        controlPanel.add(buttonPanel, BorderLayout.WEST);
    }

    /**
     * Represents action to be taken when user clicks desktop
     * to switch focus. (Needed for key handling.)
     */
    private class DesktopFocusAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            InventoryGUI.this.requestFocusInWindow();
        }
    }

    /**
     * Represents action to be taken when user wants to add a new item
     * to the inventory.
     */
    private class AddItemAction extends AbstractAction {
        private JInternalFrame addItemPane;
        private JButton addButton;

        private JTextField nameField;
        private JComboBox<String> typeBox;
        private JTextField valueField;
        private JTextField weightField;
        private JTextField quantityField;
        private JTextArea descArea;

        AddItemAction() {
            super("Add Item");
        }

        // MODIFIES: inventory
        // EFFECTS: Setsup a new Item Creator window, and handles submit functionality 
        @Override
        public void actionPerformed(ActionEvent evt) {
            if (evt.getActionCommand().equals("Add")) {
                try {
                    String name = nameField.getText();

                    String type = (String) typeBox.getSelectedItem();
                    if (typeBox.getSelectedItem() == "Type:") {
                        type = "Misc";
                    }

                    String valueString = valueField.getText();
                    int value;
                    if (valueString.equals("Value") || valueString.equals("")) {
                        value = 0;
                    } else {
                        value = Integer.parseInt(valueString);
                    }

                    String weightString = weightField.getText();
                    int weight;
                    if (weightString.equals("Weight") || weightString.equals("")) {
                        weight = 0;
                    } else {
                        weight = Integer.parseInt(weightString);
                    }

                    String quantityString = quantityField.getText();
                    int quantity;
                    if (quantityString.equals("Quantity") || quantityString.equals("")) {
                        quantity = 1;
                    } else {
                        quantity = Integer.parseInt(quantityString);
                    }

                    String desc = descArea.getText();

                    if (value < 0 || weight < 0 || quantity < 1) {
                        throw new NumberFormatException();
                    }

                    Item item = ItemCreator.createItemFromInput(name, type, value, weight, desc);
                    for (int i = 0; i < quantity; i++) {
                        inventory.addItemSorted(item);
                    }

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid value for value and weight",
                            "System Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (ItemCreationException e) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid type", "System Error",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    addItemPane.dispose();
                }
            } else {
                showMenu();
            }
        }

        // EFFECTS: Helper to setup fields for item creation as well as lay it out nice visually
        private void showMenu() {
            addItemPane = new JInternalFrame("Item Creator", false, true, false, false);

            nameField = new JTextField("Name", 10);
            String[] types = { "Type:", "Weapon", "Armour", "Consumable", "Currency", "Misc" };
            typeBox = new JComboBox<String>(types);
            valueField = new JTextField("Value", 10);
            weightField = new JTextField("Weight", 10);
            quantityField = new JTextField("Quantity", 10);
            descArea = new JTextArea("Description:", 8, 20);
            addButton = new JButton("Add");
            addButton.addActionListener(this);

            addItemPane.setLayout(new GridBagLayout());

            GridBagConstraints c = new GridBagConstraints();

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

            addItemPane.setVisible(true);
            desktop.add(addItemPane);
        }
    }
}
