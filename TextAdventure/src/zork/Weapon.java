package zork;

public class Weapon extends Item{
    private final int at;

    public Weapon(String name, int at) {
        super(name);
        this.at = at;
    }

    public int getAtValue() {
        return at;
    }

    @Override
    public void use() {
        Game.game.getPlayer().setAt(at);
        super.setInUse(true);
    }

    public void disuse() {
        Game.game.getPlayer().setAt(0);
        super.setInUse(false);
    }
}
