package ui;

import javax.swing.*;

import model.EventLog;
import model.Inventory;
import persistence.JsonReader;
import persistence.JsonWriter;
import java.io.IOException;

import java.io.File;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;

/*
Represents a class handling the GUI of an Inventory
Structure adapted mainly from AlarmController
Contains a control panel with functionality to add items, save and load
Handles the creation of the main desktop, which all other elements are contained in
*/
public class InventoryGUI extends JFrame implements WindowListener {
    private static final String STATUS_OK = "System OK";
    private static final int buttonDimensions = 20;
    private Inventory inventory;
    private JLabel statusLabel;
    private JDesktopPane desktop;
    private JInternalFrame controlPanel;
    private InventoryPanelGUI inventoryPanel;

    private static final int WIDTH = 1366;
    private static final int HEIGHT = 768;

    // EFFECTS: Sets up the desktop and main control panels, adapted from alarm
    // controller
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
        centreOnScreen();
        desktop.addMouseListener(new DesktopFocusAction());
        addWindowListener(this);

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

    // EFFECTS: Helper to add button panels
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

    // MODIFIES: this
    // EFFECTS: removes all open item panes from desktop, and calls for a redraw
    public void clearItemEditorPanes() {
        for (Component c : desktop.getAllFrames()) {
            if (c.getClass() == ItemEditorGUI.class) {
                desktop.remove(c);
            }
        }
        desktop.revalidate();
        desktop.repaint();
    }

    // EFFECTS: Helper to centre the window on screen
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
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
        // EFFECTS: creates an instance of the SaveAction
        SaveAction() {
            super("Save");
        }

        // MODIFIES: inventory
        // EFFECTS: Setsup a new save window, and handles save functionality
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
                inventoryPanel.refreshNameOnCharacterChange();
                inventoryPanel.revalidate();
                JOptionPane.showMessageDialog(null,
                        inventory.getCharacter() + "'s inventory has been successfully saved!",
                        "Success!",
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
        // EFFECTS: creates an instance of the LoadAction
        LoadAction() {
            super("Load");
        }

        // MODIFIES: inventory
        // EFFECTS: Setsup a a loading window, and handles loading functionality
        @Override
        public void actionPerformed(ActionEvent evt) {
            if (inventory.getNumItems() != 0) {
                JOptionPane.showConfirmDialog(null,
                        "Unsaved data will not be kept. Are you sure you want to continue?", "Confirm",
                        JOptionPane.YES_NO_OPTION);
            }

            String[] characterList = createCharacterList();
            JComboBox<String> savedCharacters = new JComboBox<String>(characterList);

            JOptionPane.showMessageDialog(null, savedCharacters, "Who's inventory would you like to load?",
                    JOptionPane.QUESTION_MESSAGE);

            tryLoading((String) savedCharacters.getSelectedItem());
        }

        // EFFECTS: Helper that returns the list of all possible characters to load
        private String[] createCharacterList() {
            File directory = new File("./data/user/");
            File[] files = directory.listFiles();

            String[] characterList = new String[files.length];

            for (int i = 0; i < files.length; i++) {
                characterList[i] = files[i].getName();
            }
            return characterList;
        }

        // MODIFIES: this
        // EFFECT: Helper that attempts to load the inventory
        // Additionally, calls for a refresh on things it should affect
        private void tryLoading(String character) {
            try {
                String path = "./data/user/" + character;
                JsonReader reader = new JsonReader(path);
                Inventory newInventory = reader.read();
                inventory.setInventory(newInventory);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Something went wrong, please try again.",
                        "System Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            clearItemEditorPanes();
            inventoryPanel.refreshNameOnCharacterChange();
            inventoryPanel.redrawItemPane();
            inventoryPanel.revalidate();
            JOptionPane.showMessageDialog(null,
                    inventory.getCharacter() + "'s inventory has been successfully loaded!",
                    "Success!",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
        // No specific action
    }

    @Override
    public void windowClosing(WindowEvent e) {
        for (model.Event evt : EventLog.getInstance()) {
            System.out.println(evt.toString());
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
        // No specific action
    }

    @Override
    public void windowIconified(WindowEvent e) {
        // No specific action
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // No specific action
    }

    @Override
    public void windowActivated(WindowEvent e) {
        // No specific action
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // No specific action
    }
}
