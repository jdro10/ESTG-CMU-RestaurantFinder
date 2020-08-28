package ipp.estg.restaurantfinder.models;

import java.util.Arrays;
import java.util.List;

public class ZomatoResponse {

    private int results_found;
    private int results_start;
    private int results_shown;
    private List<Restaurant> restaurants;

    public int getResults_found() {
        return results_found;
    }

    public void setResults_found(int results_found) {
        this.results_found = results_found;
    }

    public int getResults_start() {
        return results_start;
    }

    public void setResults_start(int results_start) {
        this.results_start = results_start;
    }

    public int getResults_shown() {
        return results_shown;
    }

    public void setResults_shown(int results_shown) {
        this.results_shown = results_shown;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @Override
    public String toString() {
        return "ZomatoResponse{" +
                "results_found=" + results_found +
                ", results_start=" + results_start +
                ", results_shown=" + results_shown +
                ", restaurants=" + restaurants +
                '}';
    }
}
