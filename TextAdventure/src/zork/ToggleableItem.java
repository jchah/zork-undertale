package zork;

public abstract class ToggleableItem extends Item {
    private boolean inUse = false;

    public ToggleableItem(String name) {
        super(name);
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public abstract void disuse();

    public abstract void disableActiveItem();
}
