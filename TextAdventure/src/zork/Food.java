package zork;

public class Food extends Item{
    private final int hp;

    public Food(int hp, String name) {
        super(name);
        this.hp = hp;
    }

    public int getHeal() {
        return hp;
    }

}