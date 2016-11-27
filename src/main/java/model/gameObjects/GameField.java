package model.gameObjects;

import model.gameInfo.GameConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by User on 10.10.2016.
 */
public class GameField {
    @NotNull
    private final Logger log = LogManager.getLogger(GameField.class);
    public final int width;
    public final int height;
    @NotNull
    private final HashSet<SmallFood> foods = new HashSet<>();
    @NotNull
    private final List<Virus> viruses = new ArrayList<>();

    public GameField() {
        this.width = GameConstants.FIELD_WIDTH;
        this.height = GameConstants.FIELD_HEIGHT;
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @NotNull
    public HashSet<SmallFood> getFoods() {
        return foods;
    }

    @NotNull
    public List<Virus> getViruses() {
        return viruses;
    }
}
