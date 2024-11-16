package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Inventory;
import model.Sort;
import model.Sort.SortType;
import model.exceptions.InvalidSortException;
import model.items.Item;

public class InventoryPanel extends JInternalFrame {
    private static final int buttonDimensions = 15;
    private int width;
    private int height;

    private Inventory inventory;
    private Component parent;
    private String layout;

    private JPanel inventoryHandler;
    private JPanel itemPane;

    private JButton nameButton;
    private JButton typeButton;
    private JButton valueButton;
    private JButton weightButton;

    public InventoryPanel(Inventory inventory, Component parent) {
        super("Inventory", false, false, false, false);
        this.inventory = inventory;
        this.parent = parent;
        layout = "List";

        width = parent.getWidth() / 2;
        height = parent.getHeight();

        setLayout(new BorderLayout());

        setPosition(parent);
        setSize(width, height);

        addPanels();

        setVisible(true);
    }

    /**
     * Helper to add inventory viewing.
     */
    private void addPanels() {
        addTopBar();
        addInventoryHandler();
    }

    private void addTopBar() {
        JPanel topBar = new JPanel();
        topBar.setLayout(new BorderLayout());

        topBar.add(new JLabel("   " + inventory.getCharacter() + "'s Inventory:"), BorderLayout.WEST);

        JPanel layoutBar = createLayoutBar();

        topBar.add(layoutBar, BorderLayout.EAST);
        topBar.setVisible(true);

        add(topBar, BorderLayout.NORTH);
    }

    private JPanel createLayoutBar() {
        JPanel layoutBar = new JPanel();

        layoutBar.setSize(parent.getWidth() / 2, buttonDimensions);

        ImageIcon gridIcon = new ImageIcon("./data/images/iconoir--view-grid.png");
        Image gridImage = gridIcon.getImage();
        Image scaledGridImage = gridImage.getScaledInstance(buttonDimensions, buttonDimensions,
                java.awt.Image.SCALE_SMOOTH);
        gridIcon = new ImageIcon(scaledGridImage);

        ImageIcon listIcon = new ImageIcon("./data/images/iconoir--view-columns-2.png");
        Image listImage = listIcon.getImage();
        Image scaledListImage = listImage.getScaledInstance(buttonDimensions, buttonDimensions,
                java.awt.Image.SCALE_SMOOTH);
        listIcon = new ImageIcon(scaledListImage);

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

    private void addInventoryHandler() {
        inventoryHandler = new JPanel();
        inventoryHandler.setSize(width, height - buttonDimensions);
        inventoryHandler.setLocation(width, height + buttonDimensions);
        inventoryHandler.setLayout(new BorderLayout());

        addSortBar(inventoryHandler);
        addItemPane();

        inventoryHandler.setVisible(true);
        add(inventoryHandler);
    }

    private void addSortBar(JPanel inventoryHandler) {
        JPanel sortBar = new JPanel();

        sortBar.setSize(width, buttonDimensions);
        sortBar.setLocation(width, buttonDimensions);
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

        inventoryHandler.add(sortBar, BorderLayout.NORTH);
    }

    private void addItemPane() {
        itemPane = new JPanel();
        itemPane.setLayout(new BoxLayout(itemPane, BoxLayout.Y_AXIS));

        if (layout.equals("List")) {
            addItemPaneList();
        } else {
            addItemPaneGrid();
        }

        itemPane.setVisible(true);
        inventoryHandler.add(itemPane, BorderLayout.CENTER);
    }

    private void addItemPaneList() {
        for (Item item : inventory.getProcessedInventory()) {
            JPanel nextItem = new JPanel();
            nextItem.setLayout(new FlowLayout());
            nextItem.add(new JLabel(item.getName()));
            nextItem.add(new JLabel(item.getType()));
            nextItem.add(new JLabel(Integer.toString(item.getValue())));
            nextItem.add(new JLabel(Integer.toString(item.getWeight())));
            nextItem.add(new JLabel(Integer.toString(inventory.getCount(item))));
            nextItem.setSize(width / 5, buttonDimensions);
            nextItem.setVisible(true);

            itemPane.add(nextItem);
        }
    }

    private void addItemPaneGrid() {
        // TODO
    }

    /**
     * Sets the position of this remote control UI relative to parent component
     * 
     * @param parent the parent component
     */
    private void setPosition(Component parent) {
        setLocation(width, 0);
    }

    private void resolveSortIcons() {
        ImageIcon sortIcon;
        if (inventory.getSort().getOrder()) {
            sortIcon = new ImageIcon("./data/images/iconoir--nav-arrow-down.png");
        } else {
            sortIcon = new ImageIcon("./data/images/iconoir--nav-arrow-up.png");
        }

        Image sortImage = sortIcon.getImage();
        Image scaledListImage = sortImage.getScaledInstance(buttonDimensions, buttonDimensions,
                java.awt.Image.SCALE_SMOOTH);
        sortIcon = new ImageIcon(scaledListImage);

        changeButtons(sortIcon);
    }

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

    private class LayoutAction extends AbstractAction {
        LayoutAction() {
            super();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            layout = e.getActionCommand();
        }
    }

    private class SortAction extends AbstractAction {
        SortAction() {
            super();
        }

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
            addItemPane();
        }
    }
}
