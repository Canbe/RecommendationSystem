package recommendation.util;

import recommendation.dao.RatingDao;
import recommendation.dao.SimilarRatingDao;
import recommendation.domain.SimilarRating;

import java.sql.SQLException;

public class Comparable extends Thread {

    private int i;
    private int j;

    public void setValues(int i,int j)
    {
        this.i = i;
        this.j = j;
    }


    public static int length = 0;

    @Override
    public void run() {
        length++;
//        System.out.println("线程:"+length+" 已经开始工作:"+i+" "+j);
        double rating = 0;
        try {
            rating = Recommendation.OuclidDistance(RatingMapUtil.ratingListToItemMap(RatingDao.getRatingByTwoItem(i,j)),i,j);
            if(rating<=0.0) return;
            SimilarRating t = new SimilarRating();
            t.setItem(i);
            t.setSi_item(j);
            t.setScore(rating);
            if(SimilarRatingDao.updateRating(t)==0)
            {
                SimilarRatingDao.insertRating(t);
            }
        }catch (SQLException e) {
            System.out.println("出错:"+i+" "+j);
        }finally {
        }

    }

}
