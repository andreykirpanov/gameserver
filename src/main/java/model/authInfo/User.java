package model.authInfo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by User on 20.10.2016.
 */
@Entity
@Table(name="User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String login;
    private String password;

    @Temporal(TemporalType.DATE)
    private LocalDateTime registrationDate;

    private String email;

    @Transient
    private static final ObjectMapper mapper = new ObjectMapper();

    public User(String login, String password, String email) {
        this.login = login;
        this.password = password;
        if(email == null){
            this.email = "";
        } else if(validateEmail(email)){
            this.email = email;
        }
        registrationDate = LocalDateTime.now();
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

    private boolean validateEmail(String email){
        return true;
        //TODO: implement this
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String name) {
        this.login= name;
    }

    private String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public int getId(){return id;}

    private void setId(int id){ this.id = id; }

    private LocalDateTime getRegistrationDate(){return registrationDate;}

    private void setRegistrationDate(LocalDateTime date){this.registrationDate = date;}

    //TODO: add get/set for email

}
