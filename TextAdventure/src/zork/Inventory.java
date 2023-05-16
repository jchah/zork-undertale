package zork;

import java.util.ArrayList;

public class Inventory {
    public ArrayList<Item> items;
    private final int maxCapacity;
    private int currentSize;

    public Inventory(int maxCapacity) {
        this.items = new ArrayList<Item>();
        this.maxCapacity = maxCapacity;
        this.currentSize = 0;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public boolean hasSpace() {
        return getCurrentSize() < getMaxCapacity();
    }

    public boolean addItem(Item item) {
        if (hasSpace()) {
            currentSize++;
            return items.add(item);
        }
        Game.game.printText("There is no room to add the item.");
        return false;
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
            Game.game.printText("item \"" + name + "\" not found");
            return;
        }
        Item item = items.get(index);
        if (item instanceof ToggleableItem)
            if (((ToggleableItem) item).isInUse())
                ((ToggleableItem) item).disuse();
        items.remove(index);
        currentSize--;
    }
}
