package zork;

public abstract class Entity {
    private int hp; // health points
    private int at; // attack
    private int df; // defence
    private final String name;

    public Entity(int hp, int at, int df, String name) {
        this.hp = hp;
        this.at = at;
        this.df = df;
        this.name = name;
    }

    public int getHp() {
        return hp;
    }


    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAt() {
        return at;
    }

    public void setAt(int at) {
        this.at = at;
    }

    public int getDf() {
        return df;
    }

    public void setDf(int df) {
        this.df = df;
    }

    public void takeDamage(int dmg) {
        hp -= dmg;
    }

    public String getName() {
        return name;
    }

}
