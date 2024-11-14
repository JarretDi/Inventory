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

public class InventoryPanel extends JInternalFrame {
    private Inventory inventory;
    private Component parent;
    private String layout;

    public InventoryPanel(Inventory inventory, Component parent) {
        super("Inventory", false, false, false, false);
        this.inventory = inventory;
        this.parent = parent;
        layout = "List";

        setLayout(new BorderLayout());

        setPosition(parent);
        setSize(parent.getWidth() / 2, parent.getHeight());

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

        topBar.add(new JLabel(inventory.getCharacter() + "'s Inventory:"), BorderLayout.WEST);

        JPanel layoutBar = createLayoutBar();

        topBar.add(layoutBar,BorderLayout.EAST);
        topBar.setVisible(true);
        
        add(topBar, BorderLayout.NORTH);
    }

    private JPanel createLayoutBar() {
        JPanel layoutBar = new JPanel();
        int buttonDimensions = 25;

        ImageIcon gridIcon = new ImageIcon("./data/images/iconoir--view-grid.png");
        Image gridImage = gridIcon.getImage();
        Image scaledGridImage = gridImage.getScaledInstance(buttonDimensions, buttonDimensions,  java.awt.Image.SCALE_SMOOTH);
        gridIcon = new ImageIcon(scaledGridImage);

        ImageIcon listIcon = new ImageIcon("./data/images/iconoir--view-columns-2.png");
        Image listImage = listIcon.getImage();
        Image scaledListImage = listImage.getScaledInstance(buttonDimensions, buttonDimensions,  java.awt.Image.SCALE_SMOOTH);
        listIcon = new ImageIcon(scaledListImage);

        JButton gridButton = new JButton(new GridLayoutAction());
        gridButton.setIcon(gridIcon);

        JButton listButton = new JButton(new ListLayoutAction());
        listButton.setIcon(listIcon);

        layoutBar.add(gridButton);
        layoutBar.add(listButton);

        layoutBar.setVisible(true);
        return layoutBar;
    }

    private void addInventoryHandler() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addInventoryHandler'");
    }

    /**
	 * Sets the position of this remote control UI relative to parent component
	 * @param parent   the parent component
	 */
	private void setPosition(Component parent) {
		setLocation(parent.getWidth() / 2, 0);
	}

    private class GridLayoutAction extends AbstractAction {
        GridLayoutAction() {
            super();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            layout = "Grid";
        }
    }

    private class ListLayoutAction extends AbstractAction {
        ListLayoutAction() {
            super();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            layout = "List";
        }
    }
}

