package zork;

public class Monster extends Entity{
    private int expReward;
    private int goldMultiplier;
    // Jad

    public Monster(int hp, int atk, int def, int exp, int gold, String name) {
        super(hp, atk, def, name);
        expReward = exp;
        goldMultiplier = gold;
    }

    /**
     * @return damage done to player
     */
    public int calcDamage(Player player) {
        return getAtk() - player.getDef();
    }

    public void takeDamage(int dmg) {
        setHp(getHp() - dmg);
    }

    public String check() {
        return getName() + "- " + "ATK " + getAtk() + " DEF " + getDef();
    }

    public int getExpReward() {
        return expReward;
    }

    public int calcGoldReward() {
        int r = (int) (1 + Math.random() * 3);
        return goldMultiplier * r;
    }
}
