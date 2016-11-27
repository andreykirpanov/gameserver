package clientConnection.handlers;

import clientConnection.ClientConnections;
import clientConnection.JSONHelper.JSONDeserializationException;
import clientConnection.JSONHelper.JSONHelper;
import clientConnection.packets.PacketAuthFail;
import clientConnection.packets.PacketAuthOk;
import main.ApplicationContext;
import matchmaker.MatchMaker;
import model.gameInfo.Player;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import protocol.CommandAuth;

import java.io.IOException;

public class PacketHandlerAuth {
    public PacketHandlerAuth(@NotNull Session session, @NotNull String json) {
        CommandAuth commandAuth;
        try {
            commandAuth = JSONHelper.fromJSON(json, CommandAuth.class);
        } catch (JSONDeserializationException e) {
            e.printStackTrace();
            return;
        }
        if (true//!Authentication.validateToken(commandAuth.getToken())) {
                ){
            try {
                new PacketAuthFail(commandAuth.getLogin(), commandAuth.getToken(), "Invalid user or password").write(session);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Player player = new Player(commandAuth.getLogin(), Player.idGenerator.next());
                ApplicationContext.get(ClientConnections.class).registerConnection(player, session);
                new PacketAuthOk().write(session);
                ApplicationContext.get(MatchMaker.class).joinGame(player);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
