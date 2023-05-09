package zork;

public class Weapon extends Item{
    private final int at;

    public Weapon(String name, int at) {
        super(name);
        this.at = at;
    }

    public int getAt() {
        return at;
    }

}
