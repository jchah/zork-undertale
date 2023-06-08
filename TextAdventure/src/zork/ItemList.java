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
        Food butterscotchpie = new Food(9999, "BScotchPie");
        Food quiche = new Food(34, "AbandonedQuiche");
        Food snowmanpiece = new Food(5, "SnowmanPiece");
        Armor cowboyhat = new Armor(5, "CowboyHat");
        Food crabapple = new Food(10, "CrabApple");

        items.put("Crab Apple", crabapple);
        items.put("Cowboy Hat", cowboyhat);
        items.put("Snowman Piece", snowmanpiece);
        items.put("Monster Candy", monstercandy);
        items.put("Toy Knife", toyknife);
        items.put("Bandage", bandage);
        items.put("Stick", stick);
        items.put("Tough Glove", tuffglove);
        items.put("Bandanna", bandanna);
        items.put("Nice Cream", nicecream);
        items.put("Cinnabun", cinnabun);
        items.put("Old Tutu", tutu);
        items.put("Ballet Shoes", balletshoes);
        items.put("Hotdog", hotdog);
        items.put("Hotcat", hotcat);
        items.put("Heart Locket", locket);
        items.put("Spider Donut", spiderdonut);
        items.put("Spider Cider", spidercider);
        items.put("Empty Gun", emptygun);
        items.put("Butterscotch Pie", butterscotchpie);
        items.put("Abandoned Quiche", quiche);
    }
}
