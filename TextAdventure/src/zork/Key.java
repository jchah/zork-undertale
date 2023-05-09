package zork;

public class Key extends Item {
  private String keyId;

  public Key(String keyId, String keyName) {
    super(keyName);
    this.keyId = keyId;
  }

  public String getKeyId() {
    return keyId;
  }
}
