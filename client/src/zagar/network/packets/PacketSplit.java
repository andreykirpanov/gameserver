package zagar.network.packets;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import protocol.CommandSplit;
import zagar.util.JSONHelper;
import zagar.Game;

public class PacketSplit {
  @NotNull
  private static final Logger log = LogManager.getLogger(">>>");

  private double x;
  private double y;

  public PacketSplit(double x,double y) {
    this.x = x;
    this.y = y;
  }

  public void write() throws IOException {
    String msg = JSONHelper.toJSON(new CommandSplit(x,y));
    log.error("Sending [" + msg + "]");
    Game.socket.session.getRemote().sendString(msg);
  }
}
