package zork;

public class Armor extends Item{
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
        Game.game.getPlayer().setDf(df);
        super.setInUse(true);
    }

    public void disuse() {
        Game.game.getPlayer().setDf(0);
        super.setInUse(false);
    }
}
