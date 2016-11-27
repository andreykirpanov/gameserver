package model.gameObjects;

import model.gameInfo.GameConstants;
import model.gameInfo.Location;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by User on 10.10.2016.
 */

//Игровая сущность, управляемая игроком
public class PlayerCell extends Cell {

    private final long id;

    public PlayerCell(long id, Location location) {
        super(location, GameConstants.DEFAULT_PLAYER_CELL_MASS);
        this.id = id;
    }

    public long getId() {
        return id;
    }


}
