package server.authInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by User on 20.10.2016.
 */
public class Token {

    private Long number;
    private static List<Long> existingTokens;
    private static List<Token> existingTokenObjects;

    static {
        existingTokens = new ArrayList<>();
    }

    public Token(){
        Long generated;
        do{
            generated = generateToken();
        } while(existingTokens.contains(generated));
        existingTokens.add(generated);
        number = generated;
        existingTokenObjects.add(this);
    }

    private Long generateToken(){
        return ThreadLocalRandom.current().nextLong();
    }


    public Long getToken() {
        return number;
    }

    public static boolean isTokenExist(Long token){
        return existingTokens.contains(token);
    }

    public static Token getTokenObjectByNumber(Long token){
        for(Token curToken: existingTokenObjects){
            if(curToken.number.equals(token) ){
                return curToken;
            }
        }
        return null;
    }

}
