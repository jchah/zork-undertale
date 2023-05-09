package zork;

public class Player extends Entity{

    public Player(int hp, int df, int at, String name) {
        super(hp, at, df, name);
    }

    public void equip(Armor armor) {
        super.setDf(armor.getDf());
    }

    public void equip(Weapon weapon) {
        super.setAt(weapon.getAt());
    }

}
