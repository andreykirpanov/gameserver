import model.gameInfo.GameSessionImpl;
import model.gameInfo.Location;
import model.gameObjects.Cell;
import model.gameObjects.PlayerCell;
import org.junit.Test;

/**
 * Created by User on 19.12.2016.
 */
public class MechanicsTest {
    @Test
    public void testEatCell(){
        Cell small = new PlayerCell(0, new Location(10, 10), "a");
        Cell big = new PlayerCell(1, new Location(30, 30), "b");
        big.setMass(80);
        System.out.println(small.getRadius() + "  " + big.getRadius());
        GameSessionImpl.eatCell(small, big);
        System.out.println(small.getMass() + "  " + big.getMass());
    }
}
