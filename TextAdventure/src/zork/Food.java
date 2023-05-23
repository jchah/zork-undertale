package zork;

public class Food extends Item {
    private final int hp;

    public Food(int hp, String name) {
        super(name);
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }

    @Override
    public void use() {
        Player player = Game.game.getPlayer();
        int beforeHealth = player.getHp();
        player.heal(hp);

        Inventory inventory = player.inventory;
        int index = inventory.findItemByName(getName());
        inventory.removeItemByIndex(index);
        Game.printText("You ate the " + getName() + " and recovered " + (player.getHp() - beforeHealth) + "hp");
    }
}