package model.items;

public class Armour extends Item {
    private String type;

    // Constructs a weapon with given name, type, value, weight, desc
    public Armour(String name, int value, int weight, String desc, Boolean favourite) {
        super(name, value, weight, desc, favourite);
        this.type = "Armour";
    }

    // Constructs a weapon with given name and default (0/empty) parameters
    public Armour(String name) {
        super(name, 0, 0, "", false);
        this.type = "Armour";
    }

    @Override
    public String getType() {
        return this.type;
    }

    public int getTypePriority() {
        return 2;
    }
}
