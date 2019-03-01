package recommendation.util;

import recommendation.dao.RatingDao;
import recommendation.dao.SimilarRatingDao;
import recommendation.domain.SimilarRating;

import java.sql.SQLException;
import java.util.*;

public class Recommendation {

    //最少相识物品数的配置
    public static double least_similar_item = 3.5;


    //返回从大到小的推荐列表
    public static List<Map.Entry<Integer,Double>> getRecommendation(Integer userid) throws SQLException {

        Map<Integer,Map<Integer,Double>> userRating = RatingMapUtil.ratingListToUserMap(RatingDao.getRatingByUser(userid));
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
            //加权数小于限制最小值的，去除该项
            if(totalSim.get(entry.getKey())[1]<=Recommendation.least_similar_item)
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

        Set<Map.Entry<Integer,Double>> set = recommendationList.entrySet();
        List<Map.Entry<Integer,Double>> list = new ArrayList<>(set);

        //对获取的value值进行排序
        Collections.sort(list, new Comparator<Map.Entry<Integer,Double>>() {

            @Override
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {

                if(o1.getValue()>o2.getValue()) return -1;
                return 1;
            }
        });

        return list;
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

        //获取rating数据表里的item数据集
        List items = RatingDao.getAllItems();

        Comparable temp = null;
        int len = items.size();
        int total = len*len;
        for(int i=0;i<len;i++)
        {
            int j = 0;
            for(;j<len;j++)
            {
                if (i==j) continue;
                temp = new Comparable();
                temp.setValues((Integer) items.get(i),(Integer) items.get(j));
                temp.start();
                System.out.println(i*len+"/"+total);
            }
        }


    }


}
