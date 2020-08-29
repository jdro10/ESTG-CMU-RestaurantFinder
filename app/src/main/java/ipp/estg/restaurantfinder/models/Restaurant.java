package ipp.estg.restaurantfinder.models;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Restaurant {

    @SerializedName("R")
    private R R;
    @SerializedName("apikey")
    private String apiKey;
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;
    @SerializedName("location")
    private Location location;
    @SerializedName("cuisines")
    private String cuisines;
    @SerializedName("timings")
    private String timings;
    @SerializedName("currency")
    private String currency;
    @SerializedName("highlights")
    private String[] highlights;
    @SerializedName("thumb")
    private String thumb;
    @SerializedName("user_ratings")
    private UserRating userRating;
    @SerializedName("featured_image")
    private String featuredImage;
    @SerializedName("has_online_delivery")
    private String hasOnlineDelivery;
    @SerializedName("is_table_reservation_supported")
    private int isTableReservationSupported;
    @SerializedName("phone_numbers")
    private String phoneNumbers;

    public ipp.estg.restaurantfinder.models.R getR() {
        return R;
    }

    public void setR(ipp.estg.restaurantfinder.models.R r) {
        R = r;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getCuisines() {
        return cuisines;
    }

    public void setCuisines(String cuisines) {
        this.cuisines = cuisines;
    }

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String[] getHighlights() {
        return highlights;
    }

    public void setHighlights(String[] highlights) {
        this.highlights = highlights;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public UserRating getUserRating() {
        return userRating;
    }

    public void setUserRating(UserRating userRating) {
        this.userRating = userRating;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
    }

    public String getHasOnlineDelivery() {
        return hasOnlineDelivery;
    }

    public void setHasOnlineDelivery(String hasOnlineDelivery) {
        this.hasOnlineDelivery = hasOnlineDelivery;
    }

    public int getIsTableReservationSupported() {
        return isTableReservationSupported;
    }

    public void setIsTableReservationSupported(int isTableReservationSupported) {
        this.isTableReservationSupported = isTableReservationSupported;
    }

    public String getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "R=" + R +
                ", apiKey='" + apiKey + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", location=" + location +
                ", cuisines='" + cuisines + '\'' +
                ", timings='" + timings + '\'' +
                ", currency='" + currency + '\'' +
                ", highlights=" + Arrays.toString(highlights) +
                ", thumb='" + thumb + '\'' +
                ", userRating=" + userRating +
                ", featuredImage='" + featuredImage + '\'' +
                ", hasOnlineDelivery='" + hasOnlineDelivery + '\'' +
                ", isTableReservationSupported=" + isTableReservationSupported +
                ", phoneNumbers='" + phoneNumbers + '\'' +
                '}';
    }
}