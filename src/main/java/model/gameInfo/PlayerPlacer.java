package model.gameInfo;

import org.jetbrains.annotations.NotNull;

/**
 * @author apomosov
 */
public interface PlayerPlacer {
  void place(@NotNull Player player);
}
