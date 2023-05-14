package zork;

public abstract class Entity {
    private final int MAX_HP;
    private int hp; // health
    private int at; // attack
    private int df; // defence
    private final String name;

    public Entity(int hp, int at, int df, String name) {
        MAX_HP = hp;
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

    public void heal(int hp) {
        this.hp += hp;
        if (this.hp > MAX_HP)
            this.hp = MAX_HP;
    }

    public void takeDamage(int dmg) {
        hp -= dmg;
    }

    public String getName() {
        return name;
    }
}
