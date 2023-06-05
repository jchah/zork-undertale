package zork;

import java.util.ArrayList;
import java.util.HashMap;

public class ActOptions {
    public static HashMap<String, ArrayList<Action>> actOptions = new HashMap<>();

    static {
        Action t;
        ArrayList<Action> actions = new ArrayList<>();
        t = new Action("CHECK", "Life is difficult for this enemy. FROGGIT: \"Ribbit, ribbit \"");
        actions.add(t);
        t = new Action("COMPLIMENT",
                "Froggit didn't understand what you said, but was flattered anyway. FROGGIT: \"(Blushes deeply) Ribbit..",
                true);
        actions.add(t);
        t = new Action("THREAT",
                "Froggit didn't understand what you said, but was scared anyways. FROGGIT: \"Shiver, shiver\"");
        actions.add(t);

        actOptions.put("froggit", actions);

        actions.clear();

        t = new Action("CHECK",
                "Serving Size: 1 Monster Not monitored by the USDA. VEGETOID: \"Farmed locally, very locally\"");
        actions.add(t);
        t = new Action("TALK", "Plants can't talk dummy. VEGETOID: \"Plants can't talk dummy!\"");
        actions.add(t);
        t = new Action("DINNER", "You at your stomach. Vegatoid offers a healthy meal. VEGETOID: \"Eat Your Greens\"",
                true);
        actions.add(t);
        t = new Action("DEVOUR", "You took a bite out of Vegatoid. You recovered 5 HP. ");
        actions.add(t);

        actOptions.put("vegetoid", actions);

        actions.clear();

        t = new Action("CHECK", "This monster is too sensitive to fight... WHUMSUN: \"I'm sorry...\"");
        actions.add(t);
        t = new Action("CONSOLE",
                "halfway through your first word, whimsun busrts into tears. WHIMSUN: \"*sniff, sniff\"", true);
        actions.add(t);
        t = new Action("TERRIROIZE",
                "You raise your arms and wiggle your fingers. Whimsun freaks out!. WHIMSUN: \"I can't handle this...\"");
        actions.add(t);

        actOptions.put("whimsun", actions);

        actions.clear();

        t = new Action("CHECK", "Don't pick on him. Family name: Eyewalker. LOOX: \"Please don't pick on me\"");
        actions.add(t);
        t = new Action("DON'T PICK ON", "LOOX: Finally someone gets it.");
        actions.add(t);
        t = new Action("PICK ON", "LOOX: You litle rude snipe", true);
        actions.add(t);

        actOptions.put("loox", actions);

        actions.clear();

        t = new Action("CHECK", "Greater Dog");
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

        actOptions.put("greaterdog", actions);

        actions.clear();

        t = new Action("CHECK",
                " This teens comedian fights to keep a captive audience. SNOWDRAKE: \" M.. m.. macaroni and \"freeze\" \"");
        actions.add(t);
        t = new Action("HECKLE", "You boo the snowdrake. SNOWDRAKE: \"THIS won't be dunny either!\"");
        actions.add(t);
        t = new Action("LAUGH", "You laugh at Snowdrake's pun. SNOWDRAKE: \"See!? Laughs@ Dad was wrong!\"", true);
        actions.add(t);
        t = new Action("JOKE", "Snowdrake is pleased. SNOWDRAKE: \"Is that s'posed to be funny?\"");
        actions.add(t);

        actOptions.put("snowdrake", actions);

        actions.clear();

        t = new Action("CHECK",
                " This teen wonders why it isn't named 'Ice Hat'. ICECAP: \"Your head looks so.. NAKED!\"");
        actions.add(t);
        t = new Action("IGNORE",
                "You manage to tear your eyes away from Ice Cap's hat. ICECAP: \" HELLO??? My Hat's up here\"");
        actions.add(t);
        t = new Action("STEAL",
                "You tried to steal Ice Cap's hat.... and succeeded! (It melts in your hands...). ICECAP: \"I... i...\"");
        actions.add(t);
        t = new Action("COMPLIMENT", "ICECAP: ENVIOUS? TOO BAD! ");
        actions.add(t);

        actOptions.put("icecap", actions);

        actions.clear();

        t = new Action("CHECK",
                "MOLDSMAL - Stereotypical: curvaceously attractive, but no brains.. MOLDSMAL: \"Sexy wiggle\"");
        actions.add(t);
        t = new Action("FLIRT",
                "You wiggle your hips. Moldsmal wiggles back. What a meaningful conversation!  MOLDSMAL: \"Burble burb...\"");
        actions.add(t);
        t = new Action("IMITATE",
                "You lie immobile with Moldsmal. You feel like you understand ther world a little better... MOLDSMAL: \"Slime Sounds*\"");
        actions.add(t);

        actOptions.put("moldsmal", actions);

        actions.clear();

        t = new Action("CHECk", "This seahorse has a lot of HP (horsepower). AARON: \"CHECK all you want ; )\"");
        actions.add(t);
        t = new Action("FLEX",
                "You flex. Aaron flexes twice as hard. ATTACK increases for you too. AARON: \"Flexing contest? OK. Flex more ;\"");
        actions.add(t);
        t = new Action("SHOO", "You tell Aaron to go away. AARON: \"Wow! Spunky! Love it;\"");
        actions.add(t);

        actOptions.put("aaron", actions);

        actions.clear();

        t = new Action("CHECK", "This humble germophobe seeks to cleanse the whole world. WOSHUA: \"Wosh face\"");
        actions.add(t);
        t = new Action("TOUCH", "You reach out. Woshua recoils from your touch. WOSHUA: \"Yuck!\"");
        actions.add(t);
        t = new Action("CLEAN", "You ask Woshua to clean you. It hops around excitidely. \"Green means clean\"");
        actions.add(t);
        t = new Action("JOKE",
                "You tell a joke about a kid who ate a pie with their bare hands. Woshua's power neutralized. \"NO. THAT JOKE'S TOO... DIRTY\"");
        actions.add(t);

        actOptions.put("woshua", actions);

        actions.clear();

        t = new Action("CHECK", "It's future looks brighter and brighter. FINALFROGGIT: \"Skip, Jump\"");
        actions.add(t);
        t = new Action("THREATEN",
                "You threaten the Final froggit. It understood you perfectly. Its DEFENCE dropped. FINALFROGGIT: \"Shudder shudder\"");
        actions.add(t);
        t = new Action("COMPLIMENT",
                "You compliment final froggit. It understood you perfefctly. Its ATTACK dropped. FINALFROGGIT: \"Nod, nod\"");
        actions.add(t);
        t = new Action("MYSTIFY",
                "You did something mysterious. Final Froggit recognizes it has more to learn from this world. \"(Thoughful croak)\"");
        actions.add(t);

        actOptions.put("finalfroggit", actions);

        actions.clear();

        t = new Action("CHECK", "This megaton mercenary wields the good morningstar. KNIGHTKNIGHT: \"Adieu\"");
        actions.add(t);
        t = new Action("SING",
                "You sing shyren's song. Knight Knight starts to look sleepy... KNIGHTKNIGHT: \"Close your eyes\"");
        actions.add(t);
        t = new Action("TALK", "You ask Knight Knight about her day. There's no response. \"...\"");
        actions.add(t);

        actOptions.put("knightknight", actions);

        actions.clear();

        t = new Action("CHECK", "If she invites you to her parlor, excuse yourself. \"Don't look so blue, my deary\"");
        actions.add(t);
        t = new Action("STRUGGLE",
                "You struggle to escape the web. Muffet covers her mouth and giggles at you. \"Why so pale, you should be proud\"");
        actions.add(t);

        actOptions.put("muffet", actions);

        actions.clear();

        t = new Action("CHECK", "He likes to say: \"Nyeh heh heh!\"");
        actions.add(t);
        t = new Action("INSULT", "You INSULT, but to no avail. Seems ACTing won't escalate this battle...");
        actions.add(t);
        t = new Action("FLIRT",
                "   PAPYRUS: \"What! Fl-Flirting! So you finally reveal your ultimate feelings. w-well! im a skelton with very high standards!\"");
        actions.add(t);

        actOptions.put("papyrus", actions);

        actions.clear();

    }
}
