package zagar.network.handlers;

import protocol.CommandLeaderBoard;
import zagar.util.JSONDeserializationException;
import zagar.util.JSONHelper;
import zagar.Game;
import org.jetbrains.annotations.NotNull;

public class PacketHandlerLeaderBoard {
  public PacketHandlerLeaderBoard(@NotNull String json) {
    CommandLeaderBoard commandLeaderBoard;
    try {
      commandLeaderBoard = JSONHelper.fromJSON(json, CommandLeaderBoard.class);
    } catch (JSONDeserializationException e) {
      e.printStackTrace();
      return;
    }
    for(String score: commandLeaderBoard.getLeaderBoard()){
      String[] splitted = score.split(" : ");
      if(splitted[0].equals(Game.login)){
        Game.score = Integer.parseInt(splitted[1]);
      }
    }
    Game.leaderBoard = commandLeaderBoard.getLeaderBoard();
  }
}
