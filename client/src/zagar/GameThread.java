package zagar;

public class GameThread extends Thread implements Runnable {
  public GameThread() {
    super("game");
  }

  @Override
  public void run() {
    while (true) {
      long preTickTime = System.currentTimeMillis();
      Main.updateGame();
      if (System.currentTimeMillis() % 100 == 0) {
        if((System.currentTimeMillis() - preTickTime)!=0) {
          Game.fps = 1000 / (System.currentTimeMillis() - preTickTime);
        } else { Game.fps = 60;}
        Main.frame.setTitle("· zAgar · " + Game.fps + "fps");
      }
    }
  }
}
