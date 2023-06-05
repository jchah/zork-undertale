package zork;

import java.util.ArrayList;

public class Room {

  private String roomName;
  private String description;
  private ArrayList<Exit> exits;
  private static final String[] validDirections = {"north", "south", "east", "west", "northeast", "northwest", "southeast", "southwest"};
  private boolean isSave;
  private ArrayList<Item> itemArrayList = new ArrayList<>();
  private ArrayList<Integer> costArrayList = new ArrayList<>();
  private ArrayList<String> descArrayList = new ArrayList<>();

  public ArrayList<Exit> getExits() {
    return exits;
  }

  public void setExits(ArrayList<Exit> exits) {
    this.exits = exits;
  }

  /**
   * Create a room described "description". Initially, it has no exits.
   * "description" is something like "a kitchen" or "an open court yard".
   */
  public Room(String description) {
    this.description = description;
    exits = new ArrayList<Exit>();
  }

  public Room() {
    roomName = "DEFAULT ROOM";
    description = "DEFAULT DESCRIPTION";
    exits = new ArrayList<Exit>();
  }

  public static boolean isValidDirection(String direction) {
    for (String d:validDirections) {
      if (direction.equalsIgnoreCase(d)) {
        return true;
      }
    }
    return false;
  }

  public void addExit(Exit exit) {
    exits.add(exit);
  }

  /**
   * Return the description of the room (the one that was defined in the
   * constructor).
   */
  public String shortDescription() {
    return "Room: " + roomName + "\n\n" + description;
  }

  /**
   * Return a long description of this room, on the form: You are in the kitchen.
   * Exits: north west
   */
  public String longDescription() {
    return "Room: " + roomName + "\n\n" + description + "\n" + exitString();
  }

  /**
   * Return a string describing the room's exits, for example "Exits: north west
   * ".
   */
  private String exitString() {
    StringBuilder returnString = new StringBuilder("Exits: ");
    for (Exit exit : exits) {
      returnString.append(exit.getDirection()).append("  ");
    }

    return returnString.toString();
  }

  /**
   * Return the room that is reached if we go from this room in direction
   * "direction". If there is no room in that direction, return null.
   */
  public Room nextRoom(String direction) {
    direction = direction.toLowerCase();
    try {
      for (Exit exit : exits) {
        if (exit.getDirection().equalsIgnoreCase(direction)) {
          String adjacentRoom = exit.getAdjacentRoom();

          return Game.roomMap.get(adjacentRoom);
        }

      }
    } catch (IllegalArgumentException ex) {
      System.out.println(direction + " is not a valid direction.");
      return null;
    }

    for (String validDirection : validDirections) {
      if (validDirection.equals(direction)) {
        Game.printText("You can't go that way. Nothing lies " + direction + ".");
        return null;
      }
    }
    System.out.println(direction + " is not a valid direction.");
    return null;
  }

  public ArrayList<Item> getItemArrayList() {
    return itemArrayList;
  }

  public ArrayList<Integer> getCostArrayList() {
    return costArrayList;
  }

  public ArrayList<String> getDescArrayList() {
    return descArrayList;
  }

  public void addToItemList(Item item) {
    itemArrayList.add(item);
  }

  public void addToCostList(int cost) {
    costArrayList.add(cost);
  }

  public void addToDescList(String description) {
    descArrayList.add(description);
  }

  public String getRoomName() {
    return roomName;
  }

  public void setRoomName(String roomName) {
    this.roomName = roomName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isSave() {
    return isSave;
  }

  public void setSave(boolean save) {
    isSave = save;
  }
}
