package zork;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

public class MiniGame {
    private final int GAME_TIME;
    public static AtomicInteger timer = new AtomicInteger(0);
    private PrintStream out;
    private final String[][] area = new String[12][12];
    private int rank = area.length / 2;
    private int file = area[0].length / 2;
    public static MiniGame miniGame = new MiniGame();
    String data = Game.encodeToString("❤");

    public MiniGame() {

        if (Game.game.testMode)
            GAME_TIME = 20;
        else GAME_TIME = 10;
        try {
            out = new PrintStream(System.out, true, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void printArea() {
        clearPrint();
        for (String[] row : area) {
            for (String col : row) {
                out.print(col + "   ");
            }
            out.println();
        }
    }

    private void move(int newRow, int newCol) {
        area[rank][file] = "-";  // Mark current position as empty
        rank = newRow;  // Update the new row position
        file = newCol;  // Update the new column position
        area[rank][file] = data;  // Mark the new position with the data
    }

    public void goUp() {
        if (rank > 0)
            move(rank - 1, file);
    }

    public void goDown() {
        if (rank < area.length - 1)
            move(rank + 1, file);
    }

    public void goLeft() {
        if (file > 0)
            move(rank, file - 1);
    }

    public void goRight() {
        if (file < area[0].length - 1)
            move(rank, file + 1);
    }

    private void clearPrint() {
        for (int i = 0; i < 10; ++i)
            out.println();
    }

    private String getPlayerLocation() {
        for (int r = 0; r < area.length; r++) {
            for (int f = 0; f < area[0].length; f++) {
                if (area[r][f].equals("❤"))
                    return r + "~" + f;
            }
        }
        return null;
    }

    private void resetArea() {
        for (int i = 0; i < area.length; i++) {
            for (int j = 0; j < area[0].length; j++) {
                area[i][j] = "-";
            }
        }
        area[rank][file] = data;
    }


    public void play(Monster monster) {
        Thread timer = new Thread(() -> {
            while (true) {
                Game.sleep(1000);
                MiniGame.timer.incrementAndGet();
                // This fixes the timer for some reason
                Game.sleep(1);
                if (MiniGame.timer.get() == GAME_TIME) {
                    return;
                }
            }
        });
        resetArea();
        printArea();
        timer.start();
        int level = 1;
        while (true) {
            if (monster.getName().equals("froggit")) {
                for (int r = 0; r < area.length; r++) {
                    if (r % 2 == 0)
                        printArea();
                        Game.sleep(500);

                    for (int f = 0; f < area[0].length; f++) {

                        if (f == area[0].length - 1) {
                            level++;
                            resetArea();
                        }
                        if (level == 1)
                            if (r % 2 == 0)
                                area[f][r] = "\uD80C\uDD8F";
                        if (level == 2)
                            if (r % 2 == 0)
                                area[r][f] = "\uD80C\uDD8F";
                    }
                    if (MiniGame.timer.get() >= GAME_TIME) {
                        MiniGame.timer.set(0);
                        break;
                    }
                }
            }
        }
    }
}
