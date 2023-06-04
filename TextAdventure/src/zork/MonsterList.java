package zork;

import java.util.ArrayList;

public class MonsterList {
    public static ArrayList<Monster> snowdinMonsters = new ArrayList<>();
    public static ArrayList<Monster> waterfallMonsters = new ArrayList<>();
    public static ArrayList<Monster> coreMonsters = new ArrayList<>();
    public static ArrayList<Monster> ruinsMonsters = new ArrayList<>();
    
    static {
        Monster froggit = new Monster(30, 4, 4, 2, 1, "froggit");

    }
        
}