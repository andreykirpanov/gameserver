package matchmaker;

import model.gameInfo.*;
import model.gameInfo.utils.RandomPlayerPlacer;
import model.gameInfo.utils.UniformVirusGenerator;
import model.gameObjects.GameField;
import model.gameInfo.utils.UniformFoodGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static model.gameInfo.GameConstants.MAX_PLAYERS_IN_SESSION;

/**
 * Creates {@link GameSession} for single player
 *
 * @author Alpi
 */
public class MatchMakerImpl implements MatchMaker {
    @NotNull
    private final Logger log = LogManager.getLogger(MatchMakerImpl.class);
    @NotNull
    private final List<GameSession> activeGameSessions = new ArrayList<>();
    @NotNull
    private final ConcurrentHashMap<Integer,Integer> PlayerSession = new ConcurrentHashMap<>();

    /**
     * Creates new GameSession for single player
     *
     * @param player single player
     */
    @Override
    public void joinGame(@NotNull Player player) {
        if(activeGameSessions.isEmpty()) {
            GameSession newGameSession = createNewGame();
            activeGameSessions.add(newGameSession);
            newGameSession.join(player);
            PlayerSession.put(player.getId(),0);
            log.info(player + " joined " + newGameSession);
        }
        else {
            GameSession currentGameSession = activeGameSessions.get(activeGameSessions.size() - 1);
            if(currentGameSession.getPlayers().size() < MAX_PLAYERS_IN_SESSION ) {
                currentGameSession.join(player);
                PlayerSession.put(player.getId(),activeGameSessions.indexOf(currentGameSession));
                log.info(player + " joined " + currentGameSession);
            }
            else{
                GameSession newGameSession = createNewGame();
                activeGameSessions.add(newGameSession);
                newGameSession.join(player);
                PlayerSession.put(player.getId(),activeGameSessions.indexOf(newGameSession));
                log.info(player + " joined " + newGameSession);
            }
        }
    }

    @NotNull
    public List<GameSession> getActiveGameSessions() {
        return new ArrayList<>(activeGameSessions);
    }


    private @NotNull GameSession createNewGame() {
        GameField field = new GameField();
        //TODO
        UniformFoodGenerator foodGenerator = new UniformFoodGenerator(field, GameConstants.FOOD_PER_SECOND_GENERATION,
                GameConstants.MAX_FOOD_ON_FIELD);
        return new GameSessionImpl(foodGenerator, new RandomPlayerPlacer(field), new UniformVirusGenerator(field, GameConstants.NUMBER_OF_VIRUSES),
                field);
    }

    @Override
    public GameSession getSessionForPlayer(Player player){
        for(GameSession session: activeGameSessions){
            if(session.getPlayers().contains(player)){
                return session;
            }
        }
        return null;
    }

    @Override
    public ConcurrentHashMap<Integer,Integer> getPlayerSession(){return PlayerSession;}
}
