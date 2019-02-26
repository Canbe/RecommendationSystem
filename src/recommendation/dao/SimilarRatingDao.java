package recommendation.dao;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import recommendation.domain.SimilarRating;
import recommendation.util.JdbcUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SimilarRatingDao {

    public static int insertRating(SimilarRating rating) throws SQLException {
        String sql = "insert into similar_rating (id,item,si_item,score) values(default,?,?,?)";
        Connection conn = JdbcUtil.getConnection();
        int row = JdbcUtil.getQueryRunner().update(conn,sql,rating.getItem(),rating.getSi_item(),rating.getScore());
        conn.close();
        return row;
    }

    public static int updateRating(SimilarRating rating) throws SQLException {
        String sql = "update similar_rating set score = ? where item = ? and si_item = ? ";
        Connection conn = JdbcUtil.getConnection();
        int row = JdbcUtil.getQueryRunner().update(conn,sql,rating.getScore(),rating.getItem(),rating.getSi_item());
        conn.close();
        return row;
    }

    public static List<SimilarRating> getSimilarRating(Integer item1id) throws SQLException {
        String sql = "select * from similar_rating where item = ? ";
        Connection conn = JdbcUtil.getConnection();
        List<SimilarRating> ratings = JdbcUtil.getQueryRunner().query(conn,sql,new BeanListHandler<SimilarRating>(SimilarRating.class),item1id);
        conn.close();
        return ratings;
    }




}
