package gameserver.authentification;

import gameserver.authInfo.Token;
import gameserver.authInfo.TokenUserStorage;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by User on 20.10.2016.
 */
@Autorized
@Provider

public class AutorizationFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String authorizationHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        String token = authorizationHeader.substring("Bearer".length()).trim();

        try {
            Authentification.validateToken(token);
            Authentification.setCurrentUser(TokenUserStorage.getUserByToken(
                    Token.getTokenObjectByNumber(Long.parseLong(token))));
        } catch (Exception e) {
            containerRequestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
