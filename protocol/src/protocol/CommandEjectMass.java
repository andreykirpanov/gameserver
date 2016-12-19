package protocol;

import org.jetbrains.annotations.NotNull;

/**
 * @author apomosov
 */
public final class CommandEjectMass extends Command {
  @NotNull
  public static final String NAME = "eject";
    private final double dx;
    private final double dy;

  public CommandEjectMass(double dx,double dy) {
    super(NAME);
    this.dx=dx;
    this.dy=dy;
  }

  public double getDx() {
    return dx;
  }

  public double getDy() {
    return dy;
  }
}
