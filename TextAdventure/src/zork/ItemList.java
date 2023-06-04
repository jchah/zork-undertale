package zork;

import java.util.HashMap;

public class ItemList {
    public static HashMap<String, Item> items = new HashMap<>();

    static {
        Armor bandage = new Armor(1, "Bandage");
        Weapon stick = new Weapon(1, "Stick");
        Weapon toyknife = new Weapon(3, "ToyKnife");
        Armor tuffglove = new Armor(5, "ToughGlove");
        Armor bandanna = new Armor(7, "Bandanna");
        Food nicecream = new Food(15, "NiceCream");
        Food cinnabun = new Food(22, "CinnaBun");
        Armor tutu = new Armor(10, "OldTutu");
        Weapon balletshoes = new Weapon(7, "BalletShoes");
        Food hotdog = new Food(20, "HotDog");
        Food hotcat = new Food(21, "HotCat");
        Armor locket = new Armor(15, "HeartLocket");
        Food spiderdonut = new Food(12, "SpiderDonut");
        Food spidercider = new Food(24, "SpiderCider");
        Weapon emptygun = new Weapon(12, "EmptyGun");
        Food monstercandy = new Food(10, "MnstrCndy");




        items.put("mnstrcndy", monstercandy);
        items.put("toyknife", toyknife);
        items.put("bandage", bandage);
        items.put("stick", stick);
        items.put("toughglove", tuffglove);
        items.put("bandanna", bandanna);
        items.put("bicecream", nicecream);
        items.put("cinnabun", cinnabun);
        items.put("oldtutu", tutu);
        items.put("balletshoes", balletshoes);
        items.put("hotdog", hotdog);
        items.put("hotcat", hotcat);
        items.put("locket", locket);
        items.put("spiderdonut", spiderdonut);
        items.put("spidercider", spidercider);
        items.put("emptygun", emptygun);
    }
}
