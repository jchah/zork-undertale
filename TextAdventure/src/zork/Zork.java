package zork;

public class Zork {
  public static void main(String[] args) {
    KeyListener.keyListener.startListening();
    Game.game.play();
    KeyListener.keyListener.stopListening();
  }
}
