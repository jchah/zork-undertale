package zork;

import java.util.ArrayList;
import java.util.HashMap;

public class ActOptions {
    public static HashMap<String, ArrayList<Action>> actOptions = new HashMap<String, ArrayList<Action>>();

    static {
        ArrayList<Action> actions = new ArrayList<Action>();
        Action t = new Action("CHECK", "Life is difficult for this enemy.");
        actions.add(t);

        actOptions.put("froggit", actions);
    }
}
