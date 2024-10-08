package model;

// Represents a sort with a sorting type and an order,
// With a special case of null if there is no sort (inventory unsorted)

public class Sort {
    String sort;
    Boolean order;

    // EFFECT: constructs a sort with given sort and order
    public Sort(String sort, Boolean order){
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

    public String getSort() {
        return this.sort;
    }

    public Boolean getOrder() {
        return this.order;
    }

    
}
