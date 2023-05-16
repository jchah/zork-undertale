package zork;

public class Monster extends Entity{
    private int expReward;

    public Monster(int hp, int at, int df, int expReward, String name) {
        super(hp, at, df, name);
        this.expReward = expReward;
    }

    /**
     * @return damage done to player
     */

    public int calcDamage(Player player) {
        return getAt() - player.getDf();
    }

    public void takeDamage(int dmg) {
        super.setHp(getHp() - dmg);
    }

    public void sayDialogue() {
        Game.game.printText(getName() + " awaits your next move");
    }

}
