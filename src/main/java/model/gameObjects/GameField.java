package model.gameObjects;

import matchmaker.SinglePlayerMatchMaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Created by User on 10.10.2016.
 */
public class GameField {
    @NotNull
    private final Logger log = LogManager.getLogger(SinglePlayerMatchMaker.class);
    public final int width;
    public final int height;

    public GameField(int width, int height) {
        this.width = width;
        this.height = height;
        if (log.isInfoEnabled()) {
            log.info(toString() + " created");
        }
    }

    @Override
    public String toString() {
        return "GameField{" +
                "width=" + width + " " +
                "height=" + height +
                '}';
    }
}
