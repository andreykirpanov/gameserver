package gameserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import gameserver.authentification.AutorizationFilter;

/**
 * Created by User on 20.10.2016.
 */
public class GameServer {

    public static final int PORT = 8081;
    private static final Logger log = LogManager.getLogger(GameServer.class);

    private GameServer(){}

    public static void run(){

        Server gameServer = new Server(PORT);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");

        gameServer.setHandler(contextHandler);

        ServletHolder jerseyServlet = contextHandler.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.packages",
                "gameserver"
        );

        jerseyServlet.setInitParameter(
                "com.sun.jersey.spi.container.ContainerRequestFilters",
                AutorizationFilter.class.getCanonicalName()
        );

        try {
            gameServer.start();
            gameServer.join();

        }catch (Exception e){
            log.info("Error creating server");
        }
        finally {
            gameServer.destroy();
        }

    }

    public static void main(String[] args) {
        GameServer.run();
    }
}
