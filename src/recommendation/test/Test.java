package recommendation.test;

import recommendation.dao.RatingDao;
import recommendation.util.Recommendation;

import java.sql.SQLException;
import java.util.*;

public class Test {
    public static void main(String[] args) throws SQLException {

        //用于更新数据库中的相似物品库
        Recommendation.UpdateItemSimilarMessage();

        List<Map.Entry<Integer,Double>> list  = Recommendation.getRecommendation(3);

        for(Map.Entry entry:list)
        {
            System.out.println((Integer)entry.getKey()+"  "+(Double)entry.getValue());
        }

    }
}
