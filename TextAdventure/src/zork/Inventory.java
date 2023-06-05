package zork;

import java.util.ArrayList;

public class Inventory {
    public ArrayList<Item> items;
    private final int maxCapacity;
    private int currentSize;
    private int gold;

    public Inventory(int maxCapacity) {
        this.items = new ArrayList<>();
        this.maxCapacity = maxCapacity;
        this.currentSize = 0;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public int getGold() {
        return gold;
    }

    public void addGold(int gold) {
        this.gold += gold;
    }

    public boolean spendGold(int gold) {
        if(Game.game.getPlayer().inventory.gold<gold) {
            Game.printText("You need at least " + gold + " gold");
            return false;
        }
        else {
            this.gold -= gold;
            return true;
        }
    }

    public boolean hasSpace() {
        return getCurrentSize() < getMaxCapacity();
    }

    public void addItem(Item item) {
        if (hasSpace()) {
            items.add(item);
            currentSize++;
            Game.printText("You got the " + item.getName() + ".");
        }
        else
            Game.printText("There is no room to add the item.");
    }

    public void showInventory() {
        String item;
        for (int i = 0; i < maxCapacity; i++) {
            if (i + 1 > getCurrentSize())
                item = "[empty slot]";
            else
                item = items.get(i).getName();
            System.out.println(i + 1 + ": " + item);
        }
    }

    public int findItemByName(String name) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equalsIgnoreCase(name))
                return i;
        }
        return -1;
    }

    public void removeItemByIndex(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            currentSize--;
        }
    }

    public void dropItem(String name) {
        int index = findItemByName(name);
        if (index == -1) {
            Game.printText("item \"" + name + "\" not found");
            return;
        }
        Item item = items.get(index);
        if (item instanceof ToggleableItem)
            if (((ToggleableItem) item).isInUse())
                ((ToggleableItem) item).disuse();
        items.remove(index);
        currentSize--;
        Game.printText(item.getName() + " dropped.");
    }
}
