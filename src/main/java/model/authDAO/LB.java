package model.authDAO;

import model.authInfo.Leader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Max on 07.11.2016.
 */
public class LB {
    private static final Logger log = LogManager.getLogger(LB.class);
    private static final String INSERT_POINTS_TEMPLATE =
            "INSERT INTO leaderboard VALUES (%d);";
    //private static final String UPDATE_ID=
     //       "UPDATE leaderboard SET userid=%d WHERE muser=\'%s\';";
    private static final String UPDATE_POINTS =
            "UPDATE leaderboard SET score = %d WHERE userid =%d;";
    private static final String SELECT_LEADERS =
            "SELECT * FROM leaderboard ORDER BY score DESC LIMIT %d;";

    public static void insert(int userid){
        log.info(String.format(INSERT_POINTS_TEMPLATE, userid));
        try (Connection con = JDBCDbConnection.getConnection();
             Statement stm = con.createStatement()) {
            stm.execute(String.format(INSERT_POINTS_TEMPLATE, userid));
            log.info("User " + userid +" Inserted to leaderbord");
            log.info(String.format(INSERT_POINTS_TEMPLATE, userid));
        } catch (SQLException e) {
            log.error("Failed to insert user id");
        }
    }

    public static void updateScore(int userid, int pts){
        try (Connection con = JDBCDbConnection.getConnection();
             Statement stm = con.createStatement()) {
            stm.execute(String.format(UPDATE_POINTS, pts, userid));
            log.info("user "+ userid + " pts "+ pts + " updated");
            log.info(String.format(UPDATE_POINTS, pts, userid));
        } catch (SQLException e) {
            log.error("Failed to update pts");
        }
    }


    public static List<Leader> getAll(int N) {
        List<Leader> leaders = new ArrayList<>();
        try (Connection con = JDBCDbConnection.getConnection();
             Statement stm = con.createStatement()) {
            ResultSet rs = stm.executeQuery(String.format(SELECT_LEADERS,N));
            while (rs.next()) {
                leaders.add(mapToLeader(rs));
                log.info("Userid " + rs.getInt("userid") + "pts "+ rs.getInt("score"));
            }
        } catch (SQLException e) {
            log.error("Failed to getAll.", e);
            return Collections.emptyList();
        }

        return leaders;
    }

    public static void deleteUser(int userid){
        List<Leader> leaders = new ArrayList<>();
        try (Connection con = JDBCDbConnection.getConnection();
             Statement stm = con.createStatement()) {
            stm.execute("DELETE FROM leaderboard WHERE userid = " + userid +";");
            log.info("UserId "+ userid + " deleted");
        }catch (SQLException e) {
            log.error("Failed to delete", e);
        }
    }

    public static int getUserScore(int userid){
        try (Connection con = JDBCDbConnection.getConnection();
             Statement stm = con.createStatement()) {
            ResultSet rs = stm.executeQuery("SELECT * FROM leaderboard WHERE userid = " + userid + ";");
            rs.next();
            return rs.getInt("score");
        }catch (SQLException e) {
            log.error("Failed to getScore.", e);
            return -1;
        }
    }

    private static Leader mapToLeader(ResultSet rs) throws SQLException{
        return new Leader().setName(rs.getInt("userid")).setPts(rs.getInt("score"));
    }

}
