package matchmaker;

import model.gameInfo.GameSession;
import model.gameInfo.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provides (searches or creates) {@link GameSession} for {@link Player}
 *
 * @author Alpi
 */
public interface MatchMaker {
  /**
   * Searches available game session or creates new one
   * @param player player to join the game session
   */
    void joinGame(@NotNull Player player);

  /**
   * @return Currently open game sessions
   */
    @NotNull
    List<GameSession> getActiveGameSessions();

    GameSession getSessionForPlayer(Player player);

    ConcurrentHashMap<Integer,Integer> getPlayerSession();
}
