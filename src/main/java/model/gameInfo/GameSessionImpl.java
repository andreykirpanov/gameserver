package model.gameInfo;

import model.gameInfo.utils.PlayerPlacer;
import model.gameInfo.utils.VirusGenerator;
import model.gameInfo.utils.FoodGenerator;
import model.gameObjects.GameField;
import model.gameInfo.utils.SequentialIDGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author apomosov
 */
public class GameSessionImpl implements GameSession {
    private static final SequentialIDGenerator idGenerator = new SequentialIDGenerator();
    private final int id = idGenerator.next();
    @NotNull
    private final GameField field = new GameField();
    @NotNull
    private final List<Player> players = new ArrayList<>();
    @NotNull
    private final FoodGenerator foodGenerator;
    @NotNull
    private final PlayerPlacer playerPlacer;
    @NotNull
    private final VirusGenerator virusGenerator;

    public GameSessionImpl(@NotNull FoodGenerator foodGenerator, @NotNull PlayerPlacer playerPlacer, @NotNull VirusGenerator virusGenerator) {
        this.foodGenerator = foodGenerator;
        this.playerPlacer = playerPlacer;
        this.virusGenerator = virusGenerator;
        virusGenerator.generate();
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
