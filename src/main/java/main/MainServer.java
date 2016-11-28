package main;

import accountServer.AccountServer;
import clientConnection.ClientConnectionServer;
import clientConnection.ClientConnections;
import matchmaker.MatchMaker;
import matchmaker.MatchMakerImpl;
import mechanics.Mechanics;
import messageSystem.MessageSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by User on 26.11.2016.
 */
public class MainServer  {

    private final static Logger log = LogManager.getLogger(MainServer.class);

    public static void main(String[] args) {
        new MainServer().start();
    }

    public void start(){
        log.info("main server start");

        MessageSystem messageSystem = new MessageSystem();
        ApplicationContext.put(MessageSystem.class, messageSystem);
        ApplicationContext.put(MatchMaker.class, new MatchMakerImpl());
        ApplicationContext.put(ClientConnections.class, new ClientConnections());

        messageSystem.registerService(AccountServer.class, new AccountServer(8081));
        messageSystem.registerService(ClientConnectionServer.class, new ClientConnectionServer(7000));
        messageSystem.registerService(Mechanics.class, new Mechanics());
        messageSystem.getServices().forEach(Service::start);

    }
}
