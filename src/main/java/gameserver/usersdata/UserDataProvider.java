package gameserver.usersdata;

import gameserver.authInfo.TokenUserStorage;
import gameserver.authInfo.UsersJSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Created by User on 25.10.2016.
 */
@Path("/data")
public class UserDataProvider {
    private static final Logger log = LogManager.getLogger(UserDataProvider.class);

    @POST
    @Path("/users")
    @Produces("application/json")
    public Response getLoggedInUsersList(){
        try{
            log.info("Users JSON requested");
            return Response.ok((new UsersJSON(TokenUserStorage.getUsers())).writeJson()).build();
        } catch (Exception e){
            log.info("Error sending users info");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
