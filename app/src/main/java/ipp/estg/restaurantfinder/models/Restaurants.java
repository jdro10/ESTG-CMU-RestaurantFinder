package ipp.estg.restaurantfinder.models;

import com.google.gson.annotations.SerializedName;

public class Restaurants {

    @SerializedName("restaurant")
    private Restaurant restaurant;

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Restaurants{" +
                "restaurant=" + restaurant +
                '}';
    }
}
