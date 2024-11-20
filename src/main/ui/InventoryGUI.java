package ui;

import javax.swing.*;

import model.Inventory;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/*
Represents a class handling the GUI of an Inventory
Structure adapted mainly from AlarmController
Contains a control panel with functionality to add items, save and load
Handles the creation of the main desktop, which all other elements are contained in
*/
public class InventoryGUI extends JFrame {
    private static final String STATUS_OK = "System OK";
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
        controlPanel.setLayout(new BorderLayout());
        addButtonPanel();
        controlPanel.pack();
        controlPanel.setVisible(true);
        desktop.add(controlPanel);

        inventoryPanel = new InventoryPanelGUI(inventory, this);
        desktop.add(inventoryPanel);

        setVisible(true);
    }

    public JDesktopPane getDesktop() {
        return desktop;
    }

    /**
     * Helper to add control buttons.
     */
    private void addButtonPanel() {
        JPanel buttonPanel = new JPanel();
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
}
