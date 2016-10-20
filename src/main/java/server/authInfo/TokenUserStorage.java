package server.authInfo;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by User on 20.10.2016.
 */
public class TokenUserStorage {
    private static ConcurrentHashMap<Token, User> userByToken;
    private static ConcurrentHashMap<User,Token> tokenByUser;

    static {
        userByToken = new ConcurrentHashMap<>();
        tokenByUser = new ConcurrentHashMap<>();
    }

    public void add(Token token, User user){
        userByToken.put(token, user);
        tokenByUser.put(user, token);
    }

    public User getUserByToken(Token token){
        return userByToken.get(token);
    }

    public Token getTokenByUser(User user){
        return tokenByUser.get(user);
    }
}
