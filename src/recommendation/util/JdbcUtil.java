package recommendation.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;

public class JdbcUtil {

    private static DataSource ds = null;
    static {
        Properties ps = new Properties();
        String dbPath = null;
        try {
            dbPath = new File("res//db.properties").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileInputStream in;
        try {
           in = new FileInputStream(dbPath);
           ps.load(in);
           ds = DruidDataSourceFactory.createDataSource(ps);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static DataSource getDataSource(){
        return ds;
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void print(){
        System.out.println("helloworld");
    }

    public static String getUUID()
    {
        return UUID.randomUUID().toString().replace("-","");
    }

    public static QueryRunner getQueryRunner(){
        return new QueryRunner();
    }

}
