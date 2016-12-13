package replication;

import clientConnection.ClientConnections;
import clientConnection.packets.PacketReplicate;
import main.ApplicationContext;
import matchmaker.MatchMaker;
import model.gameInfo.GameSession;
import model.gameInfo.Player;
import model.gameObjects.PlayerCell;
import org.eclipse.jetty.websocket.api.Session;
import protocol.model.Cell;
import protocol.model.Food;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Max on 29.11.2016.
 */
public class DefaultReplication implements Replicator {
    @Override
    public void replicate() {
        for (GameSession gameSession : ApplicationContext.get(MatchMaker.class).getActiveGameSessions()) {
            Food[] food = new Food[0];//TODO food and viruses
            int numberOfCellsInSession = 0;
            for (Player player : gameSession.getPlayers()) {
                numberOfCellsInSession += player.getCells().size();
            }
            Cell[] cells = new Cell[numberOfCellsInSession];
            int i = 0;
            for (Player player : gameSession.getPlayers()) {
                for (PlayerCell playerCell : player.getCells()) {
                    cells[i] = new Cell(playerCell.getId(), player.getId(), false, playerCell.getMass(), playerCell.getLocation().getX(), playerCell.getLocation().getY());
                    i++;
                }
            }
            for (Map.Entry<Player, Session> connection : ApplicationContext.get(ClientConnections.class).getConnections()) {
                if (gameSession.getPlayers().contains(connection.getKey()) && connection.getValue().isOpen()) {
                    try {
                        new PacketReplicate(cells, food).write(connection.getValue());
                    } catch (IOException e) {

                    }
                }
            }
        }
    }
}
