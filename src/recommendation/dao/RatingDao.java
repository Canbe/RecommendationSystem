package recommendation.dao;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import recommendation.domain.Rating;
import recommendation.util.JdbcUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RatingDao {

    public static List<Rating> getRatingByItem(Integer itemid) throws SQLException {

        String sql = "select * from rating where item = ? ";
        Connection conn = JdbcUtil.getConnection();

        List<Rating>ratings = JdbcUtil.getQueryRunner().query(conn,sql,new BeanListHandler<Rating>(Rating.class),itemid);

        conn.close();

        return ratings;

    }

    public static List<Rating> getRatingByUser(Integer userid) throws SQLException {
        String sql = "select * from rating where user = ? ";
        Connection conn = JdbcUtil.getConnection();

        List<Rating>ratings = JdbcUtil.getQueryRunner().query(conn,sql,new BeanListHandler<Rating>(Rating.class),userid);

        conn.close();

        return ratings;
    }

    public static List<Rating> getRatingByTwoItem(Integer itemid1,Integer itemid2) throws SQLException {
        String sql = "select * from rating where item = ? or item = ? ";
        Connection conn = JdbcUtil.getConnection();

        List<Rating>ratings = JdbcUtil.getQueryRunner().query(conn,sql,new BeanListHandler<Rating>(Rating.class),itemid1,itemid2);

        conn.close();

        return ratings;
    }

    public static List<Rating> getRatingByTwoUser(Integer userid1,Integer userid2) throws SQLException {
        String sql = "select * from rating where user = ? or user = ? ";
        Connection conn = JdbcUtil.getConnection();

        List<Rating> ratings = JdbcUtil.getQueryRunner().query(conn, sql, new BeanListHandler<Rating>(Rating.class), userid1, userid2);

        conn.close();

        return ratings;
    }

}
