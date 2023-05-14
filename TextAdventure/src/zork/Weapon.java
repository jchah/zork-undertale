package zork;

public class Weapon extends ToggleableItem {
    private final int at;

    public Weapon(int at, String name) {
        super(name);
        this.at = at;
    }

    public int getAtValue() {
        return at;
    }

    @Override
    public void use() {
        disableActiveItem();
        System.out.println("Now wielding the " + getName());
        Game.game.getPlayer().setAt(at);
        setInUse(true);
    }

    @Override
    public void disuse() {
        System.out.println("No longer wielding the " + getName());
        Game.game.getPlayer().setAt(0);
        setInUse(false);
    }

    @Override
    public void disableActiveItem() {
        for (Item item: Game.game.getPlayer().inventory.items) {
            if (item instanceof Weapon && ((Weapon) item).isInUse())
                ((Weapon) item).disuse();
        }
    }
}
