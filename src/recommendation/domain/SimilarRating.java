package recommendation.domain;

public class SimilarRating {
    private int id;
    private int item;
    private int si_item;
    private double score;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public int getSi_item() {
        return si_item;
    }

    public void setSi_item(int si_item) {
        this.si_item = si_item;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
