package zork;

public abstract class Entity {
    private int maxHp;
    private int hp; // health
    private int at; // attack
    private int df; // defence
    private final String name;

    public Entity(int hp, int at, int df, String name) {
        maxHp = hp;
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

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void heal(int hp) {
        this.hp += hp;
        if (this.hp > maxHp)
            this.hp = maxHp;
    }

    public void takeDamage(int dmg) {
        hp -= dmg;
    }

    public String getName() {
        return name;
    }
}
