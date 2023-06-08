package zork;

public class CommandWords {
  // a constant array that holds all valid command words
  private static final String[] validCommands = { "go", "quit", "help", "use", "eat", "drop", "inventory", "stats", "search", "exits", "north", "south", "east", "west", "northeast", "northwest", "southeast", "southwest"};

  /**
   * Constructor - initialise the command words.
   */
  public CommandWords() {
    // nothing to do at the moment...
  }

  /**
   * Check whether a given String is a valid command word. Return true if it is,
   * false if it isn't.
   **/
  public boolean isCommand(String aString) {
    for (String c : validCommands) {
      if (c.equals(aString))
        return true;
    }
    // if we get here, the string was not found in the commands
    return false;
  }

  /*
   * Print all valid commands to System.out.
   */
  public void showAll() {
    for (int i = 0; i < validCommands.length; i++) {
      System.out.print(validCommands[i]);
      if (i != validCommands.length - 1)
        System.out.print(",  ");
    }
    System.out.println();
  }
}
