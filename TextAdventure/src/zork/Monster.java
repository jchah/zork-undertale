package zork;

public abstract class Monster extends Entity{

    public Monster(int hp, int at, int df, String name) {
        super(hp, at, df, name);
    }

    public int calcDamage(Player player) {
        return super.getAtk() - player.getDef();
    }

    public void takeDamage(int dmg) {
        super.setHp(super.getHp() - dmg);
    }

    public void printDialogue() {
        System.out.printf("%s awaits your next move", super.getName());
    }

}