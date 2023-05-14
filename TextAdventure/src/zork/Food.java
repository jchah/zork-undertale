package zork;

public class Food extends Item{
    private final int hp;

    public Food(int hp, String name) {
        super(name);
        this.hp = hp;
    }

    public int getHpValue() {
        return hp;
    }

    @Override
    public void use() {
        Game.game.getPlayer().heal(hp);
        int index = Game.game.getPlayer().inventory.findItemByName(super.getName());
        Game.game.getPlayer().inventory.items.remove(index);
        Game.game.getPlayer().inventory.decrementCurrentSize();
    }
}