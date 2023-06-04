package zork;

import java.util.HashMap;

public class MonsterList {
    public static HashMap<String, Monster> monsters = new HashMap<>();
    public static HashMap<String, Monster> ruinsMonsters = new HashMap<>();
    public static HashMap<String, Monster> snowdinMonsters = new HashMap<>();
    public static HashMap<String, Monster> waterfallMonsters = new HashMap<>();
    public static HashMap<String, Monster> coreMonsters = new HashMap<>();
    
    static {
        Monster froggit = new Monster(30, 4, 4, 2, 1, "froggit");
        Monster vegtoid = new Monster(72, 5, 0, 6, 1, "vegetoid");
        Monster whimsun = new Monster(10, 4, 0, 2, 2, "whimsun");
        Monster loox = new Monster(50, 5, 4, 7, 5, "loox");
        Monster greaterdog = new Monster(105, 6, 4, 80, 20, "greater dog");
        Monster snowdrake = new Monster(74, 6, 2, 22, 6, "snowdrake");
        Monster icecap = new Monster(48, 6, 0, 17, 6, "icecap");
        Monster moldsmal = new Monster(50, 4, 0, 3, 3, "moldsmal");
        Monster aaron = new Monster(98, 7, 2, 52, 25, "aaron");
        Monster woshua = new Monster(70, 7, 1, 52, 13, "woshua");
        Monster finalfroggit = new Monster(100, 8, 0, 120, 27, "final froggit");
        Monster knightknight = new Monster(150, 8, 2, 180, 37, "knight knight");
        Monster muffet = new Monster(300, 8, 0 , 300, 35, "muffet");
        Monster papyrus = new Monster(300, 8, 2, 0, 0, "papyrus");

        ruinsMonsters.put("froggit", froggit);
        ruinsMonsters.put("vegetoid", vegtoid);
        ruinsMonsters.put("whimsun", whimsun);
        ruinsMonsters.put("loox", loox);
        waterfallMonsters.put("moldsmal", moldsmal);
        waterfallMonsters.put("aaron", aaron);
        waterfallMonsters.put("woshua", woshua);
        snowdinMonsters.put("greater dog", greaterdog);
        snowdinMonsters.put("snowdrake", snowdrake);
        snowdinMonsters.put("ice cap", icecap);
        coreMonsters.put("knigh tknight", knightknight);
        coreMonsters.put("final froggit", finalfroggit);
        monsters.putAll(ruinsMonsters);
        monsters.putAll(waterfallMonsters);
        monsters.putAll(snowdinMonsters);
        monsters.putAll(coreMonsters);
    }
        
}