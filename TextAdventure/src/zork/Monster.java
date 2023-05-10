package zork;

public class Monster extends Entity{

    public Monster(int hp, int at, int df, String name) {
        super(hp, at, df, name);
    }

    public int calcDamage(Player player) {
        return super.getAt() - player.getDf();
    }

    public void takeDamage(int dmg) {
        super.setHp(super.getHp() - dmg);
    }

    public void sayDialogue() {
        System.out.printf("%s awaits your next move", super.getName());
    }

}
