package zork;

public class Zork {
  public static void main(String[] args) {
    Puzzles.playConnectFour();
    System.exit(0);
    KeyListener.keyListener.startListening();
    Game.game.play();
    KeyListener.keyListener.stopListening();
  }
}
