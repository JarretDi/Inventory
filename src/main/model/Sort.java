package model;

import model.exceptions.InvalidSortException;

// Represents a sort with a sorting type and an order,
// With a special case of null if there is no sort (inventory unsorted)
// Order represents the order of sort
// true is descending sort, i.e. A->Z, Weapon->Armor->Consumable->Misc->Currency, 100->0

public class Sort {
    SortType sort;
    Boolean order;

    public enum SortType {
        Name, Type, Value, Weight
    }

    // REQUIRES: sort is one of "Name", "Type", "Value", "Weight"
    // EFFECT: constructs a sort with given sort and order
    public Sort(SortType sort, Boolean order) {
        this.sort = sort;
        this.order = order;
    }

    // EFFECT: constructs an Unsort (empty sort)
    public Sort() {
        this.sort = null;
        this.order = null;
    }

    // MODIFIES: this
    // EFFECT: turns sort into the Unsort (empty sort)
    public void setUnsorted() {
        this.sort = null;
        this.order = null;
    }

    public Boolean isUnsorted() {
        return this.equals(new Sort());
    }

    public SortType getSort() {
        return this.sort;
    }

    public Boolean getOrder() {
        return this.order;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((sort == null) ? 0 : sort.hashCode());
        result = prime * result + ((order == null) ? 0 : order.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Sort other = (Sort) obj;
        if (sort != other.sort)
            return false;
        if (order == null) {
            if (other.order != null)
                return false;
        } else if (!order.equals(other.order))
            return false;
        return true;
    }

    
}
