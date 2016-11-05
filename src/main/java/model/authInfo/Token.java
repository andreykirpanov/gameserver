package model.authInfo;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by User on 20.10.2016.
 */
@Entity
@Table(name="LoggedInUsers")
public class Token {
    @Id
    private Long number;

    @Temporal(TemporalType.DATE)
    private LocalDateTime issueDate;

    @Transient
    private static List<Long> existingTokens;

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
        issueDate = LocalDateTime.now();
    }

    private Long generateToken(){
        return ThreadLocalRandom.current().nextLong();
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

    //TODO: get/set for date and token

}
