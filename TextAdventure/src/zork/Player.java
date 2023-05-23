package zork;

public class Player extends Entity{
    public Inventory inventory;
    private int exp;
    private int lv = 1;
    private final int[] expToLv = {0, 10, 20, 40, 50, 80, 100, 200, 300, 400, 500, 800, 1000, 1500, 2000, 3000, 5000, 10000, 25000, 49999};

    public Player(int hp, int df, int at, String name) {
        super(hp, at, df, name);
        inventory = new Inventory(8);
    }

    public void addExp(int exp) {
        this.exp += exp;
        updateLv();
    }

    public int getExp() {
        return exp;
    }

    public int getLv() {
        return lv;
    }

    public void updateLv() {
        while (exp >= expToLv[lv]) {
            exp -= expToLv[lv];
            lv++;
            Game.game.printText("Your LOVE increased.");
            setMaxHp(getMaxHp() + 4);
        }

    }

    /**
     * @return damage done to monster
     */
    public int calcDamage(Monster monster, int b) {
        return -Math.round(((getAtk() - monster.getDef() + (int) (Math.random()*2))) * b);
    }
}