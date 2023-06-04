package zork;

public class Monster extends Entity{
    private final int expReward;
    private final int goldMultiplier;

    public Monster(int hp, int atk, int def, int exp, int gold, String name) {
        super(hp, atk, def, name);
        expReward = exp;
        goldMultiplier = gold;
    }

    public String check() {
        return getName() + "- " + "ATK: " + getAtk() + ", DEF: " + getDef();
    }

    public int getExpReward() {
        return expReward;
    }

    public int calcGoldReward() {
        int r = (int) (1 + Math.random() * 3);
        return goldMultiplier * r;
    }
    public void resetHp() {
        setHp(getMaxHp());
    }
}
