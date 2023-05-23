package zork;

import java.io.IOException;
import java.util.Scanner;

public class AttackMeterGame {
    private volatile int attackValue;
    private volatile boolean terminateInput;
    String[] attackMeter = "[--------o--------]".split("");
    final int centerIndex = attackMeter.length / 2;
    Scanner in = new Scanner(System.in);

    public int playGame(Monster monster) {
        attackValue = 0;
        terminateInput = false;
        Thread inputThread = new Thread(this::waitForEnter);
        inputThread.start();
        giveAttackMeter();

        try {
            inputThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (attackValue == 0)
            System.out.println("\033[3m Miss!\033[0m");
        return Game.game.getPlayer().calcDamage(monster, attackValue);
    }

    private void giveAttackMeter() {
        for (int i = attackMeter.length - 2; i >= 0; i--) {

            if (attackValue != 0) {
                break;
            }

            if (i == 0) {
                terminateInput = true;
                break;
            }

            for (int j = 0; j < attackMeter.length; j++)
                System.out.print("\b");

            attackMeter[i] = "|";

            for (String c : attackMeter) {
                System.out.print(c);
            }

            Game.sleep(100);

            if (i == attackMeter.length / 2)
                attackMeter[attackMeter.length / 2] = "o";
            else
                attackMeter[i] = "-";
        }
    }

    private void waitForEnter() {
        try {
            while (!terminateInput) {
                if (System.in.available() > 0) {
                    in.nextLine();
                    for (int i = 1; i < attackMeter.length - 1; i++) {
                        if (attackMeter[i].equals("|")) {
                            double attackBoost = i == centerIndex ? 1.5 : 1;
                            attackValue = (int) ((centerIndex - Math.abs(centerIndex - i)) * attackBoost);
                        }
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}