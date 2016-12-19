package messageSystem.messages;

import main.ApplicationContext;
import main.Service;
import matchmaker.MatchMaker;
import mechanics.Mechanics;
import messageSystem.Address;
import messageSystem.Message;
import model.gameInfo.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by User on 28.11.2016.
 */
public class EjectMassMsg extends Message {
    private final static Logger log = LogManager.getLogger(EjectMassMsg.class);
    private Player player;
    private double dx;
    private double dy;

    public EjectMassMsg(Address from, Address to, Player player,double dx,double dy){
        super(from, to);
        this.player = player;
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void execute(Service service) {
        ApplicationContext.get(MatchMaker.class).getSessionForPlayer(player).ejectMass(player,dx,dy);
    }

}
