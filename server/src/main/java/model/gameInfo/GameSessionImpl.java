package model.gameInfo;

import model.gameInfo.utils.*;
import model.gameObjects.Food;
import model.gameObjects.GameField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static model.gameInfo.GameConstants.MAX_FOOD_ON_FIELD;

/**
 * @author apomosov
 */
public class GameSessionImpl implements GameSession {
    private final static Logger log = LogManager.getLogger(GameSessionImpl.class);
    private static final SequentialIDGenerator idGenerator = new SequentialIDGenerator();
    private final long id = idGenerator.next();
    @NotNull
    private final GameField field;
    @NotNull
    private final List<Player> players = new ArrayList<>();
    @NotNull
    private final FoodGenerator foodGenerator;
    @NotNull
    private final PlayerPlacer playerPlacer;
    @NotNull
    private final VirusGenerator virusGenerator;

    public GameSessionImpl(@NotNull FoodGenerator foodGenerator, @NotNull PlayerPlacer playerPlacer, @NotNull VirusGenerator virusGenerator,
                           @NotNull GameField gameField) {
        this.foodGenerator = foodGenerator;
        this.playerPlacer = playerPlacer;
        this.virusGenerator = virusGenerator;
        field = gameField;
        //virusGenerator.generate();
        Thread foodGenerationTask = new Thread(foodGenerator);
        foodGenerationTask.start();
    }

    @Override
    public void join(@NotNull Player player) {
        players.add(player);
        this.playerPlacer.place(player);
    }

    @Override
    public void leave(@NotNull Player player) {
        players.remove(player);
    }

    @Override
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    @Override
    public GameField getField() {
        return field;
    }

    @Override
    public String toString() {
        return "GameSessionImpl{" +
                "id=" + id +
                '}';
    }

}
