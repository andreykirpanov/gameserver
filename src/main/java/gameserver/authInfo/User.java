package gameserver.authInfo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by User on 20.10.2016.
 */
public class User {
    private String login;
    private String password;
    private static final ObjectMapper mapper = new ObjectMapper();

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String name) {
        this.login= name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String writeJson() throws JsonProcessingException {
        return mapper.writeValueAsString(this);
    }

    private int generateId(){
        return ThreadLocalRandom.current().nextInt();
    }

    @Override
    public boolean equals(Object other){
        if(other == null){
            return false;
        }
        if(other instanceof User){
            User otherUser = (User) other;
            return this.getLogin().equals(otherUser.getLogin()) && this.getPassword().equals(otherUser.getPassword());
        }
        return false;
    }

    @Override
    public int hashCode(){
        return ((login != null)? login.hashCode(): 0) + ((password != null)? password.hashCode(): 0);
    }

    @Override
    public String toString(){
        return String.format("User{login=%s,password=%s}", login, password);
    }

}
