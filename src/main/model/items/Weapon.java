package model.items;

public class Weapon extends Item {
    private String type;

    // Constructs a weapon with given name, type, value, weight, desc
    public Weapon(String name, int value, int weight, String desc, Boolean favourite) {
        super(name, value, weight, desc, favourite);
        this.type = "Weapon";
    }

    // Constructs a weapon with given name and default (0/empty) parameters
    public Weapon(String name) {
        super(name, 0, 0, "", false);
        this.type = "Weapon";
    }

    @Override
    public String getType() {
        return this.type;
    }

    public int getTypePriority() {
        return 1;
    }
}
