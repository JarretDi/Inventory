package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Inventory;
import model.Sort;

public class InventoryPanel extends JInternalFrame {
    private static final int buttonDimensions = 15;
    private int WIDTH;
    private int HEIGHT;

    private Inventory inventory;
    private Component parent;
    private String layout;

    private JButton nameButton;
    private JButton typeButton;
    private JButton valueButton;
    private JButton weightButton;

    public InventoryPanel(Inventory inventory, Component parent) {
        super("Inventory", false, false, false, false);
        this.inventory = inventory;
        this.parent = parent;
        layout = "List";

        WIDTH = parent.getWidth() / 2;
        HEIGHT = parent.getHeight();

        setLayout(new BorderLayout());

        setPosition(parent);
        setSize(WIDTH, HEIGHT);

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

        topBar.add(layoutBar,BorderLayout.EAST);
        topBar.setVisible(true);
        
        add(topBar, BorderLayout.NORTH);
    }

    private JPanel createLayoutBar() {
        JPanel layoutBar = new JPanel();

        layoutBar.setSize(parent.getWidth() / 2, buttonDimensions);

        ImageIcon gridIcon = new ImageIcon("./data/images/iconoir--view-grid.png");
        Image gridImage = gridIcon.getImage();
        Image scaledGridImage = gridImage.getScaledInstance(buttonDimensions, buttonDimensions,  java.awt.Image.SCALE_SMOOTH);
        gridIcon = new ImageIcon(scaledGridImage);

        ImageIcon listIcon = new ImageIcon("./data/images/iconoir--view-columns-2.png");
        Image listImage = listIcon.getImage();
        Image scaledListImage = listImage.getScaledInstance(buttonDimensions, buttonDimensions,  java.awt.Image.SCALE_SMOOTH);
        listIcon = new ImageIcon(scaledListImage);

        LayoutAction layoutAction = new LayoutAction();

        JButton gridButton = new JButton(layoutAction);
        gridButton.setIcon(gridIcon);
        gridButton.setActionCommand("Grid");

        JButton listButton = new JButton(layoutAction);
        listButton.setIcon(listIcon);
        gridButton.setActionCommand("List");

        layoutBar.add(gridButton);
        layoutBar.add(listButton);

        layoutBar.setVisible(true);
        return layoutBar;
    }

    private void addInventoryHandler() {
        JPanel inventoryHandler = new JPanel();
        inventoryHandler.setSize(WIDTH, HEIGHT - buttonDimensions);
        inventoryHandler.setLocation(WIDTH, HEIGHT + buttonDimensions);
        inventoryHandler.setLayout(new BorderLayout());

        addSortBar(inventoryHandler);
        addItemPane(inventoryHandler);

        inventoryHandler.setVisible(true);
        add(inventoryHandler);
    }

    private void addSortBar(JPanel inventoryHandler) {
        JPanel sortBar = new JPanel();

        sortBar.setSize(WIDTH, buttonDimensions);
        sortBar.setLocation(WIDTH, buttonDimensions);
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

    private void addItemPane(JPanel inventoryPanel) {
        JPanel itemPane = new JPanel();

        if (layout.equals("List")) {
            addItemPaneList(itemPane);
        } else {
            addItemPaneGrid(itemPane);
        }

        inventoryPanel.add(itemPane);
    }

    private void addItemPaneList(JPanel itemPane) {
        // TODO
    }
    private void addItemPaneGrid(JPanel itemPane) {
        // TODO
    }

    /**
	 * Sets the position of this remote control UI relative to parent component
	 * @param parent   the parent component
	 */
	private void setPosition(Component parent) {
		setLocation(WIDTH, 0);
	}

    private void resolveSortIcons() {
        ImageIcon sortIcon;
        if (inventory.getSort().getOrder()) {
            sortIcon = new ImageIcon("./data/images/iconoir--nav-arrow-down.png");
        } else {
            sortIcon = new ImageIcon("./data/images/iconoir--nav-arrow-up.png");
        }

        Image sortImage = sortIcon.getImage();
        Image scaledListImage = sortImage.getScaledInstance(buttonDimensions, buttonDimensions,  java.awt.Image.SCALE_SMOOTH);
        sortIcon = new ImageIcon(scaledListImage);

        switch (inventory.getSort().getSort()) {
            case "Name":
                nameButton.setIcon(sortIcon);
                typeButton.setIcon(null);
                valueButton.setIcon(null);
                weightButton.setIcon(null);
                break;
            case "Type":
                nameButton.setIcon(null);
                typeButton.setIcon(sortIcon);
                valueButton.setIcon(null);
                weightButton.setIcon(null);
                break;
            case "Value":
                nameButton.setIcon(null);
                typeButton.setIcon(null);
                valueButton.setIcon(sortIcon);
                weightButton.setIcon(null);
                break;
            case "Weight":
                nameButton.setIcon(null);
                typeButton.setIcon(null);
                valueButton.setIcon(null);
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
            String originalSort = inventory.getSort().getSort();
            String sort = e.getActionCommand();
            Boolean order = inventory.getSort().getOrder();

            if (inventory.getSort().isUnsorted()) order = false;
            if (!sort.equals(originalSort)) order = false;

            inventory.sort(new Sort(sort, !order));
            resolveSortIcons();
        }
    }
}

