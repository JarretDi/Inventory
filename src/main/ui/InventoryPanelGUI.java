package ui;

import java.awt.event.ActionEvent;
import java.awt.*;

import javax.swing.*;

import model.Inventory;
import model.Sort;
import model.Sort.SortType;
import model.exceptions.InvalidSortException;
import model.exceptions.InvalidTypeException;
import model.items.Item;

/*
Represents a class handling the GUI and functionality of an inventory
*/
public class InventoryPanelGUI extends JInternalFrame {
    private static final int arrowDimensions = 15;

    private Inventory inventory;
    private JDesktopPane desktop;
    private String layout;

    private int buttonDimensions;
    private int width;
    private int height;

    private JPanel topBar;
    private JPanel itemPane;
    private JScrollPane itemScrollPane;

    private JButton nameButton;
    private JButton typeButton;
    private JButton valueButton;
    private JButton weightButton;

    // EFFECTS: Constructs an inventory given an inventory and parent to add it to
    public InventoryPanelGUI(Inventory inventory, InventoryGUI parent) {
        super("Inventory", false, false, true, false);
        this.inventory = inventory;
        layout = "List";

        this.itemPane = new JPanel();
        this.itemScrollPane = new JScrollPane();
        this.desktop = parent.getDesktop();
        this.buttonDimensions = parent.getButtonDimensions();

        width = parent.getWidth() / 2;
        height = parent.getHeight() - 30;

        setLayout(new BorderLayout());

        setPosition();
        addPanels();

        setPreferredSize(new Dimension(width, height));

        pack();

        setVisible(true);
    }

    /**
     * Helper to add inventory viewing.
     */
    private void addPanels() {
        addTopBar();
        addSortBar();
        redrawItemPane();
    }

    // MODIFIES: this
    // EFFECTS: Helper to add top bar which includes title and layout bar
    private void addTopBar() {
        topBar = new JPanel();
        topBar.setLayout(new BorderLayout());

        topBar.add(new JLabel("   " + inventory.getCharacter() + "'s Inventory:"), BorderLayout.WEST);

        JPanel layoutBar = createLayoutBar();

        topBar.add(layoutBar, BorderLayout.EAST);
        topBar.setVisible(true);

        add(topBar, BorderLayout.NORTH);
    }

    // MODIFIES: this
    // EFFECTS: Called when an action is taken to change the name of the character (saving/loading)
    // Updates the top bar
    public void refreshNameOnCharacterChange() {
        topBar.removeAll();

        topBar.add(new JLabel("   " + inventory.getCharacter() + "'s Inventory:"), BorderLayout.WEST);

        JPanel layoutBar = createLayoutBar();

        topBar.add(layoutBar, BorderLayout.EAST);
        topBar.setVisible(true);
    }
    
    // MODIFIES: this
    // EFFECTS: Helper to add layout bar which includes buttons to change layout type
    private JPanel createLayoutBar() {
        JPanel layoutBar = new JPanel();

        layoutBar.setSize(width, buttonDimensions);

        ImageIcon gridIcon = ItemImageHandler.scaleToButtonDim(new ImageIcon("./data/images/iconoir--view-grid.png"),
                buttonDimensions);

        ImageIcon listIcon = ItemImageHandler
                .scaleToButtonDim(new ImageIcon("./data/images/iconoir--view-columns-2.png"), buttonDimensions);

        LayoutAction layoutAction = new LayoutAction();

        JButton gridButton = new JButton(layoutAction);
        gridButton.setIcon(gridIcon);
        gridButton.setActionCommand("Grid");

        JButton listButton = new JButton(layoutAction);
        listButton.setIcon(listIcon);
        listButton.setActionCommand("List");

        layoutBar.add(gridButton);
        layoutBar.add(listButton);

        layoutBar.setVisible(true);
        return layoutBar;
    }

    // MODIFIES: this
    // EFFECTS: Helper to add sort bar at bottom of inventory, which includes buttons to change the sort order
    private void addSortBar() {
        JPanel sortBar = new JPanel();

        sortBar.setSize(width, buttonDimensions);
        sortBar.setLayout(new FlowLayout());

        SortAction sortAction = new SortAction();

        nameButton = new JButton(sortAction);
        nameButton.setText("Name");
        nameButton.setActionCommand("Name");

        typeButton = new JButton(sortAction);
        typeButton.setText("Type");
        typeButton.setActionCommand("Type");

        valueButton = new JButton(sortAction);
        valueButton.setText("Value");
        valueButton.setActionCommand("Value");

        weightButton = new JButton(sortAction);
        weightButton.setText("Weight");
        weightButton.setActionCommand("Weight");

        sortBar.add(nameButton);
        sortBar.add(typeButton);
        sortBar.add(valueButton);
        sortBar.add(weightButton);
        sortBar.setVisible(true);

        add(sortBar, BorderLayout.SOUTH);
    }

