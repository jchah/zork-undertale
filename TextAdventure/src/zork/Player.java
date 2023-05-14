package zork;

public class Player extends Entity{
    private final int MAX_HP;
    private int exp;
    private int lv;

    public Player(int hp, int df, int at, String name) {
        super(hp, at, df, name);
        MAX_HP = hp;
    }

    public void use(Armor armor) {
        super.setDf(armor.getDf());
    }

    public void use(Weapon weapon) {
        super.setAt(weapon.getAt());
    }

    public void use(Food food) {
        super.setHp(super.getHp() + food.getHeal());
    }

    public void addExp(int exp) {
        this.exp += exp;
    }

    public void updateLv() {
        while (exp >= Math.pow(lv, 2)) {
            exp -= Math.pow(lv, 2);
            lv++;
        }
    }
}
