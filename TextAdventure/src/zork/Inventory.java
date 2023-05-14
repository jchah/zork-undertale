package zork;

import java.util.ArrayList;

public class Inventory {
    private ArrayList<Item> items;
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
        System.out.println("There is no room to add the item.");
        return false;
    }

    public ArrayList<Item> getInventory() {
        return items;
    }

    public void showInventory() {
        for (int i = 0; i < items.size(); i++)
            System.out.println(i + 1 + ": " + items.get(i).getName());
    }

    public int findItemByName(String name) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equalsIgnoreCase(name))
                return i;
        }
        return -1;
    }
}
