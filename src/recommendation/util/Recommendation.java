package recommendation.util;

import recommendation.dao.RatingDao;
import recommendation.dao.SimilarRatingDao;
import recommendation.domain.Rating;
import recommendation.domain.SimilarRating;

import java.sql.SQLException;
import java.util.*;

public class Recommendation {

    public static Map<Integer,Double> getRecommendation(Integer userid) throws SQLException {

        Map<Integer,Map<Integer,Double>> userRating = RatingMapUtil.ratingListToUserMap(RatingDao.getRatingByUser(userid));
        System.out.println(userRating.size());
        if(userRating==null) return null;

        Map<Integer,Double> recommendationList = new HashMap<>();
        Map<Integer,Double[]> totalSim  = new HashMap<>();
        Map<Integer,Double> rating = userRating.get(userid);
        List<SimilarRating> similarRatings;

        for(Map.Entry entry:rating.entrySet())
        {
            similarRatings = SimilarRatingDao.getSimilarRating((Integer) entry.getKey());
            double eval = (Double) entry.getValue();
            if(similarRatings==null) continue;

//            遍历相近的物品
            for(SimilarRating si:similarRatings)
            {
                //如果用户已经评价过该物品，则跳过
                if(rating.containsKey(si.getSi_item())) continue;

                //相识度评价值加权之和
                double score = recommendationList.getOrDefault(si.getSi_item(),0.0);
                recommendationList.put(si.getSi_item(),si.getScore()*eval+score);
                Double[] ar = new Double[2];
                ar[0] = 0.0;
                ar[1] = 0.0;
                //相识度之和
                Double similar[] = totalSim.getOrDefault(si.getSi_item(),ar);
                similar[0] +=si.getScore();
                similar[1] +=1;
                totalSim.put(si.getSi_item(),similar);

            }
        }
        List<Map.Entry> removeList = new ArrayList<>();
        for(Map.Entry entry : recommendationList.entrySet())
        {
            //加权数小于两次的，从列表中去除
            if(totalSim.get(entry.getKey())[1]<=3.5)
            {
                removeList.add(entry);
            }
            else
            {
                recommendationList.put((Integer)entry.getKey(),(Double)entry.getValue()/totalSim.get(entry.getKey())[0]);
            }

        }

        for(Map.Entry entry:removeList)
        {
            recommendationList.remove(entry.getKey());
        }

        return recommendationList;
    }

    //计算item1和item2的欧几里得距离
    public static Double OuclidDistance(Map<Integer,Map<Integer,Double>> prefs,Integer item1,Integer item2){

        ArrayList<Integer> similarList = new ArrayList<Integer>();

        Map<Integer,Double> item1List = prefs.get(item1);
        Map<Integer,Double> item2List = prefs.get(item2);

        if(item1List==null||item2List==null) return 0.0;

        Iterator iter = item1List.entrySet().iterator();
        // 获取item1 和 item2 的交集
        Map.Entry entry;
        while (iter.hasNext()) {
             entry = (HashMap.Entry) iter.next();
            if(item2List.containsKey(entry.getKey()))
            {
                similarList.add((Integer) entry.getKey());
            }
        }
//        交集为空，相识度为0
        if(similarList.size()==0)
        {
            return 0.0;
        }

        double sum_distance = 0.0;

        for(Integer similar_key:similarList)
        {
            sum_distance+=Math.pow((Double)item1List.get(similar_key)-(Double)item2List.get(similar_key),2);
        }

        return 1/Math.sqrt(sum_distance+1);
    }

    //更新数据库中的物品相似度信息
    public static void UpdateItemSimilarMessage() throws SQLException {
        SimilarRating t = new SimilarRating();
        //数据库中的物品1-100 两两比较
        for(int i=1;i<=100;i++)
        {
            for(int j=1;j<=100;j++)
            {
                //不和自己比较
                if(i==j) continue;
                double rating = OuclidDistance(RatingMapUtil.ratingListToItemMap(RatingDao.getRatingByTwoItem(i,j)),i,j);
                if(rating<=0.0) continue;

                t.setItem(i);
                t.setSi_item(j);
                t.setScore(rating);

                if(SimilarRatingDao.updateRating(t)==0)
                {
                    SimilarRatingDao.insertRating(t);
                }

            }
        }

    }

}
