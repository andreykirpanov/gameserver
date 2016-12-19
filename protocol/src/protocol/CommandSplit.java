package protocol;

import org.jetbrains.annotations.NotNull;

/**
 * @author apomosov
 */
public final class CommandSplit extends Command {
  @NotNull
  public static final String NAME = "split";

  private double dx;
  private double dy;

  public CommandSplit(double dx,double dy) {
    super(NAME);
    this.dx = dx;
    this.dy = dy;
  }

  public double getDx() {
    return dx;
  }

  public double getDy() {
    return dy;
  }
}
