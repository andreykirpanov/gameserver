package gameserver.authentification;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import gameserver.authInfo.Token;
import gameserver.authInfo.TokenUserStorage;
import gameserver.authInfo.User;
import gameserver.authInfo.UsersJSON;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by User on 20.10.2016.
 */
@Path("/auth")
public class Authentification {

    private static User autorizedUser;
    private static final Logger log = LogManager.getLogger(Authentification.class);
    private static CopyOnWriteArrayList<User> registeredUsers;

    static {
        registeredUsers = new CopyOnWriteArrayList<>();
    }

    public static void setCurrentUser(User user) {
        autorizedUser = user;
    }

    @POST
    @Path("/register")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/plain")
    public Response register(@FormParam("user")String login, @FormParam("password")String password ){
        try {
            if (login == null || password == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            User currentUser = new User(login, password);
            if (registeredUsers.contains(currentUser)) {
                log.info("User '{}' already exists", currentUser);
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
            registeredUsers.add(currentUser);
            log.info("New user '{}' registered", currentUser);
            return Response.ok("User " + currentUser + " registered").build();
        } catch (Exception e){
            log.info("Error register user.");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }


    @POST
    @Path("/login")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/plain")
    public Response login(@FormParam("user")String login, @FormParam("password")String password ){
        try {
            if (login == null || password == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            User currentUser = new User(login, password);
            if (!validatePassword(currentUser)) {
                log.info("Wrong password for user {}", currentUser);
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            if(!TokenUserStorage.containsUser(currentUser)){
                TokenUserStorage.add(new Token(), currentUser);
            } else{
                log.info("User '{}' already logged in.", currentUser);
            }
            log.info("User '{}' logged in.", currentUser);
            return Response.ok(TokenUserStorage.getTokenByUser(currentUser).getToken()).build();
        } catch (Exception e){
            log.info("Error login user.");
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    private boolean validatePassword(User user){
        return registeredUsers.contains(user);
    }

    public static void validateToken(String token) throws Exception{
        Long longToken = Long.parseLong(token);
        if(Token.isTokenExist(longToken)){
            log.info("Correct token from '{}'", longToken);
        } else{
            throw new Exception("Token validation exception");
        }

    }

    @Autorized
    @POST
    @Path("/logout")
    @Produces("text/plain")
    public Response logout(){
        try{
            TokenUserStorage.delete(autorizedUser);
            return Response.ok("User logged out").build();
        } catch(Exception e) {
            log.info("Error logout user.");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }

    @Autorized
    @POST
    @Path("/profile/name")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/plain")
    public Response changeName(@FormParam("name")String newName){
        try {
            if (newName == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            autorizedUser.setLogin(newName);
            for(User user: registeredUsers){
                if(user.equals(autorizedUser)){
                    user.setLogin(newName);
                }
            }
            log.info("User '{}' has new name", autorizedUser);
            return Response.ok("Name changed").build();
        } catch (Exception e){
            log.info("Error name changing for '{}'", autorizedUser);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("/data/users")
    @Produces("application/json")
    public Response getLoggedInUsersList(){
        try{
            log.info("Users JSON requested", autorizedUser);
            return Response.ok((new UsersJSON(TokenUserStorage.getUsers())).writeJson()).build();
        } catch (Exception e){
            log.info("Error sending users info", autorizedUser);
            return Response.status(Response.Status.BAD_REQUEST).build();
    }
    }
}
