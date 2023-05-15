package zork;

public class Food extends Item {
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
        Player player = Game.game.getPlayer();
        int beforeHealth = player.getHp();
        player.heal(hp);

        Inventory inventory = player.inventory;
        int index = inventory.findItemByName(getName());
        inventory.removeItemByIndex(index);
        System.out.printf("You ate the %s and recovered %d hp\n", getName(), player.getHp() - beforeHealth);
    }
}