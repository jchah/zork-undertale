package zork;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
    private final Parser parser;
    private Room currentRoom;
    private final Player player;
    private static final Scanner in = new Scanner(System.in);
    private static final AttackMeterGame attackMeterGame= new AttackMeterGame();
    private static final Charset defaultCharset = Charset.defaultCharset();
    private PrintStream out = null;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() {
        Charset utf8Charset = StandardCharsets.UTF_8;
        // charset is windows-1252

        try {
            out = new PrintStream(System.out, true, utf8Charset.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            initRooms("TextAdventure\\src\\zork\\data\\rooms.json");
            currentRoom = roomMap.get("Bedroom");
        } catch (Exception e) {
            e.printStackTrace();
        }

        parser = new Parser();

        boolean testMode = true;
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

    public static String namePrompt() {
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
                        System.out.println("Invalid response. Please answer (y)es or (n)o");
                    } else
                        good = true;
                }
            }
            else {
                System.out.println("Name must be within 1 and 15 characters");
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
        Monster froggit = new Monster(30, 5, 2, 4, 2, "froggit");
        encounter(froggit);
        player.inventory.addItem(new Food(5, "Candy"));

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
    private static void printIntro() {
        // play Once Upon a Time
        printText("Long ago, two races ruled over Earth: HUMANS and MONSTERS.\nOne day, war broke out between the two races.\nAfter a long battle, the humans were victorious.\nThey sealed the monsters underground with a magic spell.\nMany years later...\nMT.Ebott.\n201X\nLegends say that those who climb the mountain never return.\n");
    }

    private static void printAsciiImage(String name) {
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

    public static void printText(String str) {
        String[] chars = str.split("");
        for (String aChar : chars) {
            System.out.print(aChar);
            sleep(50);
        }
        System.out.println();
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String encodeToString(String string) {
        String data = "";
        byte[] sourceBytes = string.getBytes(StandardCharsets.UTF_8);
        try {
            data = new String(sourceBytes , defaultCharset.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void showHealthBar(Entity entity) {
        String data = encodeToString("❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤");
        String[] bar = data.split("");
        int startIndex = bar.length - 1;
        double percent = (double) entity.getHp() / entity.getMaxHp();
        int offset = (int) (bar.length * (1 - percent));
        for (int i = 0; i < offset; i++) {
            bar[startIndex--] = "\uD83D\uDC94";
        }
        for (String marker: bar) {
            out.print(marker);
        }
        System.out.println();
    }

    private void encounter(Monster monster) {
        String option;
        boolean keepFighting = true;
        boolean canMercy = false;
        String monsterName = monster.getName();
        printAsciiImage(monsterName);
        printText("A wild " + monsterName + " appeared!");
        while (keepFighting) {
            player.takeDamage(5);
            System.out.print(player.getName() + " health: ");
            showHealthBar(player);
            System.out.print(monsterName + " health: ");
            showHealthBar(monster);

            if (player.isDead()) {
                printText("GAME OVER");
                printDeathMessage();
                return;
            }
            option = giveEncounterOptions();
            switch (option) {
                case "fight":
                    monster.takeDamage(attackMeterGame.playGame(monster));
                    break;
                case "act":
                    canMercy = giveActOptions(monster);
                    break;
                case "item":
                    giveItemOptions();
                    break;
                case "mercy":
                    if (!canMercy)
                        printText(monsterName + " still wants to fight.");
                    else
                        keepFighting = false;
                    break;
            }

            if (monster.isDead()) {
                int gold = monster.calcGoldReward();
                printText(monsterName + " was defeated.");
                printText("You earned " + monster.getExpReward() + " exp and " + gold + " gold.");
                player.addExp(monster.getExpReward());
                player.inventory.addGold(gold);
                return;
            }
        }
        int gold = monster.calcGoldReward();
        printText("You spared " + monsterName + ".");
        printText("You earned 0 exp and " + gold + " gold.");
        player.inventory.addGold(gold);
    }

    /**
     * Prints the inventory and prompts the player for the item to use.
     */
    private void giveItemOptions() {
        Inventory inventory = player.inventory;
        inventory.showInventory();
        if (inventory.getCurrentSize() == 0) {
            printText("Your inventory is empty.");
            return;
        }
        while (true) {
            System.out.print("> ");
            String chosenItem = in.nextLine();
            int index = inventory.findItemByName(chosenItem);
            if (index > -1) {
                Item item = inventory.items.get(index);
                item.use();
                return;
            } else {
                printText("No such item " + chosenItem + ".");
            }
        }
    }

    /**
     * Prints the act options and prompts the player for their choice.
     * @return whether the player chose the correct option to mercy.
     */
    private boolean giveActOptions(Monster monster) {
        HashMap<String, ArrayList<Action>> actOptions = ActOptions.actOptions;
        ArrayList<Action> actionList = actOptions.get(monster.getName());
        for (Action action: actionList) {
            System.out.print(action.getName() + "   ");
        }
        System.out.println();

        while (true) {
            System.out.print("> ");
            String chosenAction = in.nextLine();
            if (chosenAction.equalsIgnoreCase("check")) {
                for (Action action: actionList) {
                    if (action.getName().equalsIgnoreCase("check")) {
                        printText(monster.check() + " " + action.getResponse());
                        return false;
                    }
                }
            }
            for (Action action : actionList) {
                if (action.getName().equalsIgnoreCase(chosenAction)) {
                    printText(action.getResponse());
                    return action.isMercyOption();
                }
            }
            printText("No such action " + chosenAction + ".");
        }
    }

    /**
     * Plays an attack slider mini-game. The closer to the target, the more damage done.
     * @return the amount of damage done to the monster.
     */
    private String giveEncounterOptions() {
        String option;
        System.out.println("FIGHT   ACT   ITEM   MERCY");
        while (true) {
            System.out.print("> ");
            option = in.nextLine().toLowerCase();
            if (isValidOption(option))
                return option;
            else
                System.out.println("Not a valid option: choose FIGHT, ACT, ITEM, or MERCY.");
        }
    }

    public static boolean isValidOption(String option) {
        return option.equals("fight") || option.equals("act") || option.equals("item") || option.equals("mercy");
    }

    public void printDeathMessage() {
        int r = (int) (Math.random() * 3);
        switch (r) {
            case 0 -> printText("You cannot give up just yet...");
            case 1 -> printText("Don't lose hope!");
            case 2 -> printText("Our fate rests upon you...");
        }
        printText(player.getName() + "! Stay determined!");
    }

    private boolean processCommand(Command command) {
        if (command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        switch (commandWord) {
            case "help":
                printHelp();
                break;
            case "go":
                goRoom(command);
                break;
            case "quit":
                if (command.hasSecondWord())
                    System.out.println("Quit what?");
                else
                    return true; // signal that we want to quit
                break;
            case "eat":
                System.out.println("Do you really think you should be eating at a time like this?");
                break;
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