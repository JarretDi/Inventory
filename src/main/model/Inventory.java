package model;

import java.util.ArrayList;

// Represents the inventory of a character, consisting of an
// arbitrary amount of items as well as the count of each identical element
public class Inventory {
    private ArrayList<Item> inventory;
    private Sort sort;

    // EFFECT: creates an unsorted Inventory with no items inside of it
    public Inventory() {
        this.inventory = new ArrayList<>();
        this.sort = new Sort();
    }

    // MODIFIES: this, Sort
    // EFFECT: adds given item to this inventory
    // additionally, changes the inventory's type to unsorted
    public void addItem(Item item) {
        this.inventory.add(item);
        this.sort.setUnsorted();
    }

    // MODIFIES: this
    // EFFECT: removes first instance of given item from the inventory
    public void removeItem(Item item) {
        this.inventory.remove(item);
    }

    // MODIFIES: this
    // EFFECT: removes ALL instances of given item from the inventory
    public void removeAllItem(Item item) {
        while (inventory.contains(item)) {
            inventory.remove(item);
        }
    }

    // MODIFIES: this
    // EFFECT: removes all items from inventory
    public void clearInventory() {
        inventory.clear();
    }

    // REQUIRES: sort is not the Unsort (empty sort)
    // MODIFIES: this, Sort
    // EFFECT: sorts an inventory according to sort, and order is whether
    // ascending(true) or descending(false)
    // additionally changes the sort type to given sort
    public void sort(Sort sort) {
        Boolean order = sort.getOrder();
        switch (sort.getSort()) {
            case "Name":
                if (order) {
                    inventory.sort((item1, item2) -> {
                        return (item1.getName().compareTo(item2.getName()));
                    });
                } else {
                    inventory.sort((item1, item2) -> {
                        return (item2.getName().compareTo(item1.getName()));
                    });
                }
                break;

            case "Type":
                if (order) {
                    inventory.sort((item1, item2) -> {
                        return (item1.getTypePriority() - (item2.getTypePriority()));
                    });
                } else {
                    inventory.sort((item1, item2) -> {
                        return (item2.getTypePriority() - (item1.getTypePriority()));
                    });
                }
                break;

            case "Value":
                if (order) {
                    inventory.sort((item1, item2) -> {
                        return (item1.getValue() - (item2.getValue()));
                    });
                } else {
                    inventory.sort((item1, item2) -> {
                        return (item2.getValue() - (item1.getValue()));
                    });
                }
                break;

            case "Weight":
                if (order) {
                    inventory.sort((item1, item2) -> {
                        return (item1.getWeight() - (item2.getWeight()));
                    });
                } else {
                    inventory.sort((item1, item2) -> {
                        return (item2.getWeight() - (item1.getWeight()));
                    });
                }
        }
        this.sort = sort;
    }

    // REQUIRES: Inventory is already sorted
    // MODIFIES: this
    // EFFECT: adds given item to this inventory in its correct position
    // according to sort. If two are identical, inserts at the next point
    // where they aren't
    public void addItemSorted(Item item) {
        if (this.sort.getSort() == "Name") {
            addItemSortedName(item);
        } else {
            addItemSortedInt(item);
        }
    }

    private void addItemSortedName(Item item) {
        int len = inventory.size();
        Boolean order = this.sort.getOrder();

        String itemName = item.getName();

        for (int i = 0; i < len; i++) {
            String currentItemName = inventory.get(i).getName();

            if (itemName.compareTo(currentItemName) < 0 && order) {
                inventory.add(i, item);
                break;
            } else if (itemName.compareTo(currentItemName) > 0 && !order) {
                inventory.add(i, item);
                break;
            } else if (i + 1 == len) {
                inventory.add(item);
                break;
            } else if (itemName.compareTo(currentItemName) == 0) {
                continue;
            }
        }
    }

    private void addItemSortedInt(Item item) {
        int len = inventory.size();
        Boolean order = this.sort.getOrder();
        int itemPriority = 0;
        int currentItemPriority = 0;

        for (int i = 0; i < len; i++) {
            switch (sort.getSort()) {
                case "Type":
                    itemPriority = item.getTypePriority();
                    currentItemPriority = inventory.get(i).getTypePriority();
                    break;
                case "Value":
                    itemPriority = item.getValue();
                    currentItemPriority = inventory.get(i).getValue();
                    break;
                case "Weight":
                    itemPriority = item.getWeight();
                    currentItemPriority = inventory.get(i).getWeight();
                    break;
            }
            if (itemPriority - currentItemPriority < 0 && order) {
                inventory.add(i, item);
                break;
            } else if (itemPriority - currentItemPriority > 0 && !order) {
                inventory.add(i, item);
                break;
            } else if (i + 1 == len) {
                inventory.add(item);
                break;
            } else if (itemPriority - currentItemPriority == 0) {
                continue;
            }
        }
    }

    public Sort getSort() {
        return this.sort;
    }

    public Item getItem(int index) {
        return inventory.get(index);
    }

    // EFFECT: returns number of times given item appears in Inventory
    public int getItemCount(Item item) {
        int count = 0;
        for (Item i : inventory) {
            if (i.equals(item)) {
                count += 1;
            }
        }
        return count;
    }

    public int getNumItems() {
        int count = 0;
        for (Item i : inventory) {
            count += 1;
        }
        return count;
    }
}
