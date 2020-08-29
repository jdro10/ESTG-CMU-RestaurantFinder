package ipp.estg.restaurantfinder.models;

import java.util.Arrays;

public class Restaurant {

    private R R;
    private String apikey;
    private String id;
    private String url;
    private Location location;
    private String cuisines;
    private String timings;
    private String currency;
    private String[] highlights;
    private String thumb;
    private UserRating user_rating;
    private String featured_image;
    private String has_online_delivery;
    private int is_table_reservation_supported;
    private String phone_numbers;

    public ipp.estg.restaurantfinder.models.R getR() {
        return R;
    }

    public void setR(ipp.estg.restaurantfinder.models.R r) {
        R = r;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
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

    public UserRating getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(UserRating user_rating) {
        this.user_rating = user_rating;
    }

    public String getFeatured_image() {
        return featured_image;
    }

    public void setFeatured_image(String featured_image) {
        this.featured_image = featured_image;
    }

    public String getHas_online_delivery() {
        return has_online_delivery;
    }

    public void setHas_online_delivery(String has_online_delivery) {
        this.has_online_delivery = has_online_delivery;
    }

    public int getIs_table_reservation_supported() {
        return is_table_reservation_supported;
    }

    public void setIs_table_reservation_supported(int is_table_reservation_supported) {
        this.is_table_reservation_supported = is_table_reservation_supported;
    }

    public String getPhone_numbers() {
        return phone_numbers;
    }

    public void setPhone_numbers(String phone_numbers) {
        this.phone_numbers = phone_numbers;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "R=" + R +
                ", apikey='" + apikey + '\'' +
                ", id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", location=" + location +
                ", cuisines='" + cuisines + '\'' +
                ", timings='" + timings + '\'' +
                ", currency='" + currency + '\'' +
                ", highlights=" + Arrays.toString(highlights) +
                ", thumb='" + thumb + '\'' +
                ", user_rating=" + user_rating +
                ", featured_image='" + featured_image + '\'' +
                ", has_online_delivery='" + has_online_delivery + '\'' +
                ", is_table_reservation_supported=" + is_table_reservation_supported +
                ", phone_numbers='" + phone_numbers + '\'' +
                '}';
    }
}