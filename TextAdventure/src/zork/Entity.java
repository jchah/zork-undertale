package zork;

public abstract class Entity {
    private int maxHp;
    private int hp; // health
    private int atk; // attack
    private int def; // defence
    private final String name;

    public Entity(int hp, int atk, int def, String name) {
        maxHp = hp;
        this.hp = hp;
        this.atk = atk;
        this.def = def;
        this.name = name;
    }

    public int getHp() {
        return hp;
    }


    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
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
