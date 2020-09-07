package ipp.estg.restaurantfinder.db;

public class Review {

    private String userId;
    private String restaurantId;
    private String comment;
    private int foodRate;
    private int cleanRate;
    private String imageRef;

    public Review() {

    }

    public Review(String userId, String restaurantId, String comment, int foodRate, int cleanRate, String imageRef) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.comment = comment;
        this.foodRate = foodRate;
        this.cleanRate = cleanRate;
        this.imageRef = imageRef;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getFoodRate() {
        return foodRate;
    }

    public void setFoodRate(int foodRate) {
        this.foodRate = foodRate;
    }

    public int getCleanRate() {
        return cleanRate;
    }

    public void setCleanRate(int cleanRate) {
        this.cleanRate = cleanRate;
    }

    public String getImageRef() {
        return imageRef;
    }

    public void setImageRef(String imageRef) {
        this.imageRef = imageRef;
    }

    @Override
    public String toString() {
        return "Review{" +
                "userId='" + userId + '\'' +
                ", restaurantId='" + restaurantId + '\'' +
                ", comment='" + comment + '\'' +
                ", foodRate=" + foodRate +
                ", cleanRate=" + cleanRate +
                ", imageRef='" + imageRef + '\'' +
                '}';
    }
}