    // MODIFIES: this
    // EFFECT: redraws the items based on current layout
    // intended to be safe to call following any action that changes the inventory
    public void redrawItemPane() {
        itemPane.removeAll();
        try {
            if (layout.equals("List")) {
                addItemPaneList();
            } else {
                addItemPaneGrid();
            }
        } catch (InvalidTypeException e) {
            JOptionPane.showMessageDialog(null, "Something went wrong, please try again", "System Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        itemPane.setVisible(true);

        itemScrollPane.setViewportView(itemPane);

        add(itemScrollPane, BorderLayout.CENTER);
    }

    // REQUIRES: current layout is list
    // MODIFIES: this
    // EFFECT: helper to iterate over an inventory to draw each unique item in a list
    private void addItemPaneList() throws InvalidTypeException {
        itemPane.setLayout(new BoxLayout(itemPane, BoxLayout.Y_AXIS));

        for (Item item : inventory.getProcessedInventory()) {
            JPanel nextItem = createItemListView(item);
            itemPane.add(nextItem);
        }
    }

    // REQUIRES: current layout is list
    // MODIFIES: this
    // EFFECT: helper to create a nice visual panel for each item 
    private JPanel createItemListView(Item item) throws InvalidTypeException {
        JPanel nextItem = new JPanel();
        nextItem.setLayout(new BorderLayout());
        nextItem.setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel leftPanel = createLeftListPanel(item);

        JPanel rightPanel = createRightListPanel(item);

        nextItem.add(leftPanel, BorderLayout.WEST);
        nextItem.add(rightPanel, BorderLayout.EAST);

        nextItem.setMaximumSize(new Dimension(width, buttonDimensions * 2));
        nextItem.setVisible(true);
        return nextItem;
    }

    // REQUIRES: current layout is list
    // MODIFIES: this
    // EFFECT: helper to create the left panel for item panel, which includes
    // image (determined by type), name, quantity and favourite star (if favourite) 
    private JPanel createLeftListPanel(Item item) throws InvalidTypeException {
        JPanel leftPanel = new JPanel(new FlowLayout());

        leftPanel.add(new JLabel(ItemImageHandler.getImageByType(item, (int) Math.round(buttonDimensions * 1.5))));
        leftPanel.add(new JLabel(item.getName()));
        leftPanel.add(new JLabel("x" + Integer.toString(inventory.getCount(item))));
        if (item.isFavourite()) {
            leftPanel.add(new JLabel(ItemImageHandler
                    .scaleToButtonDim(new ImageIcon("data/images/iconoir--star.png"),
                            (int) Math.round(buttonDimensions * 1.5))));
        }
        return leftPanel;
    }

    // REQUIRES: current layout is list
    // MODIFIES: this
    // EFFECT: helper to create the right panel for item panel, which includes
    // type, value, weight and edit button
    private JPanel createRightListPanel(Item item) {
        JPanel rightPanel = new JPanel(new FlowLayout());

        rightPanel.add(new JLabel(item.getType()));
        rightPanel.add(new JLabel("V: " + Integer.toString(item.getValue())));
        rightPanel.add(new JLabel("W: " + Integer.toString(item.getWeight())));
        JButton editButton = new JButton(ItemImageHandler
                .scaleToButtonDim(new ImageIcon("data/images/iconoir--edit-pencil.png"), buttonDimensions));
        editButton.addActionListener(new EditAction(item));
        editButton.setPreferredSize(
                new Dimension((int) Math.round(buttonDimensions * 1.5), (int) Math.round(buttonDimensions * 1.5)));
        rightPanel.add(editButton);
        return rightPanel;
    }

    // REQUIRES: current layout is grid
    // MODIFIES: this
    // EFFECT: helper to iterate over an inventory to draw each unique item in a grid
    private void addItemPaneGrid() throws InvalidTypeException {
        itemPane.setLayout(new GridLayout(0, 3));
        
        for (Item item : inventory.getProcessedInventory()) {
            JPanel nextItem = createItemGridView(item);
            itemPane.add(nextItem);
        }
    }

    // REQUIRES: current layout is grid
    // MODIFIES: this
    // EFFECT: helper to create a nice visual panel for each item 
    private JPanel createItemGridView(Item item) throws InvalidTypeException {
        JPanel nextItem = new JPanel();
        nextItem.setLayout(new BorderLayout());
        nextItem.setBorder(BorderFactory.createLineBorder(Color.black));

        createTopGridPanel(item, nextItem);

        nextItem.add(new JLabel(ItemImageHandler.getImageByType(item, width / 4)), BorderLayout.CENTER);

        createBottomGridPanel(item, nextItem);

        return nextItem;
    }

    // REQUIRES: current layout is grid
    // MODIFIES: this
    // EFFECT: helper to create a top bar for grid layout, which includes:
    // Name, quantity and edit button
    private void createTopGridPanel(Item item, JPanel nextItem) {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.add(new JLabel("   " + item.getName() + " x" + inventory.getCount(item)), BorderLayout.CENTER);

        JButton editButton = new JButton(ItemImageHandler
                .scaleToButtonDim(new ImageIcon("data/images/iconoir--edit-pencil.png"), buttonDimensions));
        editButton.addActionListener(new EditAction(item));
        editButton.setPreferredSize(
                new Dimension((int) Math.round(buttonDimensions), (int) Math.round(buttonDimensions)));
        topBar.add(editButton, BorderLayout.EAST);

        nextItem.add(topBar, BorderLayout.NORTH);
    }

    // REQUIRES: current layout is grid
    // MODIFIES: this
    // EFFECT: helper to create the bottom bar for grid layout, which includes:
    // Value, weight and whether the item is favourited
    private void createBottomGridPanel(Item item, JPanel nextItem) {
        JPanel bottomBar = new JPanel(new BorderLayout());

        bottomBar.add(new JLabel("   V: " + item.getValue() + "   W: " + item.getWeight()), BorderLayout.CENTER);

        if (item.isFavourite()) {
            bottomBar.add(new JLabel(ItemImageHandler
                    .scaleToButtonDim(new ImageIcon("data/images/iconoir--star.png"),
                            (int) Math.round(buttonDimensions * 1.5))), BorderLayout.EAST);
        }
        
        nextItem.add(bottomBar, BorderLayout.SOUTH);
    }

    // MODIFIES: this
    // EFFECTS: helper for constructor, sets the panel in the right half of screen
    private void setPosition() {
        setLocation(width, 0);
    }

    /*
    Represents a class handling action to be taken when an action is taken to modify
    layout of inventoryPanel
    */
    private class LayoutAction extends AbstractAction {
        // EFFECTS: constructs a LayoutAction
        LayoutAction() {
            super();
        }

        // REQUIRES: e is one of "List", "Grid"
        // MODIFIES: this
        // EFFECT: changes layout of InventoryPanel in response to button press
        // additionally, since this can affect the drawing of the inventory calls a redraw
        @Override
        public void actionPerformed(ActionEvent e) {
            layout = e.getActionCommand();
            redrawItemPane();
        }
    }

    /*
    Represents a class handling action to be taken when an action is taken to modify
    sort of inventory
    */
    private class SortAction extends AbstractAction {
        // EFFECTS: constructs a SortAction
        SortAction() {
            super();
        }

        // MODIFIES: this, inventory
        // EFFECT: changes sort of inventory in response to button press
        // additionally, since this can affect the drawing of the inventory calls a redraw
        @Override
        public void actionPerformed(ActionEvent e) {
            SortType originalSort = inventory.getSort().getSort();
            SortType sort = SortType.valueOf(e.getActionCommand());
            Boolean order = inventory.getSort().getOrder();

            if (inventory.getSort().isUnsorted()) {
                order = false;
            }
            if (!sort.equals(originalSort)) {
                order = false;
            }

            try {
                inventory.sort(new Sort(sort, !order));
            } catch (InvalidSortException e1) {
                JOptionPane.showMessageDialog(null, "Something went wrong, please try again", "System Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            resolveSortIcons();
            redrawItemPane();
        }

        // MODIFIES: this, sortButtons
        // EFFECTS: helper that grabs the image based on sort order and scales it
        private void resolveSortIcons() {
            ImageIcon sortIcon;
            if (inventory.getSort().getOrder()) {
                sortIcon = new ImageIcon("./data/images/iconoir--nav-arrow-down.png");
            } else {
                sortIcon = new ImageIcon("./data/images/iconoir--nav-arrow-up.png");
            }
    
            sortIcon = ItemImageHandler.scaleToButtonDim(sortIcon, arrowDimensions);
    
            changeButtons(sortIcon);
        }
    
        // MODIFIES: this, sortButtons
        // EFFECTS: helper to change buttons in response to an inventory's new sort
        private void changeButtons(ImageIcon sortIcon) {
            nameButton.setIcon(null);
            typeButton.setIcon(null);
            valueButton.setIcon(null);
            weightButton.setIcon(null);
    
            switch (inventory.getSort().getSort()) {
                case Name:
                    nameButton.setIcon(sortIcon);
                    break;
                case Type:
                    typeButton.setIcon(sortIcon);
                    break;
                case Value:
                    valueButton.setIcon(sortIcon);
                    break;
                case Weight:
                    weightButton.setIcon(sortIcon);
                    break;
            }
        }
    }

    /*
    Represents a class handling action to be taken when an action is taken to modify
    an item within an inventory
    */
    private class EditAction extends AbstractAction {
        Item itemToEdit;

        // EFFECTS: Creates an instance of EditAction, being passed the item to edit
        public EditAction(Item itemToEdit) {
            super();
            this.itemToEdit = itemToEdit;
        }

        // EFFECTS: creates a new window of itemEditorGUI with the item to edit
        @Override
        public void actionPerformed(ActionEvent e) {
            desktop.add(new ItemEditorGUI(itemToEdit, inventory, getThis()));
        }
    }

    /*
    Weird bit of code that's necesary due to implementation of EditAction, as the itemCreator needs
    this inventoryPanel, but calling 'this' within the method only gives the Edit Action.
    Will remain until a better solution is found
    */
    private InventoryPanelGUI getThis() {
        return this;
    }
}
