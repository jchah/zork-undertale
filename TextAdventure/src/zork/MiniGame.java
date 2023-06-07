package zork;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class MiniGame {
    private final int GAME_TIME = 10; // s
    public static AtomicInteger timer = new AtomicInteger(0);
    private PrintStream out;
    private final String[][] area = new String[12][12];
    private int rank = area.length / 2;
    private int file = area[0].length / 2;
    public static MiniGame miniGame = new MiniGame();
    String data = Game.encodeToString("❤");
    private int totalDamage;
    private int hitDamage;
    private String symbol;
    private int tickCount;


    public MiniGame() {
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
        if (area[rank][file].equals(symbol)) {
            totalDamage += hitDamage;
        }
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

    private void resetArea() {
        for (int i = 0; i < area.length; i++) {
            for (int j = 0; j < area[0].length; j++) {
                area[i][j] = "-";
            }
        }
        rank = area.length / 2;
        file = area[0].length / 2;
        area[rank][file] = data;
    }



    public int play(Monster monster) {
        hitDamage = Math.max(1, monster.getAtk() - Game.game.getPlayer().getDef());
        int TICK_DELAY = 500; //ms
        double seconds = TICK_DELAY / 1000.0;
        int temp = (int) (GAME_TIME / seconds) + 1;
        String[] playerLocationHistory = new String[temp];
        switch (monster.getName().toLowerCase()) {
            case "froggit" -> symbol = "\uD80C\uDD8F";
            case "whimsun" -> symbol = "\uD83D\uDC1D";
            case "vegetoid" -> symbol = "\uD83E\uDD66";
            case "loox" -> symbol = "\uD83D\uDC7D";
            case "moldsmal" -> symbol = "\uD83D\uDCA9";
            case "aaron" -> symbol = "\uD83D\uDCAA";
            case "woshua" -> symbol = "\uD83E\uDDFC";
            case "knight knight" -> symbol = "♞";
            case "final froggit" -> symbol = "✨";
            case "greater dog" -> symbol = "\uD83D\uDC15";
            case "ice cap" -> symbol = "\uD83E\uDDCA";
            case "snowdrake" -> symbol = "❄";
            case "flowey" -> {
                symbol = "🌻";
                hitDamage = 0;
            }

        }
        totalDamage = 0;
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

        int r;
        int f;
        double randR;
        double randF;
        int lastY;
        int lastX;

        while (true) {
            if (MiniGame.timer.get() >= GAME_TIME) {
                MiniGame.timer.set(0);
                tickCount = 0;
                break;
            }

            if (tickCount > 0) {
                String[] locationParts = playerLocationHistory[tickCount - 1].split("~");
                lastX = Integer.parseInt(locationParts[1]);
                lastY = Integer.parseInt(locationParts[0]);

                randR = Math.random();
                randF = Math.random();

                if (randR < 0.7) {
                    r = lastY;
                } else {
                    r = (int) (Math.random() * area.length);
                }

                if (randF < 0.7) {
                    f = lastX;
                } else {
                    f = (int) (Math.random() * area[0].length);
                }

                if (r == rank && f == file) {
                    totalDamage += hitDamage;
                } else {
                    area[r][f] = symbol;
                }
            }

            playerLocationHistory[tickCount] = rank + "~" + file;
            printArea();

            Game.sleep(TICK_DELAY);
            tickCount++;
        }

        return totalDamage;

    }
}