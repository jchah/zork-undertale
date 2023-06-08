package zork;

public class Player extends Entity{
    public Inventory inventory;
    private int exp;
    private double attackBoost;
    private int lv = 1;
    private final int[] expToLv = {0, 5, 10, 25, 40, 60, 90, 120, 160, 200, 250, 310, 400, 500, 650, 800};

    public Player(int hp, int def, int atk, String name) {
        super(hp, atk, def, name);
        inventory = new Inventory(8);
        attackBoost = 1.0;
    }

    public String check() {
        return getName() + "- " + "ATK: " + getAtk() + ", DEF: " + getDef() + ", HP: " + getHp() + ",\nGOLD: " + inventory.getGold() + ", EXP: " +getExp() + ", LV: " + getLv();
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
            Game.printText("Your LV increased to " + lv + ".");
            setMaxHp(getMaxHp() + 4);
            resetHp();
            Game.printText("Health restored.");
            attackBoost += 0.2;
            Game.printText("+20% attack boost.");
        }

    }

    /**
     * @return damage done to monster
     */
    public int calcDamage(Monster monster, int b) {
        int r = (int) Math.max(1, Math.random()*5);
        return (int) Math.round(((getAtk() - monster.getDef() + r * b)) * attackBoost);
    }
}