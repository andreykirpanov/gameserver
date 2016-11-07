package DAOtest;

import com.squareup.okhttp.*;
import gameserver.authentification.Authentification;
import model.authDAO.LB;
import model.authInfo.Leader;
import model.authInfo.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 07.11.2016.
 */
public class LBtest {
    private static final Logger log = LogManager.getLogger(LBtest.class);
    private static final String PROTOCOL = "http";
    private static final String HOST = "127.0.0.1";
    private static final String PORT = "8081";
    private static final String SERVICE_URL = PROTOCOL + "://" + HOST + ":" + PORT;

    @Test
    public void InsertTest() {
        int userid = 247859;
        LB.insert(userid);
        Assert.assertEquals(0, LB.getUserScore(userid));
        LB.deleteUser(userid);
    }

    @Test
    public void UpdateScoreTest() {
        int user = 247859;
        LB.insert(user);
        LB.updateScore(user, 25);
        Assert.assertEquals(25, LB.getUserScore(user));
        LB.deleteUser(user);
    }

    @Test
    public void LeaderRegisterTest(){
        String user="LeaderTest";
        String password="LeaderTest";
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(
                mediaType,
                String.format("user=%s&password=%s", user, password)
        );

        String requestUrl = SERVICE_URL + "/auth/register";
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();
        try {
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            System.out.println(response.isSuccessful());
        } catch (IOException e) {
            //log.warn("Something went wrong in register.", e);
            System.out.println(false);
        }
        User jUser= Authentification.userDAO.getUserByLoginData(user,password);
        Assert.assertEquals(0, LB.getUserScore(jUser.getId()));
        Authentification.userDAO.delete(jUser);
        LB.deleteUser(jUser.getId());
    }

    @Test
    public void getLeaderTest(){
        String testJSON="{ \"LeaderUser1\": 2147483647, \"LeaderUser2\": 2147483646, \"LeaderUser3\": 2147483645 }";
        String l1="LeaderUser1";
        String l2="LeaderUser2";
        String l3="LeaderUser3";
        List<String> ls = new ArrayList<String>();
        List<User> jUser = new ArrayList<>();
        ls.add(l1);
        ls.add(l2);
        ls.add(l3);
        for(int i=0;i<3;i++) {
            String user = ls.get(i);
            String password = user;
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(
                    mediaType,
                    String.format("user=%s&password=%s", user, password)
            );

            String requestUrl = SERVICE_URL + "/auth/register";
            Request request = new Request.Builder()
                    .url(requestUrl)
                    .post(body)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                System.out.println(response.isSuccessful());
            } catch (IOException e) {
                //log.warn("Something went wrong in register.", e);
                System.out.println(false);
            }
            jUser.add( Authentification.userDAO.getUserByLoginData(user,password));
        }
        LB.updateScore(jUser.get(0).getId(),2147483647);
        LB.updateScore(jUser.get(1).getId(),2147483646);
        LB.updateScore(jUser.get(2).getId(),2147483645);
        List<Leader> l= LB.getAll(3);
        Assert.assertEquals("LeaderUser1", Authentification.userDAO.getUserById(l.get(0).getId()).getLogin());
        Assert.assertEquals("LeaderUser2", Authentification.userDAO.getUserById(l.get(1).getId()).getLogin());
        Assert.assertEquals("LeaderUser3", Authentification.userDAO.getUserById(l.get(2).getId()).getLogin());
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

        String requestUrl = SERVICE_URL + "/data/leaderboard?N=3";
        Request request = new Request.Builder()
                .url(requestUrl)
                .get()
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();
        try {
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            Assert.assertEquals(response.body().string(),testJSON);
            System.out.println(response.isSuccessful());
        } catch (IOException e) {
            //log.warn("Something went wrong in register.", e);
            System.out.println(false);
        }

        for(int i=0;i<3;i++){
            Authentification.userDAO.delete(jUser.get(i));
        }

        LB.deleteUser(jUser.get(0).getId());
        LB.deleteUser(jUser.get(1).getId());
        LB.deleteUser(jUser.get(2).getId());
    }
}

