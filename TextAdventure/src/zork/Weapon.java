package zork;

public class Weapon extends ToggleableItem {
    private final int atk;

    public Weapon(int atk, String name) {
        super(name);
        this.atk = atk;
    }

    public int getAtk() {
        return atk;
    }

    @Override
    public void use() {
        disableActiveItem();
        Game.printText("Now wielding the " + getName());
        Game.game.getPlayer().setAtk(atk);
        setInUse(true);
    }

    @Override
    public void disuse() {
        Game.printText("No longer wielding the " + getName());
        Game.game.getPlayer().setAtk(0);
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
