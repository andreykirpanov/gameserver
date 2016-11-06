package clientTest;

import client.AuthRequests;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
/**
 * Created by User on 06.11.2016.
 */
public class ClientTest {

    AuthRequests requests = new AuthRequests();
    String login = "Andrew";
    String password = "123";

    @Test
    public void registerTest(){
        assertTrue(requests.register(login, password));
        assertFalse(requests.register(login, password));
        assertFalse(requests.register(login, null));
    }

    @Test
    public void loginTest(){
        Long token = requests.login(login, password);
        assertNotEquals(Long.valueOf(0), token);
        assertEquals(token, requests.login(login, password));
    }

    @Test
    public void wrongPasswordTest(){
        requests.login(login, "124");
    }

    @Test
    public void logoutTest(){
        Long token = requests.login(login, password);
        assertTrue(requests.logout(token));
        assertFalse(requests.logout(token));
    }

    @Test
    public void anotherTokenTest(){
        Long token = requests.login(login, password);
        requests.logout(token);
        assertNotEquals(token, requests.login(login, password));
    }
}
