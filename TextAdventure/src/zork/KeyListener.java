package zork;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class KeyListener implements NativeKeyListener {

    public static volatile boolean run = false;
    public static KeyListener keyListener = new KeyListener();
    public void startListening() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
        GlobalScreen.addNativeKeyListener(this);
    }

    public void stopListening() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
        GlobalScreen.removeNativeKeyListener(this);
    }

    public void nativeKeyPressed(NativeKeyEvent event) {
        if(!run) return;
        final int keyPressed = event.getKeyCode();

        Thread t = new Thread(() -> {
            if(keyPressed == 17) {
                MiniGame.miniGame.goUp();
            }
            else if (keyPressed == 30) {
                MiniGame.miniGame.goLeft();
            }

            else if (keyPressed == 31) {
                MiniGame.miniGame.goDown();
            }

            else if (keyPressed == 32) {
                MiniGame.miniGame.goRight();
            }
            else {
                return;
            }
            MiniGame.miniGame.printArea();
        });
        t.start();
    }
}
