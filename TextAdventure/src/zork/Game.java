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
    private Player player;
    private final Scanner in = new Scanner(System.in);
    private static final AttackMeterGame attackMeterGame= new AttackMeterGame();
    private static final Charset defaultCharset = Charset.defaultCharset();
    private PrintStream out = null;
    boolean blackJack = false;
    boolean connect4 = false;
    boolean hangman = false;
    boolean math = false;
    boolean numbers = false;
    boolean rockPaperScissors = false;
    boolean ticTacToe = false;
    Room lastRoom;
    boolean firstWaterfall = true;
    boolean firstSnowdin = true;
    boolean firstCore = true;
    static String currentSong = "TextAdventure/src/zork/data/music/Undertale-Ruins.wav";
    ArrayList<String> alreadyDoneRooms = new ArrayList<>();

    ArrayList<Room> ruins = new ArrayList<>();
    ArrayList<Room> snowdin = new ArrayList<>();
    ArrayList<Room> waterfall = new ArrayList<>();
    ArrayList<Room> core = new ArrayList<>();


    /**
     * Create the game and initialise its internal map.
     */
    public Game() {
        System.out.println("IMPORTANT DISCLAIMER:");
        System.out.println("To ensure proper character encoding, please follow these instructions before continuing:");
        System.out.println("1. Stop the game and close any running instances.");
        System.out.println("2. Open the terminal or command prompt.");
        System.out.println("3. Type 'chcp 65001' and press Enter.");
        System.out.println("4. Restart the game and continue playing.");
        System.out.println("Recommended to expand the terminal all the way.");
        System.out.println("Press Enter if you wish to continue.");
        in.nextLine();
        Charset utf8Charset = StandardCharsets.UTF_8;

        try {
            out = new PrintStream(System.out, true, utf8Charset.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            initRooms("TextAdventure\\src\\zork\\data\\rooms.json");
            currentRoom = roomMap.get("Spawn Room");
            savedRoom = currentRoom;
        } catch (Exception e) {
            e.printStackTrace();
        }

        parser = new Parser();

        testMode = false;
    }

    public Player getPlayer() {
        return player;
    }

    private String namePrompt() {
        PlayMusic.play("TextAdventure/src/zork/data/music/Undertale Start Menu.wav", true);
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


    private void initRooms(String fileName) {
        try {
            ArrayList<Room> currentArea = ruins;
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
                JSONObject object = (JSONObject) roomObj;
                boolean lcoked = object.containsKey("isLocked") && (boolean) ((JSONObject) roomObj).get("isLocked");
                room.setSave(isSave);
                room.setLocked(lcoked);
                room.setDescription(roomDescription);
                room.setRoomName(roomName);

                JSONArray jsonExits = (JSONArray) ((JSONObject) roomObj).get("exits");
                ArrayList<Exit> exits = new ArrayList<>();
                for (Object exitObj : jsonExits) {
                    String direction = (String) ((JSONObject) exitObj).get("direction");
                    String adjacentRoom = (String) ((JSONObject) exitObj).get("adjacentRoom");
                    Boolean isLocked = (Boolean) ((JSONObject) exitObj).get("isLocked");
                    Exit exit = new Exit(direction, adjacentRoom, isLocked);
                    exits.add(exit);
                }
                room.setExits(exits);

                JSONArray roomItems = (JSONArray) ((JSONObject) roomObj).get("items");

                if (roomItems != null) {
                    for (Object itemObj : roomItems) {
                        String name = (String) ((JSONObject) itemObj).get("name");
                        String itemDesc = (String) ((JSONObject) itemObj).get("itemDesc");
                        int cost = ((Long) ((JSONObject) itemObj).get("cost")).intValue();
                        Item item = ItemList.items.get(name);
                        room.addToItemList(item);
                        room.addToDescList(itemDesc);
                        room.addToCostList(cost);
                    }
                }
                room.setExits(exits);
                roomMap.put(roomId, room);
                String name = room.getRoomName();
                if (name.equals("Tundra Hallway"))
                    currentArea = snowdin;
                else if (name.equals("Waterfall Entrance"))
                    currentArea = waterfall;
                else if (name.equals("Core Entrance")) {
                    currentArea = core;
                }

                currentArea.add(room);
            }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }


    /**
     * Main play routine. Loops until end of play.
     */
    public void play() {
        if (testMode) {
            System.out.println("GAME IN TEST MODE");
            player = new Player(40, 0, 999, "Frisk");
        }
        else {
            printIntro();
            player = new Player(40, 0, 0, namePrompt());
        }
        printText(currentRoom.longDescription());
        boolean finished = false;
        boolean flowerRoomDialogueShown = false;
        boolean torielEncounterDialogueShown = false;
        boolean sansEncounterDialogueShown = false;
        boolean muffetEncounterDialogueShown = false;
        boolean finalSansEncounterDialogueShown = false;
        boolean asgoreEncounterDialogueShown = false;
        boolean floweyEncounterDialogueShown = false;



        PlayMusic.play(currentSong, true);

        while (!finished) {
            if (currentRoom.getRoomName().equals("Underground Exit")) {
                rollCredits();
                finished = true;
            }
            if (currentRoom.getRoomName().equals("Flower Room") && !flowerRoomDialogueShown) {
                PlayMusic.clip.stop();
                PlayMusic.play("TextAdventure/src/zork/data/music/Undertale-Flowey.wav", true);
                printAsciiImage("flowey");
                printText("Howdy! I'm Flowey. Flowey the Flower!");
                printText("You're new to the underground, aren'tcha?");
                printText("Someone ought to teach you how things work around here!");
                printText("I guess little old me will have to do.");
                printText("In this world, you have a SOUL.");
                Game.sleep(500);
                printText("When you encounter a monster, you can move your soul around to dodge their attacks!");
                printText("Let's play a game, try and get as many of the \"friendliness pellets\" as you can!");
                Game.sleep(500);
                printText("Ready? Here we go!");
                Game.sleep(500);
                Monster flowey = new Monster(999, 0,0, 0, 0, "flowey");
                playMiniGame(flowey);
                Game.sleep(500);
                PlayMusic.stop();
                printAsciiImage("evil flowey");
                player.takeDamage(39);
                System.out.print(player.getName() + " health:");
                showHealthBar(player);
                player.heal(39);
                printText("You idiot!");
                printText("In this world, it's KILL OR BE KILLED.");
                printText("Why would ANYBODY pass up an opportunity like this!?!");
                printText("Die.");
                printText("AHAHAHAAHAHAHAHAHAHA");
                Game.sleep(600);
                printAsciiImage("toriel");
                printText("Toriel: What a terrible creature. Torturing such a poor, innocent youth.");
                printText("Ah, do not be afraid, my child.");
                printText("I am TORIEL. Caretaker of the RUINS.");
                printText("I pass through this place every day to see if anyone has fallen down.");
                printText("You are the first human to come here in a long time.");
                printText("Come, I will guide you through the catacombs.");
                printText("Come this way.");
                flowerRoomDialogueShown = true;
                PlayMusic.play(Game.currentSong, true);
            }
            if(currentRoom.getRoomName().equals("Snowdin Town")) {
                boolean snowdinShop = true;
                while (snowdinShop) {
                    printText("Would you like to enter the Snowdin Shop?");
                    System.out.print(">");
                    String temp = in.nextLine();
                    if (temp.equalsIgnoreCase("y") || temp.equalsIgnoreCase("yes")) {
                        printAsciiImage("shopkeeper");
                        printText("Tough Glove: ATK 5. A worn pink leather glove. For five-fingered folk. Costs 50 gold.");
                        printText("Bandanna: DEF 7. It has seen some wear. It has abs drawn on it. Costs 50 gold.");
                        printText("Cinnabun: Heals 22 HP. A cinnamon roll in the shape of a bunny. Costs 25 gold.");
                        boolean stillPurchasing = true;
                        while (stillPurchasing) {
                            printText("What would you like to purchase? (\"leave\" to leave)");
                            System.out.print("> ");
                            String purchase = in.nextLine();

                            if (purchase.equalsIgnoreCase("leave"))
                                stillPurchasing = false;

                            else if (purchase.equalsIgnoreCase("tough glove")) {
                                if (player.inventory.spendGold(50)) {
                                    player.inventory.addItem(ItemList.items.get("Tough Glove"));
                                }
                            }
                            else if (purchase.equalsIgnoreCase("bandanna")) {
                                if (player.inventory.spendGold(50)) {
                                    player.inventory.addItem(ItemList.items.get("Bandanna"));
                                }
                            }

                            else if (purchase.equalsIgnoreCase("Cinnabun")) {
                                if (player.inventory.spendGold(25)) {
                                    player.inventory.addItem(ItemList.items.get("Cinnabun"));
                                }
                            }
                        }
                    }
                    else if (temp.equalsIgnoreCase("n") || temp.equalsIgnoreCase("no")) {
                        snowdinShop = false;
                    }

                    else {
                        printText("Invalid response. Please answer (y)es or (n)o.");
                    }
                }
            }
            if (currentRoom.getRoomName().equals("Toriel Encounter") && !torielEncounterDialogueShown) {
                PlayMusic.clip.stop();
                PlayMusic.play("TextAdventure/src/zork/data/music/Undertale-Toriel-Theme.wav", true);
                printAsciiImage("toriel");
                printText("You want to leave so badly?");
                printText("Hmph.");
                printText("You are just like the others.");
                printText("What are you proving this way?");
                printText("Fight me or leave!");
                printText("Stop looking at me that way.");
                Game.sleep(500);
                printText("I know you want to go home but...");
                Game.sleep(400);
                printText("But please go upstairs.");
                Game.sleep(200);
                printText("I promise I'll take good care of you here.");
                printText("I know we don't have much but...");
                Game.sleep(300);
                printText("You can have a good life here..");
                printText("Why are you making this so difficult");
                printText("Pathetic, is it not? I could not save a single child.");
                printText("No, I understand. You would just be unhappy trapped in here.");
                printText("The ruins are very small once you get used to them.");
                printText("It would not be right for you to grow up in a place like this");
                printText("If you truly wish to leave ruins... I will not stop you.");
                printText("However... when you leave");
                printText("Please do not come back.");
                printText("I hope you understand.");
                PlayMusic.stop();
                torielEncounterDialogueShown = true;
                PlayMusic.play(Game.currentSong, true);
            }
            if (currentRoom.getRoomName().equals("Muffet Encounter") && !muffetEncounterDialogueShown) {
                if (player.inventory.findItemByName("Spider Cider")>-1 || player.inventory.findItemByName("Spider Donut")>-1) {
                    PlayMusic.clip.stop();
                    PlayMusic.play("TextAdventure/src/zork/data/music/Undertale-Muffet-Theme.wav", true);
                    printAsciiImage("muffet");
                    printText("Huh?");
                    printText("Where did you get that spider food?");
                    printText("Did you steal it?");
                    printText("It's time for me to show you what we do with thieves...");
                    printText("What? a telegram from the spiders in the ruins?");
                    printText("They're telling me that... You helped donate to their cause!");
                    printText("Oh my- this has all been a big misunderstanding..");
                    printText("I thought you were someone that hated spiders-");
                    printText("Sorry for all the trouble-");
                    PlayMusic.stop();
                    PlayMusic.play(Game.currentSong, true);
                }
                else {
                    printText("They said a human with a striped shirt would come through...");
                    printText("I heard that they hate spiders...");
                    printText("I heard that they love to stomp on them...");
                    printText("I heard that they like to rip their legs off...");
                    printAsciiImage("muffet");
                    encounter("muffet");

                }
                muffetEncounterDialogueShown = true;

            }
            if (currentRoom.getRoomName().equals("Castle Hall") && !finalSansEncounterDialogueShown) {
                PlayMusic.clip.stop();
                PlayMusic.play("TextAdventure/src/zork/data/music/Undertale-Waterfall-Music.wav", true);
                printAsciiImage("sans");
                printText("In a few moments, you will meet the king.");
                printText("Together... You will determine the future of this world");
                printText("As for now.. You will be judged");
                printText("You will be judged for your every action.");
                printText("You will be judged for every EXP you've earned");
                printText("What's EXP?");
                printText("It's an acronym.");
                printText("It stands for \"Execution Points\".");
                printText("A way of quantifying the pain you have inflicted on others.");
                printText("If you refuse to fight...");
                printText("Asgore will take your soul and destroy humanity.");
                printText("But if you kill Asgore and go home...");
                printText("Monsters will remain trapped underground.");
                printText("I believe you can do the right thing.");
                printText("We're all counting on you, kid.");
                PlayMusic.stop();
                finalSansEncounterDialogueShown = true;
                PlayMusic.play(Game.currentSong, true);
            }
            if (currentRoom.getRoomName().equals("Asgore Encounter") && !asgoreEncounterDialogueShown) {
                PlayMusic.clip.stop();
                PlayMusic.play("TextAdventure/src/zork/data/music/Undertale-Toriel-Theme.wav", true);
                printAsciiImage("asgore");
                printText("Howdy! How can I...");
                Game.sleep(1000);
                printText("Oh.");
                Game.sleep(1000);
                printText("I so badly want to say, \"Would you like a cup of tea?\".");
                printText("But... You know how it is.");
                printText("Nice day today, huh?");
                printText("Perfect weather for a game of catch.");
                printText("... You know what we must do.");
                encounter("asgore");
                PlayMusic.stop();
                asgoreEncounterDialogueShown = true;
                PlayMusic.play(Game.currentSong, true);
            }
            if (currentRoom.getRoomName().equals("Flowey Encounter") && !floweyEncounterDialogueShown) {
                PlayMusic.clip.stop();
                PlayMusic.play("TextAdventure/src/zork/data/music/Undertale-OmegaFlowey.wav", true);
                printAsciiImage("evil flowey");
                printText("Howdy!");
                printText("I owe you a huge thanks!");
                printText("Without you, I would've never gotten past him!");
                printText("But now, with YOUR help..");
                printText("He's dead.");
                printText("And I've got the human SOULS!");
                printText("It feels great to have a soul inside me now!");
                printText("I feel them wriggling");
                printText("But after all...");
                printText("I only have six souls.");
                printText("I still need one more...");
                printText("Before I become GOD.");
                printText("And then, with my newfound powers...");
                printText("Monster. Humans. Everyone. I'll show them ALL the real meaning of this world.");
                printText("KILL OR BE KILLED.");
                Game.sleep(1000);
                printAsciiImage("scary face");
                encounter("flowey");
                PlayMusic.stop();
                floweyEncounterDialogueShown = true;
                PlayMusic.play(Game.currentSong, true);
            }

            if (currentRoom.getRoomName().equals("Sans Dialogue") && !sansEncounterDialogueShown) {
                PlayMusic.clip.stop();
                PlayMusic.play("TextAdventure/src/zork/data/music/Undertale-Papyrus.wav", true);
                printAsciiImage("sans");
                printText("hey Kiddo.");
                Game.sleep(1000);
                printText("soooo.... you're a human, right?");
                printText("that's hilarious.");
                printText("i'm Sans. Sans the skeleton");
                printText("i'm actually supposed to be on watch for humans");
                printText("but... y'know...");
                printText("i don't really care about capturing anybody.");
                printText("now my brother, Papyrus...");
                printText("he's a human-hunting fanatic");
                printText("hey, actually, I think that's him over there.");
                printText("i have an idea. Go through this gate thingy.");
                printAsciiImage("gate");
                printText("yeah, go right through. my bro made the bars too wide to stop anyone.");
                Game.sleep(1000);
                printText("sup, bro");
                printAsciiImage("papyrus");
                printText("Papyrus: YOU KNOW WHAT. SUP, BROTHER.");
                printText("IT'S BEEN EIGHT DAYS AND YOU STILL HAVEN'T...");
                printText("RECALIBRATED. YOUR. PUZZLES.");
                printText("YOU JUST HANG AROUND YOUR STATION.");
                printText("WHAT ARE YOU EVEN DOING!?!");
                Game.sleep(1000);
                printText("sans: staring at this lamp. it's really cool. do you wanna look?");
                Game.sleep(500);
                printAsciiImage("lamp");
                Game.sleep(2000);
                printText("Papyrus: NO!! I DON'T HAVE TIME FOR THAT!!!");
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
                printText("sans: hmm. maybe this lamp will help you.");
                printText("papyrus: SANS!! YOU ARE NOT HELPING!! YOU LAZYBONES!!!");
                printText("ALL YOU DO IS SIT AND BOONDOGGLE");
                printText("YOU GET LAZIER AND LAZIER EVERY DAY!!!");
                Game.sleep(500);
                printText("sans: hey, take it easy. I've gotten a TON of work done today. a SKELE-TON!");
                Game.sleep(1000);
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
                printText("sans: wow.. sounds like you're really working yourself...");
                printText("down to the bone...");
                Game.sleep(1200);
                printText("Papyrus: UGH.. I'LL ATTEND TO MY WORK. AS FOR YOUR WORK.. PUT A LITTLE MORE..");
                printText("BACKBONE INTO IT!!!!");
                printText("NYEHEHEHEHEHEHEHEHEHEHEHEHEHEH!!!");
                Game.sleep(1000);
                printText("*Papyrus leaves*");
                printText("sans to frisk: ok.. you can come out now");
                printText("you oughta get going. he might come back. and if he does...");
                printText("you'll have to sit through more of my hilarious jokes.");
                Game.sleep(2000);
                printText("what's the holdup? Look. there's nothing to be afraid of.");
                printText("it's just a dark caven filled with skeletons and monsters.");
                Game.sleep(500);
                printText("hey, before you go, can you do me a favor?");
                printText("my brothers been down lately, and seeing a human, it might make his day.");
                printText("Don't worry, he's not dangerous, even if he tried to be.");
                sansEncounterDialogueShown = true;
                PlayMusic.stop();
                PlayMusic.play(Game.currentSong, true);
            }

            Command command;
            try {
                command = parser.getCommand();
                finished = processCommand(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        PlayMusic.play("TextAdventure/src/zork/data/music/Undertale-Once-Upon-A-Time-Music.wav", true);
        printTextCustomDelay("Long ago, two races ruled over Earth: HUMANS and MONSTERS.", 50);
        Game.sleep(500);
        printTextCustomDelay("One day, war broke out between the two races.", 50);
        Game.sleep(500);
        printTextCustomDelay("After a long battle, the humans were victorious.", 50);
        Game.sleep(500);
        printTextCustomDelay("They sealed the monsters underground with a magic spell.", 50);
        Game.sleep(500);
        printTextCustomDelay("Legends say that those who climb the mountain never return.", 50);

        PlayMusic.stop();
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
        if (monsterName.equals("muffet")) {
            PlayMusic.play("TextAdventure/src/zork/data/music/Undertale-Muffet-Theme.wav", true);
        }
        else {
            PlayMusic.play("TextAdventure/src/zork/data/music/Undertale-Enemy.wav", true);
        }
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
                monster.resetHp();
                PlayMusic.stop();
                PlayMusic.play(Game.currentSong, true);
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
                PlayMusic.stop();
                PlayMusic.play(Game.currentSong, true);
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
        monster.resetHp();
        PlayMusic.stop();
        PlayMusic.play(Game.currentSong, true);
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
        player.resetHp();
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
        while (true) {
            printText("Save?:\n>");
            String temp = in.nextLine();
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
            System.out.println("Invalid command. use \"help\" for help.");
            return false;
        }

        String commandWord = command.getCommandWord().strip();
        switch (commandWord.toLowerCase()) {
            case "help":
                printHelp();
                break;
            case "go":
                goRoom(command);
                break;
            case "north":
                command = new Command("go", "north");
                goRoom(command);
                break;
            case "south":
                command = new Command("go", "south");
                goRoom(command);
                break;
            case "east":
                command = new Command("go", "east");
                goRoom(command);
                break;
            case "west":
                command = new Command("go", "west");
                goRoom(command);
                break;
            case "northeast":
                command = new Command("go", "northeast");
                goRoom(command);
                break;
            case "southeast":
                command = new Command("go", "southeast");
                goRoom(command);
                break;
            case "northwest":
                command = new Command("go", "northwest");
                goRoom(command);
                break;
            case "southwest":
                command = new Command("go", "southwest");
                goRoom(command);
                break;
            case "exits":
                currentRoom.getExits().forEach((r) -> System.out.println(r.getDirection() + ": " + r.getAdjacentRoom()));
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
                    if (player.inventory.getCurrentSize() > 0) {
                        System.out.println("Drop what?");
                        player.inventory.showInventory();
                        System.out.print("> ");
                        String temp = in.nextLine();
                        int index = player.inventory.findItemByName(temp);
                        if (index > -1) {
                            player.inventory.dropItem(temp);
                        }
                    } else {
                        printText("Nothing to drop.");
                    }
                } else {
                    int index = player.inventory.findItemByName(command.getSecondWord());
                    if (index > -1) {
                        player.inventory.dropItem(command.getSecondWord());
                    }
                }
                break;
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
                break;
            case "search":
                ArrayList<Item> itemList = currentRoom.getItemArrayList();
                ArrayList<String> descriptions = currentRoom.getDescArrayList();
                ArrayList<Integer> costs = currentRoom.getCostArrayList();
                if (itemList.size() == 0 && !currentRoom.getRoomName().toLowerCase().contains("puzzle")) {
                    printText("You searched the room but found nothing.");
                    break;
                }
                for (int i = 0; i < itemList.size(); i++) {
                    Item item = itemList.get(i);
                    printText("You found " + item.getName());
                    printText(descriptions.get(i));

                    if (costs.get(i) == 0) {
                        printText("Take " + item.getName() + "?");
                        while (true) {
                            System.out.print("> ");
                            String temp = in.nextLine();
                            if (temp.equalsIgnoreCase("yes") || temp.equalsIgnoreCase("y")) {
                                player.inventory.addItem(item);
                                itemList.remove(i);
                                break;
                            } else if (temp.equalsIgnoreCase("no") || temp.equalsIgnoreCase("n")) {
                                printText("You left the " + item.getName() + ".");
                                break;
                            } else {
                                printText("Invalid response. Please answer (y)es or (n)o.");
                            }
                        }
                    }
                    else {
                        printText("Buy " + item.getName()+ " for " + costs.get(i) + " gold?");
                        while (true) {
                            System.out.print("> ");
                            String temp = in.nextLine();
                            if (temp.equalsIgnoreCase("yes") || temp.equalsIgnoreCase("y")) {
                                if (player.inventory.spendGold(costs.get(i))) {
                                    player.inventory.addItem(item);
                                }
                                itemList.remove(i);
                                break;
                            } else if (temp.equalsIgnoreCase("no") || temp.equalsIgnoreCase("n")) {
                                printText("You didn't buy the " + item.getName() + ".");
                                break;
                            } else {
                                printText("Invalid response. Please answer (y)es or (n)o.");
                            }
                        }
                    }
                }

                if (currentRoom.getRoomName().toLowerCase().contains("puzzle")) {
                    if (!alreadyDoneRooms.contains(currentRoom.getRoomName())) {
                        while (true) {
                            printText("You found a puzzle, do you want to play? (y/n)");
                            out.print("> ");
                            String response;
                            response = in.nextLine();
                            if (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("yes")) {
                                if (!blackJack) {
                                    if (Puzzles.playBlackjack()) {
                                        Room ghost = roomMap.get("Ghost Room");
                                        ghost.setLocked(false);
                                        alreadyDoneRooms.add(currentRoom.getRoomName());
                                        blackJack = true;
                                        printText("Area unlocked.");
                                    }
                                } else if (!connect4) {
                                    if (Puzzles.playConnectFour()) {
                                        Room crossroads = roomMap.get("Ruins Crossroads");
                                        crossroads.setLocked(false);
                                        alreadyDoneRooms.add(currentRoom.getRoomName());
                                        connect4 = true;
                                        printText("Area unlocked.");
                                    }
                                } else if (!hangman) {
                                    if (Puzzles.playHangman()) {
                                        Room spaghetti = roomMap.get("Tundra Spaghetti");
                                        spaghetti.setLocked(false);
                                        alreadyDoneRooms.add(currentRoom.getRoomName());
                                        hangman = true;
                                        printText("Area unlocked.");
                                    }
                                } else if (!math) {
                                    if (Puzzles.playMathGame(10, 20)) {
                                        Room telescope = roomMap.get("Telescope Room");
                                        telescope.setLocked(false);
                                        alreadyDoneRooms.add(currentRoom.getRoomName());
                                        math = true;
                                        printText("Area unlocked.");
                                    }
                                } else if (!numbers) {
                                    if (Puzzles.playNumberGuessingGame()) {
                                        alreadyDoneRooms.add(currentRoom.getRoomName());
                                        numbers = true;
                                    }
                                } else if (!rockPaperScissors) {
                                    if (Puzzles.playRockPaperScissors()) {
                                        Room coreMain = roomMap.get("Core Main");
                                        coreMain.setLocked(false);
                                        alreadyDoneRooms.add(currentRoom.getRoomName());
                                        rockPaperScissors = true;
                                        printText("Area unlocked.");
                                    }
                                } else if (!ticTacToe) {
                                    if (Puzzles.playTicTacToe()) {
                                        Room centerMain = roomMap.get("Core Center Main");
                                        centerMain.setLocked(false);
                                        alreadyDoneRooms.add(currentRoom.getRoomName());
                                        ticTacToe = true;
                                        printText("Area unlocked.");
                                    }
                                }
                                break;
                            }
                            else if (response.equalsIgnoreCase("n") || response.equalsIgnoreCase("no")) {
                                printText("You skipped the puzzle.");
                                break;
                            }
                            else {
                                printText("Invalid response. Please respond with (y)es or (n)o.");
                            }
                        }
                    }else {
                        printText("You found a puzzle but it's already been solved.");
                    }
                    break;
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

    public ArrayList<Room> getArea() {
        if (ruins.contains(currentRoom)) {
            return ruins;
        } else if (snowdin.contains(currentRoom)) {
            return snowdin;
        }
        else if (waterfall.contains(currentRoom)) {
            return waterfall;
        } else if (core.contains(currentRoom)) {
            return core;
        }
        return null;
    }

    public boolean switchingAreas() {
        return (getArea().contains(currentRoom) != getArea().contains(lastRoom));
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
            lastRoom = currentRoom;
            currentRoom = nextRoom;


            if (ruins.contains(currentRoom)) {
                if (switchingAreas()) {
                    PlayMusic.play("TextAdventure/src/zork/data/music/Undertale-Ruins.wav", false);
                }
                currentSong = "TextAdventure/src/zork/data/music/Undertale-Ruins.wav";
            }

            if (snowdin.contains(currentRoom)) {
                if (switchingAreas() || firstSnowdin) {
                    PlayMusic.play("TextAdventure/src/zork/data/music/Undertale-Snowdin-Music.wav", false);
                    firstSnowdin = false;
                }
                currentSong = "TextAdventure/src/zork/data/music/Undertale-Snowdin-Music.wav";
            }

            if (waterfall.contains(currentRoom)) {
                if (switchingAreas() || firstWaterfall) {
                    PlayMusic.play("TextAdventure/src/zork/data/music/Undertale-Waterfall-Music.wav", false);
                    firstWaterfall = false;
                }
                currentSong = "TextAdventure/src/zork/data/music/Undertale-Waterfall-Music.wav";
            }

            if (core.contains(currentRoom)) {
                if (switchingAreas() || firstCore) {
                    PlayMusic.play("TextAdventure/src/zork/data/music/Undertale-CORE.wav", false);
                    firstCore = false;
                }
                currentSong = "TextAdventure/src/zork/data/music/Undertale-CORE.wav";
            }

            Game.printText(currentRoom.longDescription());

            double r = Math.random();

            if (r < 0.25) {
                if (ruins.contains(currentRoom)) {
                    int index = (int) (Math.random() * MonsterList.ruinsMonstersList.size());
                    encounter(MonsterList.ruinsMonstersList.get(index).getName());
                }

                else if (snowdin.contains(currentRoom)) {
                    int index = (int) (Math.random() * MonsterList.snowdinMonstersList.size());
                    encounter(MonsterList.snowdinMonstersList.get(index).getName());
                }

                else if (waterfall.contains(currentRoom)) {
                    int index = (int) (Math.random() * MonsterList.waterfallMonstersList.size());
                    encounter(MonsterList.waterfallMonstersList.get(index).getName());
                }

                else if (core.contains(currentRoom)) {
                    int index = (int) (Math.random() * MonsterList.coreMonstersList.size());
                    encounter(MonsterList.coreMonstersList.get(index).getName());
                }
            }

            if (currentRoom.isSave())
                if (!currentRoom.equals(savedRoom))
                    if (savePrompt())
                        savedRoom = currentRoom;
        }
    }
}