package ipp.estg.restaurantfinder.models;

public class UserRating {

    private float aggregate_rating;
    private String rating_text;
    private int votes;

    public float getAggregate_rating() {
        return aggregate_rating;
    }

    public void setAggregate_rating(float aggregate_rating) {
        this.aggregate_rating = aggregate_rating;
    }

    public String getRating_text() {
        return rating_text;
    }

    public void setRating_text(String rating_text) {
        this.rating_text = rating_text;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
