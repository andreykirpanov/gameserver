package clientConnection.handlers;

import clientConnection.JSONHelper.JSONDeserializationException;
import clientConnection.JSONHelper.JSONHelper;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import protocol.CommandSplit;

public class PacketHandlerSplit {
    public PacketHandlerSplit(@NotNull Session session, @NotNull String json) {
        CommandSplit commandSplit;
        try {
            commandSplit = JSONHelper.fromJSON(json, CommandSplit.class);
        } catch (JSONDeserializationException e) {
            e.printStackTrace();
            return;
        }
        //TODO
    }
}
