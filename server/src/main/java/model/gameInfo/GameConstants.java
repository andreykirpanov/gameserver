package model.gameInfo;

/**
 * Global static constants
 *
 * @author apomosov
 */
public interface GameConstants {
    int MAX_PLAYERS_IN_SESSION = 10;
    int FIELD_WIDTH = 2000;
    int FIELD_HEIGHT = 2000;
    int FOOD_MASS = 10;
    int DEFAULT_PLAYER_CELL_MASS = 40;
    int VIRUS_MASS = 100;
    int FOOD_PER_SECOND_GENERATION = 10;
    int MAX_FOOD_ON_FIELD = 50;
    int NUMBER_OF_VIRUSES = 10;
    int SERVER_FPS = 30;
    int EJECT_LOSE_MASS = 0;
    int EJECT_PIECE_MASS = (int)0.72*18;
    int MIN_MASS_FOR_ABILITIES = 0;
    double EJECTED_DISTANCE = 20;
    double EJECTED_SPEED = 333*Math.pow(DEFAULT_PLAYER_CELL_MASS, -1/3);
    /*int EJECT_LOSE_MASS = 18;
    int EJECT_PIECE_MASS = (int)0.72*18;
    int MIN_MASS_FOR_ABILITIES = 35;*/
    int SCALE = 10;
}
