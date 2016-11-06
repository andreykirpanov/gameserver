package gameserver.usersdata;

import gameserver.authentification.Authentification;
import model.authInfo.Token;
import model.authInfo.User;
import model.authInfo.UsersJSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 25.10.2016.
 */
@Path("/data")
public class UserDataProvider {
    private static final Logger log = LogManager.getLogger(UserDataProvider.class);

    @GET
    @Path("/users")
    @Produces("application/json")
    public Response getLoggedInUsersList(){
        try{
            log.info("Users JSON requested");
            List<Token> tokens = Authentification.tokenDAO.getAll();
            ArrayList<User> loggedInUsers = new ArrayList<>();
            for(Token token: tokens){
                loggedInUsers.add(Authentification.userDAO.getUserById(token.getUserId()));
            }
            return Response.ok((new UsersJSON(loggedInUsers)).writeJson()).build();
        } catch (Exception e){
            log.info("Error sending users info");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
