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

    public static HashMap<String, Room> roomMap = new HashMap<>();

    public static Game game = new Game();
    public boolean testMode;
    private final Parser parser;
    private Room currentRoom;
    private Room savedRoom;
    private final Player player;
    private final Scanner in = new Scanner(System.in);
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
            currentRoom = roomMap.get("Snowdin Town");
            savedRoom = currentRoom;
        } catch (Exception e) {
            e.printStackTrace();
        }

        parser = new Parser();

        testMode = true;

        if (testMode) {
            System.out.println("GAME IN TEST MODE");
            player = new Player(20, 0, 0, "Frisk");
        }
        else {
            printIntro();
            player = new Player(20, 0, 0, namePrompt());
        }
    }

    public Player getPlayer() {
        return player;
    }

    private String namePrompt() {
        String name;
        String temp;
        while (true) {
            printText("Name the fallen human: ");
            name = in.nextLine();
            if (name.length() >= 1 && name.length() <= 15) {
                boolean good = false;
                while(!good) {
                    printText("Your name is " + name + ", confirm?");
                    temp = in.nextLine();
                    if (temp.equalsIgnoreCase("y") || temp.equalsIgnoreCase("yes")) {
                        return name;
                    } else if (!temp.equalsIgnoreCase("n") && !temp.equalsIgnoreCase("no")) {
                        printText("Invalid response. Please answer (y)es or (n)o");
                    } else
                        good = true;
                }
            }
            else {
                printText("Name must be within 1 and 15 characters");
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
            boolean isSave = (boolean) ((JSONObject) roomObj).get("isSave");
            room.setSave(isSave);
            room.setDescription(roomDescription);
            room.setRoomName(roomName);

            JSONArray jsonExits = (JSONArray) ((JSONObject) roomObj).get("exits");
            ArrayList<Exit> exits = new ArrayList<>();
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
        player.inventory.addItem(ItemList.items.get("Bandage"));
        player.inventory.addItem(ItemList.items.get("Stick"));

        printText(currentRoom.longDescription());
        boolean finished = false;
        boolean flowerRoomDialogueShown = false;
        boolean torielEncounterDialogueShown = false;
        boolean sansEncounterDialogueShown = false;

        while (!finished) {
            if (currentRoom.getRoomName().equals("Flower Room") && !flowerRoomDialogueShown) {
                printAsciiImage("flowey");
                printText("Howdy! I'm Flowey. Flowey the Flower!");
                printText("You're new to the underground, aren'tcha?");
                printText("Someone ought to teach you how things work around here!");
                printText("I guess little old me will have to do.");
                printText("Ready? Here we go!");
                flowerRoomDialogueShown = true;
            }
            if(currentRoom.getRoomName().equals("Snowdin Town")) {
                boolean snowdinShop = true;
                while(snowdinShop){
                printText("Would you like to enter the Snowdin Shop?");
                System.out.print(">");
                String temp = in.nextLine();
                if (temp.equalsIgnoreCase("y") || temp.equalsIgnoreCase("yes")) {
                    printText("Tough Glove: ATK 5. A worn pink leather glove. For five-fingered folk. Costs 50 gold.");
                    printText("Manly Bandanna: DEF 7. It has seen some wear. It has abs drawn on it. Costs 50 gold.");
                    printText("Cinnabun: Heals 22 HP. A cinnamon roll in the shape of a bunny. Costs 25 gold.");
                    boolean snowdinShopPurchase = true;
                    while(snowdinShopPurchase){
                        String snowdinPurchase = in.nextLine();
                        if (snowdinPurchase.equalsIgnoreCase("tough glove")){
                            player.inventory.spendGold(50);
                            player.inventory.addItem(ItemList.items.get("Tough Glove"));
                            printText("Would you like to make another purchase?");
                            System.out.print(">");
                            String nextPurchase = in.nextLine();
                            if (nextPurchase.equalsIgnoreCase("n") || nextPurchase.equalsIgnoreCase("no")) {
                                snowdinShopPurchase = false;
                            }
                            else if (!nextPurchase.equalsIgnoreCase("y") || !nextPurchase.equalsIgnoreCase("yes")) {
                                printText("Invalid input. Options are: \"n\", \"no\", \"y\", \"yes\"");
                            }
                            else {
                                printText("What would you like to purchase?");
                            }
                        }
                        else if(snowdinPurchase.equalsIgnoreCase("manly bandanna")){
                            player.inventory.spendGold(50);
                            player.inventory.addItem(ItemList.items.get("Bandanna"));
                        }
                        else if(snowdinPurchase.equalsIgnoreCase("cinnabun")){
                            player.inventory.spendGold(25);
                            player.inventory.addItem(ItemList.items.get("Cinnabun"));
                        }
                        else{
                            printText("Invalid input. Options are: \"tough glove\", \"manly bandanna\", \"cinnabun\".");
                        }

                    
                    }
                    snowdinShop = false;
                } else if (!temp.equalsIgnoreCase("n") && !temp.equalsIgnoreCase("no")) {
                    printText("Invalid response. Please answer (y)es or (n)o");      
                }
                else snowdinShop = false;
            }
            }
            if (currentRoom.getRoomName().equals("Toriel Encounter") && !torielEncounterDialogueShown) {
                printAsciiImage("toriel");
                printText("You want to leave so badly?");
                printText("Hmph.");
                printText("You are just like the others.");
                printText("There is only one solution to this.");
                printText("Prove yourself");
                printText("Prove to me you are strong enough to survive.");
                torielEncounterDialogueShown = true;
            }

            if (currentRoom.getRoomName().equals("Sans Encounter") && !sansEncounterDialogueShown) {
                printAsciiImage("sans");
                printText("You're a human, right?");
                printText("That's hilarious.");
                printText("I'm Sans. Sans the skeleton");
                printText("I'm actually supposed to be on watch for humans");
                printText("but... y'know...");
                printText("I don't really care about capturing anybody.");
                printText("Now my brother, Papyrus...");
                printText("He's a human-hunting fanatic");
                printText("Hey, actually, I think that's him over there.");
                printText("I have an idea. Go through this gate thingy.");
                printAsciiImage("gate");
                printText("Yeah, go right through. My bro made the bars too wide to stop anyone.");
                printText("sup, bro");
                printAsciiImage("papyrus");
                printText("papyrus: YOU KNOW WHAT. SUP, BROTHER.");
                printText("IT'S BEEN EIGHT DAYS AND YOU STILL HAVEN'T...");
                printText("RECALIBRATED. YOUR. PUZZLES.");
                printText("YOU JUST HANG AROUND YOUR STATION.");
                printText("WHAT ARE YOU EVEN DOING!?!");
                Game.sleep(1000);
                printText("sans: Staring at this lamp. It's really cool. Do you wanna look?");
                Game.sleep(500);
                printAsciiImage("lamp");
                printText("papyrus: NO!! I DON'T HAVE TIME FOR THAT!!!");
                Game.sleep(500);
                printAsciiImage("papyrus");
                printText("WHAT IF A HUMAN COMES THROUGH HERE???");
                printText("I WANT TO BE READY!!!");
                printText("I WILL BE THE ONE!! I MUST BE THE ONE!!");
                printText("I WILL CAPTURE A HUMAN!!!");
                printText("THEN, I, THE GREAT PAPYRUS");
                printText("WILL GET ALL THE THINGS I UTTERLY DESERVE");
                printText("RESPECT... RECOGNITION...");
                printText("I WILL FINALLY BE ABLE TO JOIN THE ROYAL GUARD!!!");
                printText("PEOPLE WILL ASK, TO, BE MY, FRIEND!");
                printText("I WILL BATHE IN A SHOWER OF KISSES EVERY MORNING.");
                Game.sleep(500);
                printText("sans: hmm. Maybe this lamp will help you.");
                printText("papyrus: SANS!! YOU ARE NOT HELPING!! YOU LAZYBONES!!!");
                printText("ALL YOU DO IS SIT AND BOONDOGGLE");
                printText("YOU GET LAZIER AND LAZIER EVERY DAY!!!");
                Game.sleep(500);
                printText("sans: hey, take it easy. I've gotten a TON of work done today.");
                printText("a SKELE-TON!");
                Game.sleep(1300);
                printText("papyrus: SANS!!!");
                Game.sleep(300);
                printText("sans: come on.. you're smiling.");
                Game.sleep(200);
                printText("papyrus: I AM AND I HATE IT!!!");
                Game.sleep(300);
                printText("SIGH...");
                printText("WHY DOES SOMEONE AS GREAT AS ME...");
                printText("HAVE TO DO SO MUCH JUST TO GET SOME RECOGNITION.");
                Game.sleep(500);
                printText("sans: wow.. Sounds like you're really working yourself...");
                printText("down to the bone.. *wink*");
                Game.sleep(1200);
                printText("papyrus: UGH.. I'LL ATTEND TO MY WORK. AS FOR YOUR WORK.. PUT A LITTLE MORE..");
                printText("BACKBONE INTO IT!!!!");
                printText("NYEHEHEHEHEHEHEHEHEHEHEHEHEHEH!!!");
                Game.sleep(1000);
                printText("sans: ok.. you can come out now");
                printText("You oughta get going. he might come back. and if he does...");
                printText("you'll have to sit through more of my hilarious jokes.");
                Game.sleep(1900);
                printText("What's the holdup? Look. There's nothing to be afraid of.");
                printText("It's just a dark caven filled with skeletons and monsters. *wink*");
                Game.sleep(600);
                printText("Hey, before you go, can you do me a favor?");
                printText("My brothers been down lately, and seeing a human, it might make his day.");
                printText("Don't worry, he's not dangerous.");
                sansEncounterDialogueShown = true;
            }

            Command command;
            try {
                command = parser.getCommand();
                finished = processCommand(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        rollCredits();
    }
    

    private void rollCredits() {
        printTextCustomDelay("UNDERTALE TEXT ADVENTURE", 100);
        printTextCustomDelay("A GAME BY...", 100);
        printTextCustomDelay("JAD C.", 50);
        printTextCustomDelay("RYAN K.", 50);
        printTextCustomDelay("KAI S.", 50);
        printTextCustomDelay("AND SAVVA P.",50);
        printTextCustomDelay("THANK YOU FOR PLAYING! :)", 100);
    }

    /**
     * Print out the opening message for the player.
     */
    private static void printIntro() {
        // play Once Upon a Time
        printTextCustomDelay("Long ago, two races ruled over Earth: HUMANS and MONSTERS.\nOne day, war broke out between the two races.\nAfter a long battle, the humans were victorious.\nThey sealed the monsters underground with a magic spell.\nMany years later...\nMT.Ebott.\n201X\nLegends say that those who climb the mountain never return.\n", 50);
    }

    private static void printAsciiImage(String name) {
        // allow readers to read text before image
        Game.sleep(1000);
        try {
            File ascii = new File("TextAdventure\\src\\zork\\data\\ascii_art\\" + name.toLowerCase() + ".txt");
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
            sleep(30);
        }
        System.out.println();
    }

    public static void printTextCustomDelay(String str, int delay) {
        String[] chars = str.split("");
        for (String aChar : chars) {
            System.out.print(aChar);
            sleep(delay);
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

    public static String encodeToString(String string) {
        String data = "";
        byte[] sourceBytes = string.getBytes(StandardCharsets.UTF_8);
        try {
            data = new String(sourceBytes , defaultCharset.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void showHealthBar(Entity entity) {
        String data = encodeToString("❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤");
        String[] bar = data.split("");
        int startIndex = bar.length - 1;
        double percent = (double) entity.getHp() / entity.getMaxHp();
        int offset = (int) (bar.length * (1 - percent));
        for (int i = 0; i < Math.min(offset, bar.length); i++) {
            bar[startIndex] = "\uD83D\uDC94";
            startIndex--;
        }
        for (String marker: bar) {
            out.print(marker);
        }
        System.out.println();
    }

    private void encounter(String monsterName) {
        Monster monster = MonsterList.monsters.get(monsterName.toLowerCase());
        monsterName = monsterName.toUpperCase();
        String option;
        boolean keepFighting = true;
        boolean canMercy = false;
        printAsciiImage(monsterName);
        printText("A wild " + monsterName + " appeared!");
        while (keepFighting) {
            System.out.print(player.getName() + " health: ");
            showHealthBar(player);
            System.out.print(monsterName + " health: ");
            showHealthBar(monster);

            if (player.isDead()) {
                printText("GAME OVER");
                playerRespawn();
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
                monster.resetHp();
                int gold = monster.calcGoldReward();
                printText(monsterName + " was defeated.");
                printText("You earned " + monster.getExpReward() + " exp and " + gold + " gold.");
                player.addExp(monster.getExpReward());
                player.updateLv();
                player.inventory.addGold(gold);
                return;
            }
            if (keepFighting) {
                Game.sleep(1000);
                player.takeDamage(playMiniGame(monster));
            }
        }
        int gold = monster.calcGoldReward();
        printText("You spared " + monsterName + ".");
        printText("You earned 0 exp and " + gold + " gold.");
        player.inventory.addGold(gold);
    }

    private int playMiniGame(Monster monster) {
        int temp;
        KeyListener.run = true;
        temp = MiniGame.miniGame.play(monster);
        KeyListener.run = false;
        return temp;
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
                printText("No such item \"" + shortenInvalid(chosenItem) + "\" in your inventory.");
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
            printText("No such action \"" + shortenInvalid(chosenAction) + "\".");
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
            try {
                System.in.read(new byte[System.in.available()]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            option = in.nextLine().toLowerCase();
            if (isValidOption(option))
                return option;
            else
                System.out.println("Not a valid option: choose FIGHT, ACT, ITEM, or MERCY.");
        }
    }

    private static boolean isValidOption(String option) {
        return option.equals("fight") || option.equals("act") || option.equals("item") || option.equals("mercy");
    }

    private void playerRespawn() {
        printDeathMessage();
        currentRoom = savedRoom;
        System.out.println("You were brought back to " + currentRoom.getRoomName() + ".");
    }



    private void printDeathMessage() {
        int r = (int) (Math.random() * 3);
        switch (r) {
            case 0 -> printText("You cannot give up just yet...");
            case 1 -> printText("Don't lose hope!");
            case 2 -> printText("Our fate rests upon you...");
        }
        printText(player.getName() + "! Stay determined!");
    }

    private boolean savePrompt() {
        String temp;
        while (true) {
            printText("Save?: ");
            temp = in.nextLine();
            if (temp.equalsIgnoreCase("y") || temp.equalsIgnoreCase("yes")) {
                printText("Game saved.");
                return true;
            } else if (temp.equalsIgnoreCase("n") || temp.equalsIgnoreCase("no")) {
                return false;
            } else
                printText("Invalid response. Please answer (y)es or (n)o.");
        }
    }



    private boolean processCommand(Command command) {
        if (command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        switch (commandWord.toLowerCase()) {
            case "help":
                printHelp();
                break;
            case "go":
                goRoom(command);
                break;
            case "quit":
                if (command.hasSecondWord())
                    System.out.println("? -> " + shortenInvalid(command.getSecondWord()));
                else
                    return true; // signal that we want to quit
                break;
            case "eat":
                if (!command.hasSecondWord()) {
                    System.out.println("Eat what?");
                    System.out.print("> ");
                    String temp = in.nextLine();
                    int index = player.inventory.findItemByName(temp);
                    if (index > -1) {
                        Food food = (Food) player.inventory.items.get(index);
                        food.use();
                    } else {
                        System.out.println("No such food \"" + shortenInvalid(temp) + "\" in your inventory.");
                    }
                } else {
                    int index = player.inventory.findItemByName(command.getSecondWord());
                    if (index > -1) {
                        Food food = (Food) player.inventory.items.get(index);
                        food.use();
                    } else {
                        System.out.println("No such item \"" + shortenInvalid(command.getSecondWord()) + "\" in your inventory.");
                    }
                }
                break;
            case "use":
                if (!command.hasSecondWord()) {
                    System.out.println("Use what?");
                    System.out.print("> ");
                    String temp = in.nextLine();
                    int index = player.inventory.findItemByName(temp);
                    if (index > -1) {
                        player.inventory.items.get(index).use();
                    } else {
                        System.out.println("No such item \"" + shortenInvalid(temp) + "\" in your inventory.");
                    }
                } else {
                    int index = player.inventory.findItemByName(command.getSecondWord());
                    if (index > -1) {
                        player.inventory.items.get(index).use();
                    } else {
                        System.out.println("No such item \"" + shortenInvalid(command.getSecondWord()) + "\" in your inventory.");
                    }
                }
                break;
            case "drop":
                if (!command.hasSecondWord()) {
                    System.out.println("Drop what?");
                    System.out.print("> ");
                    String temp = in.nextLine();
                    int index = player.inventory.findItemByName(temp);
                    if (index > -1) {
                        player.inventory.dropItem(temp);
                    }
                } else {
                    int index = player.inventory.findItemByName(command.getSecondWord());
                    if (index > -1) {
                        player.inventory.dropItem(command.getSecondWord());
                    }
                }
            case "inventory":
                if (command.hasSecondWord())
                    System.out.println("? -> " + shortenInvalid(command.getSecondWord()));
                else {
                    player.inventory.showInventory();
                }
                break;
            case "stats":
                if (command.hasSecondWord())
                    System.out.println("? -> " + shortenInvalid(command.getSecondWord()));
                else {
                    System.out.println(player.check());
                }
        }
        return false;
    }

    private String shortenInvalid(String string) {
        if (string.length() > 20) {
            return string.substring(0, 17) + "...";
        }
        return string;
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
        String direction;
        if (command.hasSecondWord()) {
            direction = command.getSecondWord();
        }
        else {
            System.out.println("Go where?");
            System.out.print("> ");
            String temp = in.nextLine();
            if (Room.isValidDirection(temp)) {
                direction = temp;
            }
            else {
                printText(shortenInvalid(temp) + " is not a valid direction");
                return;
            }
        }

        // Try to leave current room.
        Room nextRoom = currentRoom.nextRoom(direction);

        if (nextRoom != null) {
            currentRoom = nextRoom;
            Game.printText(currentRoom.longDescription());

            if (currentRoom.isSave())
                if (savePrompt())
                    savedRoom = currentRoom;
        }
    }
}