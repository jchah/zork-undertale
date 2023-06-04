package zork;

import java.util.ArrayList;
import java.util.HashMap;

public class ActOptions {
    public static HashMap<String, ArrayList<Action>> actOptions = new HashMap<>();

    static {
        Action t;
        ArrayList<Action> actions = new ArrayList<>();
        t = new Action("CHECK", "Life is difficult for this enemy.");
        actions.add(t);
        t = new Action("COMPLIMENT", "Froggit didn't understand what you said, but was flattered anyway.", true);
        actions.add(t);
        t = new Action("THREAT", "Froggit didn't understand what you said, but was scared anyways");
        actions.add(t);

        actOptions.put("froggit", actions);
        /*
         * t = new Action("", "");
         * actions.add(t);
         * t = new Action("", "");
         * actions.add(t);
         * t = new Action("", "");
         * actions.add(t);
         * 
         * actOptions.put("", actions);
         * 
         * actions.clear();
         * 
         * 
         */

        actions.clear();

        t = new Action("TALK", "Plants can't talk dummy");
        actions.add(t);
        t = new Action("DINNER", "You at your stomach. Vegatoid offers a healthy meal.", true);
        actions.add(t);
        t = new Action("DEVOUR", "You took a bite out of Vegatoid. You recovered 5 HP");
        actions.add(t);

        actOptions.put("vegetoid", actions);

        actions.clear();

        t = new Action("CHECK", "WHIMSUN - ATK 5 DEF 0. This monster is too sensitive to fight...");
        actions.add(t);
        t = new Action("CONSOLE", "halfway through your first word, whimsun busrts into tears", true);
        actions.add(t);
        t = new Action("TERRIROIZE", "You raise your arms and wiggle your fingers. Whimsun freaks out!");
        actions.add(t);

        actOptions.put("whimsun", actions);

        actions.clear();

        t = new Action("CHECK", "LOOX - ATK 6 DEF 6. Don't pick on him. Family name: Eyewalker");
        actions.add(t);
        t = new Action("DON'T PICK ON", "Finally someone gets it");
        actions.add(t);
        t = new Action("PICK ON", "You litle rude snipe", true);
        actions.add(t);

        actOptions.put("loox", actions);

        actions.clear();

        t = new Action("CHECK", "Greater Dog - ATK 15 DEF - 8");
        actions.add(t);
        t = new Action("BECKON", "You call the greater dog. It bounds towards you, flecking slobber into your face.");
        actions.add(t);
        t = new Action("IGNORE", "Greater dog inches closer");
        actions.add(t);
        t = new Action("PET",
                "Greater dog curls up in your lap as it is pet by you. It gets so comforatble it falls asleep... zzzzzz THen it wakes up! It's so excited!",
                true);
        actions.add(t);
        t = new Action("PLAY",
                "You make a snowball and throw it for the dog to fetch. It splats on the ground, Greater dog picks up all the snow in the area and brings it to you. Now dog is very tired... It rests its head on you");
        actions.add(t);

        actOptions.put("greater dog", actions);

        actions.clear();

        t = new Action("CHECK", "SNOWDRAKE - ATK 6 DEF 2. This teens comedian fights to keep a captive audience");
        actions.add(t);
        t = new Action("HECKLE", "You boo the snowdrake");
        actions.add(t);
        t = new Action("LAUGH", "You laugh at Snowdrake's pun   ", true);
        actions.add(t);
        t = new Action("JOKE", "Snowdrake is pleased");
        actions.add(t);

        actOptions.put("snowdrake", actions);

        actions.clear();

        t = new Action("CHECK", "ICE CAP - ATK 7 DEF 2. This teen wonders why it isn't named 'Ice Hat'");
        actions.add(t);
        t = new Action("IGNORE", "You manage to tear your eyes away from Ice Cap's hat");
        actions.add(t);
        t = new Action("STEAL", "You tried to steal Ice Cap's hat.... and succeeded! (It melts in your hands...)");
        actions.add(t);
        t = new Action("COMPLIMENT", "ENVIOUS? TOO BAD!");
        actions.add(t);

        actOptions.put("ice cap", actions);

        actions.clear();

    }
}
