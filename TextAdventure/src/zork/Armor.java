package zork;

public class Armor extends ToggleableItem {
    private final int def;

    public Armor(int def, String name) {
        super(name);
        this.def = def;
    }

    public int getDef() {
        return def;
    }

    @Override
    public void use() {
        disableActiveItem();
        Game.printText("Now wearing the " + getName() + ".");
        Game.game.getPlayer().setDef(def);
        setInUse(true);
    }

    @Override
    public void disuse() {
        Game.printText("No longer wearing the " + getName() + ".");
        Game.game.getPlayer().setDef(0);
        setInUse(false);
    }

    @Override
    public void disableActiveItem() {
        for (Item item: Game.game.getPlayer().inventory.items) {
            if (item instanceof Armor && ((Armor) item).isInUse())
                ((Armor) item).disuse();
        }
    }
}
