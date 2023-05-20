package zork;

public class Monster extends Entity{
    private int expReward;
    private String[] actOptions;

    public Monster(int hp, int atk, int def, int expReward, String name) {
        super(hp, atk, def, name);
        this.expReward = expReward;
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

}
