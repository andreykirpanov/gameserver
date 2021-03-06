package model.gameInfo.utils;


import model.gameInfo.Location;
import model.gameInfo.Player;
import model.gameInfo.utils.PlayerPlacer;
import model.gameObjects.GameField;
import model.gameObjects.PlayerCell;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * @author apomosov
 */
public class RandomPlayerPlacer implements PlayerPlacer {
    @NotNull
    private final GameField field;

    public RandomPlayerPlacer(@NotNull GameField field) {
    this.field = field;
  }

   @Override
   public void place(@NotNull Player player) {
       assert(player.getCells().size() == 1);
       Random random = new Random();
       for (PlayerCell playerCell : player.getCells()) {
           Location newLocation = new Location(playerCell.getRadius() + random.nextInt(field.getWidth() - 2 * playerCell.getRadius()),
                playerCell.getRadius() + random.nextInt(field.getHeight() - 2 * playerCell.getRadius()));
           playerCell.setLocation(newLocation);
       }
   }
}
