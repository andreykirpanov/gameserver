package clientConnection.handlers;

import clientConnection.JSONHelper.JSONDeserializationException;
import clientConnection.JSONHelper.JSONHelper;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import protocol.CommandEjectMass;

public class PacketHandlerEjectMass {
    public PacketHandlerEjectMass(@NotNull Session session, @NotNull String json) {
        CommandEjectMass commandEjectMass;
        try {
            commandEjectMass = JSONHelper.fromJSON(json, CommandEjectMass.class);
        } catch (JSONDeserializationException e) {
            e.printStackTrace();
            return;
        }
        //TODO
    }
}
