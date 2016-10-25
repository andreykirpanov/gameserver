package gameserver.authInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        existingTokenObjects = new ArrayList<>();
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

    public static Token getTokenObjectByString(String token){
        Long longToken = Long.parseLong(token);
        for(Token curToken: existingTokenObjects){
            if(curToken.number.equals(longToken) ){
                return curToken;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object other){
        if(other == null){
            return false;
        }
        if(other instanceof Token){
            Token otherToken = (Token) other;
            return this.number.equals(otherToken.number);
        }
        return false;
    }

    @Override
    public int hashCode(){
        return (number != null)? number.hashCode(): 0;
    }

    @Override
    public String toString(){
        return number.toString();
    }

}
