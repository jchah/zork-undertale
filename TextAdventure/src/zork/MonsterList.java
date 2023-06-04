package zork;

import java.util.ArrayList;

public class MonsterList {
    public static ArrayList<Monster> ruinsMonsters = new ArrayList<>();
    public static ArrayList<Monster> snowdinMonsters = new ArrayList<>();
    public static ArrayList<Monster> waterfallMonsters = new ArrayList<>();
    public static ArrayList<Monster> coreMonsters = new ArrayList<>();
    
    static {
        Monster froggit = new Monster(30, 4, 4, 2, 1, "froggit");
        Monster vegtoid = new Monster(72, 5, 0, 6, 1, "vegetoid");
        Monster whimsun = new Monster(10, 4, 0, 2, 2, "whimsun");
        Monster loox = new Monster(50, 5, 4, 7, 5, "loox");
        Monster greaterdog = new Monster(105, 6, 4, 80, 20, "greaterdog");
        Monster snowdrake = new Monster(74, 6, 2, 22, 6, "snowdrake");
        Monster icecap = new Monster(48, 6, 0, 17, 6, "icecap");
        Monster moldsmal = new Monster(50, 4, 0, 3, 3, "moldsmal");
        Monster aaron = new Monster(98, 7, 2, 52, 25, "aaron");
        Monster woshua = new Monster(70, 7, 1, 52, 13, "woshua");
        Monster finalfroggit = new Monster(100, 8, 0, 120, 27, "finalfroggit");
        Monster knightknight = new Monster(150, 8, 2, 180, 37, "knightknight");
        Monster muffet = new Monster(300, 8, 0 , 300, 35, "muffet");
        Monster papyrus = new Monster(300, 8, 2, 0, 0, "papyrus");

        ruinsMonsters.add(froggit);
        ruinsMonsters.add(vegtoid);
        ruinsMonsters.add(whimsun);
        ruinsMonsters.add(loox);
        waterfallMonsters.add(moldsmal);
        waterfallMonsters.add(aaron);
        waterfallMonsters.add(woshua);
        snowdinMonsters.add(greaterdog);
        snowdinMonsters.add(snowdrake);
        snowdinMonsters.add(icecap);
        coreMonsters.add(knightknight);
        coreMonsters.add(finalfroggit);
    }
        
}