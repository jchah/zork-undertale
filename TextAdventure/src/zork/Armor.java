package zork;

public class Armor extends ToggleableItem {
    private final int df;

    public Armor(int df, String name) {
        super(name);
        this.df = df;
    }

    public int getDfValue() {
        return df;
    }

    @Override
    public void use() {
        disableActiveItem();
        Game.game.printText("Now wearing the " + getName() );
        Game.game.getPlayer().setDf(df);
        setInUse(true);
    }

    @Override
    public void disuse() {
        Game.game.printText("No longer wearing the " + getName() );
        Game.game.getPlayer().setDf(0);
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
