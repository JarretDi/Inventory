package ui;

import javax.swing.*;

import model.Inventory;
import persistence.JsonWriter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;

/*
Represents a class handling the GUI of an Inventory
Structure adapted mainly from AlarmController
Contains a control panel with functionality to add items, save and load
Handles the creation of the main desktop, which all other elements are contained in
*/
public class InventoryGUI extends JFrame {
    private static final String STATUS_OK = "System OK";
    private static final int buttonDimensions = 20;
    private Inventory inventory;
    private JLabel statusLabel;
    private JDesktopPane desktop;
    private JInternalFrame controlPanel;
    private InventoryPanelGUI inventoryPanel;

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    // Sets up the desktop and main control panels, adapted from alarm controller
    public InventoryGUI(Inventory inventory) {
        super("Inventory UI");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.inventory = inventory;

        statusLabel = new JLabel(STATUS_OK);
        add(statusLabel, BorderLayout.NORTH);

        desktop = new JDesktopPane();
        setContentPane(desktop);
        setTitle("Inventory Handler");
        setSize(WIDTH, HEIGHT);
        desktop.addMouseListener(new DesktopFocusAction());

        controlPanel = new JInternalFrame("Control Panel", true, false, false, false);
        controlPanel.setLayout(new GridLayout(3, 1));
        controlPanel.setPreferredSize(new Dimension(WIDTH / 4, HEIGHT / 3));
        addButtonPanel();
        controlPanel.pack();
        controlPanel.setVisible(true);
        desktop.add(controlPanel);

        inventoryPanel = new InventoryPanelGUI(inventory, this);
        desktop.add(inventoryPanel);

        setVisible(true);
    }

    /**
     * Helper to add control buttons.
     */
    private void addButtonPanel() {
        JButton addButton = new JButton(new AddItemAction());
        addButton.setIcon(ItemImageHandler.scaleToButtonDim(new ImageIcon("data/images/iconoir--plus.png"),
                buttonDimensions * 2));
        JButton saveButton = new JButton(new SaveAction());
        saveButton.setIcon(ItemImageHandler.scaleToButtonDim(
                new ImageIcon("data/images/iconoir--floppy-disk-arrow-in.png"), buttonDimensions * 2));
        JButton loadButton = new JButton(new LoadAction());
        loadButton.setIcon(ItemImageHandler.scaleToButtonDim(
                new ImageIcon("data/images/iconoir--floppy-disk-arrow-out.png"), buttonDimensions * 2));

        controlPanel.add(addButton);
        controlPanel.add(saveButton);
        controlPanel.add(loadButton);
    }

    /**
     * Some getters to be used in other panels after being passed 'this'
     * to set consistent sizing of components and windows.
     */
    public JDesktopPane getDesktop() {
        return desktop;
    }

    public int getButtonDimensions() {
        return buttonDimensions;
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
        // EFFECTS: creates an instance of the AddItemAction
        AddItemAction() {
            super("Add Item");
        }

        // MODIFIES: inventory
        // EFFECTS: Setsup a new Item Creator window, and handles submit functionality
        @Override
        public void actionPerformed(ActionEvent evt) {
            desktop.add(new ItemEditorGUI(inventory, inventoryPanel));
        }
    }

    /**
     * Represents action to be taken when user wants to save their inventory.
     */
    private class SaveAction extends AbstractAction {
        // EFFECTS: creates an instance of the AddItemAction
        SaveAction() {
            super("Save");
        }

        // MODIFIES: inventory
        // EFFECTS: Setsup a new Item Creator window, and handles submit functionality
        @Override
        public void actionPerformed(ActionEvent evt) {
            String character = JOptionPane.showInputDialog("Who's inventory is this?", inventory.getCharacter());
            if (character == null) {
                return;
            }

            inventory.setCharacter(character);
            JsonWriter writer = new JsonWriter("./data/user/" + inventory.getCharacter() + ".json");
            try {
                writer.open();
                writer.write(inventory);
                writer.close();
                JOptionPane.showMessageDialog(null, inventory.getCharacter() + "'s inventory has been successfully saved!",
                        "Success",
                        JOptionPane.PLAIN_MESSAGE);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Something went wrong, please try entering a valid name",
                        "System Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Represents action to be taken when user wants to save their inventory.
     */
    private class LoadAction extends AbstractAction {
        // EFFECTS: creates an instance of the AddItemAction
        LoadAction() {
            super("Load");
        }

        // MODIFIES: inventory
        // EFFECTS: Setsup a new Item Creator window, and handles submit functionality
        @Override
        public void actionPerformed(ActionEvent evt) {
            // stub
        }
    }
}
