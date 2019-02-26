package recommendation.test;

import recommendation.dao.RatingDao;
import recommendation.domain.Rating;
import recommendation.util.Recommendation;

import java.sql.SQLException;
import java.util.*;

public class Test {
    public static void main(String[] args) throws SQLException {


//        {itemid,userid,score}
//        Rating item;
//        item.setItem(1);
//        item.setUser(2);
//        item.setScore(3.5);
//
//        RatingDao.add(item);
//
//        {itemid,score}

//
        Recommendation.UpdateItemSimilarMessage();

        Map<Integer,Double> map = Recommendation.getRecommendation(3);


        Set<Map.Entry<Integer,Double>> set = map.entrySet();
        List<Map.Entry<Integer,Double>> list = new ArrayList<>(set);

        //对获取的value值进行排序
        Collections.sort(list, new Comparator<Map.Entry<Integer,Double>>() {

            @Override
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {

                if(o1.getValue()>o2.getValue()) return 1;
                return -1;
            }
        });


        for(Map.Entry entry:list)
        {
            System.out.println((Integer)entry.getKey()+"  "+(Double)entry.getValue());
        }

    }
}
