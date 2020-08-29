package ipp.estg.restaurantfinder.models;

public class Restaurants {

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
