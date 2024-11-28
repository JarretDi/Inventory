package model.items;

public class Currency extends Item {
    private String type;

    // Constructs a weapon with given name, type, value, weight, desc
    public Currency(String name, int value, int weight, String desc, Boolean favourite) {
        super(name, value, weight, desc, favourite);
        this.type = "Currency";
    }

    // Constructs a weapon with given name and default (0/empty) parameters
    public Currency(String name) {
        super(name, 0, 0, "", false);
        this.type = "Currency";
    }

    @Override
    public String getType() {
        return this.type;
    }

    public int getTypePriority() {
        return 5;
    }
}
