package zork;

public class Armor extends Item{
    private final int df;

    public Armor(int df, String name) {
        super(name);
        this.df = df;
    }

    public int getDf() {
        return df;
    }

}
