package model.items;

public class Misc extends Item {
    private String type;

    // Constructs a weapon with given name, type, value, weight, desc
    public Misc(String name, int value, int weight, String desc) {
        super(name, value, weight, desc);
        this.type = "Misc";
    }

    // Constructs a weapon with given name and default (0/empty) parameters
    public Misc(String name) {
        super(name, 0, 0, "");
        this.type = "Misc";
    }
    
    @Override
    public String getType() {
        return this.type;
    }

    public int getTypePriority() {
        return 4;
    }
}
