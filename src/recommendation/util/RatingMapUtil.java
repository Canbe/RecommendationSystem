package recommendation.util;

import recommendation.domain.Rating;
import recommendation.domain.SimilarRating;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatingMapUtil {

    public static Map<Integer,Map<Integer,Double>> ratingListToItemMap(List<Rating> ratings)
    {
        Map<Integer,Map<Integer,Double>> items = new HashMap<>();
        Map<Integer,Double> t;
        for(Rating rating:ratings) {
            if (items.containsKey(rating.getItem())) {
                t = items.get(rating.getItem());
                t.put(rating.getUser(), rating.getScore());
            } else {
                t = new HashMap<Integer, Double>();
                t.put(rating.getUser(), rating.getScore());
                items.put(rating.getItem(), t);
            }
        }
        return items;
    }

    public static Map<Integer,Map<Integer,Double>> ratingListToUserMap(List<Rating> ratings)
    {
        Map<Integer,Map<Integer,Double>> user = new HashMap<>();
        Map<Integer,Double> t;
        for(Rating rating:ratings) {
            if (user.containsKey(rating.getUser())) {
                t = user.get(rating.getUser());
                t.put(rating.getItem(), rating.getScore());
            } else {
                t = new HashMap<Integer, Double>();
                t.put(rating.getItem(), rating.getScore());
                user.put(rating.getUser(), t);
            }
        }
        return user;
    }

    public static Map<Integer,Double> SimilarRatingToMap(List<SimilarRating> ratings){

        Map<Integer,Double> map = new HashMap<>();

        for(SimilarRating rating:ratings){

            map.put(rating.getSi_item(),rating.getScore());

        }

        return map;
    }





}
