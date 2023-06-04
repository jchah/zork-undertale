package zork;

public class Player extends Entity{
    public Inventory inventory;
    private int exp;
    private double attackBoost;
    private int lv = 1;
    // todo adjust these numbers
    private final int[] expToLv = {0, 10, 20, 40, 50, 80, 100, 200, 300, 400, 500, 800, 1000, 1500, 2000, 3000, 5000, 10000, 25000, 49999};

    public Player(int hp, int def, int atk, String name) {
        super(hp, atk, def, name);
        inventory = new Inventory(8);
        attackBoost = 1.0;
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
            Game.printText("Your LOVE increased.");
            setMaxHp(getMaxHp() + 4);
            attackBoost += 0.2;
        }

    }

    /**
     * @return damage done to monster
     */
    public int calcDamage(Monster monster, int b) {
        int r = (int) Math.max(1, Math.random()*3);
        return (int) Math.round(((getAtk() - monster.getDef() + r * b)) * attackBoost);
    }
}