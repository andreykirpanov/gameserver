package main;

import accountServer.AccountServer;
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

        messageSystem.registerService(AccountServer.class, new AccountServer(8081));
        messageSystem.getServices().forEach(Service::start);

    }
}
