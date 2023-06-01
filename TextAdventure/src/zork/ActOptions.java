package zork;

import java.util.ArrayList;
import java.util.HashMap;

public class ActOptions {
    public static HashMap<String, ArrayList<Action>> actOptions = new HashMap<String, ArrayList<Action>>();

    static {
        Action t;
        ArrayList<Action> actions = new ArrayList<>();
        t = new Action("CHECK", "Life is difficult for this enemy.");
        actions.add(t);
        t = new Action("COMPLIMENT", "Froggit didn't understand what you said, but was flattered anyway.", true);
        actions.add(t);

        actOptions.put("froggit", actions);
    }
}
