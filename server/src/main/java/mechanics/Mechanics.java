package mechanics;

import main.ApplicationContext;
import main.Service;
import messageSystem.Message;
import messageSystem.MessageSystem;
import messageSystem.messages.ReplicateLeaderboardMsg;
import messageSystem.messages.ReplicateMsg;
import model.gameInfo.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ticker.Tickable;
import ticker.Ticker;

/**
 * Created by User on 28.11.2016.
 */
public class Mechanics extends Service implements Tickable {
    @NotNull
    private final static Logger log = LogManager.getLogger(Mechanics.class);

    public Mechanics() {
        super("mechanics");
    }

    @Override
    public void run() {
        log.info(getAddress() + " started");
        Ticker ticker = new Ticker(this, 20);
        ticker.loop();
    }

    @Override
    public void tick(long elapsedNanos) {
        /*try {
            //Thread.sleep(10);
        } catch (InterruptedException e) {
            log.error(e);
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }*/

        log.info("Start replication");
        @NotNull MessageSystem messageSystem = ApplicationContext.get(MessageSystem.class);
        Message message = new ReplicateMsg(this.getAddress());
        messageSystem.sendMessage(message);
        message = new ReplicateLeaderboardMsg(this.getAddress());
        messageSystem.sendMessage(message);

        //execute all messages from queue
        messageSystem.executeForService(this);
    }

    public boolean ejectMass(Player player){
        log.info(player + " eject mass");
        return true;
    }

    public boolean split(Player player){
        log.info(player + " split");
        return true;
    }
}
