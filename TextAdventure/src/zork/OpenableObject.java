package zork;

public class OpenableObject {
  private Boolean isLocked;

  public OpenableObject() {
    this.isLocked = false;
  }

  public OpenableObject(boolean isLocked) {
    this.isLocked = isLocked;
  }

  public boolean isLocked() {
    return isLocked;
  }

  public void setLocked(boolean isLocked) {
    this.isLocked = isLocked;
  }
}
