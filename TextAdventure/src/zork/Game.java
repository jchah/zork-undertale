package zork;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Game {

    public static HashMap<String, Room> roomMap = new HashMap<String, Room>();

    public static Game game = new Game();
    private Parser parser;
    private Room currentRoom;
    private Player player;
    private Scanner in = new Scanner(System.in);
    private boolean testMode = true;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() {
        try {
            initRooms("TextAdventure\\src\\zork\\data\\rooms.json");
            currentRoom = roomMap.get("Bedroom");
        } catch (Exception e) {
            e.printStackTrace();
        }

        parser = new Parser();

        if (!testMode) {
            printIntro();
            player = new Player(20, 0, 0, namePrompt());
        }
        else {
            System.out.println("GAME IN TEST MODE");
            player = new Player(20, 0, 0, "Frisk");
        }
    }

    public Player getPlayer() {
        return player;
    }

    public String namePrompt() {
        String name;
        String temp;
        while (true) {
            System.out.println("Name the fallen human: ");
            name = in.nextLine();
            if (name.length() >= 1 && name.length() <= 15) {
                boolean good = false;
                while(!good) {
                    System.out.println("Your name is " + name + ", confirm?");
                    temp = in.nextLine();
                    if (temp.equalsIgnoreCase("y") || temp.equalsIgnoreCase("yes")) {
                        return name;
                    } else if (!temp.equalsIgnoreCase("n") && !temp.equalsIgnoreCase("no")) {
                        System.err.println("Invalid response. Please answer (y)es or (n)o");
                    } else
                        good = true;
                }
            }
            else {
                System.err.println("Name must be within 1 and 15 characters");
            }
        }
    }


    private void initRooms(String fileName) throws Exception {
        Path path = Path.of(fileName);
        String jsonString = Files.readString(path);
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(jsonString);
        JSONArray jsonRooms = (JSONArray) json.get("rooms");
        for (Object roomObj : jsonRooms) {
            Room room = new Room();
            String roomName = (String) ((JSONObject) roomObj).get("name");
            String roomId = (String) ((JSONObject) roomObj).get("id");
            String roomDescription = (String) ((JSONObject) roomObj).get("description");
            room.setDescription(roomDescription);
            room.setRoomName(roomName);

            JSONArray jsonExits = (JSONArray) ((JSONObject) roomObj).get("exits");
            ArrayList<Exit> exits = new ArrayList<Exit>();
            for (Object exitObj : jsonExits) {
                String direction = (String) ((JSONObject) exitObj).get("direction");
                String adjacentRoom = (String) ((JSONObject) exitObj).get("adjacentRoom");
                String keyId = (String) ((JSONObject) exitObj).get("keyId");
                Boolean isLocked = (Boolean) ((JSONObject) exitObj).get("isLocked");
                Boolean isOpen = (Boolean) ((JSONObject) exitObj).get("isOpen");
                Exit exit = new Exit(direction, adjacentRoom, isLocked, keyId, isOpen);
                exits.add(exit);
            }
            room.setExits(exits);
            roomMap.put(roomId, room);
        }
    }

    /**
     * Main play routine. Loops until end of play.
     */
    public void play() {
        boolean finished = false;
        while (!finished) {
            Command command;
            try {
                command = parser.getCommand();
                finished = processCommand(command);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printIntro() {
        // play Once Upon a Time
        printDialouge("Long ago, two races ruled over Earth: HUMANS and MONSTERS.\nOne day, war broke out between the two races.\nAfter a long battle, the humans were victorious.\nThey sealed the monsters underground with a magic spell.\nMany years later...\nMT.Ebott.\n201X\nLegends say that those who climb the mountain never return.\n");
    }

    private void printAsciiImage(String name) {
        try {
            File ascii = new File("TextAdventure\\src\\zork\\data\\" + name.toLowerCase() + ".txt");
            Scanner reader = new Scanner(ascii);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                System.out.println(data);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void printDialouge(String str) {
        String[] chars = str.split("");
        for (String aChar : chars) {
            System.out.print(aChar);
            sleep(50);
        }
        System.out.println();
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void encounter(Monster monster) {
        String option;
        boolean keepFighting = false;
        boolean canMercy = false;
        printAsciiImage(monster.getName());
        printDialouge("A wild " + monster.getName() + " appeared!");
        while (player.getHp() > 0 && !keepFighting) {
            option = serveEncounterOptions();
            switch (option) {
                case "fight": monster.takeDamage(serveAttackSlider());
                case "act": canMercy = serveActOptions(monster);
                case "item": serveItemOptions();
                case "mercy": keepFighting = canMercy;
            }
        }
    }

    /**
     * Prints the inventory and prompts the player for the item to use.
     */
    private void serveItemOptions() {
        Inventory inventory = player.inventory;
        inventory.showInventory();
        while (true) {
            System.out.print("> ");
            String chosenItem = in.nextLine();
            int index = inventory.findItemByName(chosenItem);
            if (index > -1) {
                Item item = inventory.items.get(index);
                item.use();
                return;
            } else {
                System.out.printf("No such item \"%s\"", chosenItem);
            }
        }
    }

    /**
     * Prints the act options and prompts the player for their choice.
     * Returns whether the player chose the correct option to mercy.
     */
    private boolean serveActOptions(Monster monster) {
        return false;
    }

    /**
     * Plays an attack slider mini-game. The closer to the target, the more damage done.
     * Returns the amount of damage done to the monster.
     */
    private int serveAttackSlider() {
        return 0;
    }

    private String serveEncounterOptions() {
        String option;
        System.out.println("FIGHT   ACT   ITEM   MERCY");
        while (true) {
            System.out.print("> ");
            option = in.nextLine().toLowerCase();
            if (!(option.equals("fight") || option.equals("act") || option.equals("item") || option.equals("mercy"))) {
                System.err.println("Not a valid option: choose FIGHT, ACT, ITEM, or MERCY");
            }
            else
                return option;
        }
    }

    private boolean processCommand(Command command) {
        if (command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help"))
            printHelp();
        else if (commandWord.equals("go"))
            goRoom(command);
        else if (commandWord.equals("quit")) {
            if (command.hasSecondWord())
                System.out.println("Quit what?");
            else
                return true; // signal that we want to quit
        } else if (commandWord.equals("eat")) {
            System.out.println("Do you really think you should be eating at a time like this?");
        }
        return false;
    }

    /**
     * Print out some help information. Here we print a list of the command words.
     */
    private void printHelp() {
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /**
     * Try to go to one direction. If there is an exit, enter the new room,
     * otherwise print an error message.
     */
    private void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.nextRoom(direction);

        if (nextRoom == null)
            System.out.println("There is no door!");
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.longDescription());
        }
    }
}
