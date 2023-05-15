package zork;

public class Player extends Entity{
    public Inventory inventory;
    private int exp;
    private int lv;

    public Player(int hp, int df, int at, String name) {
        super(hp, at, df, name);
        inventory = new Inventory(8);
    }

    public void addExp(int exp) {
        this.exp += exp;
    }

    public void updateLv() {
        while (exp >= Math.pow(lv, 2) + 30) {
            exp -= Math.pow(lv, 2) + 30;
            lv++;
        }
    }
}
