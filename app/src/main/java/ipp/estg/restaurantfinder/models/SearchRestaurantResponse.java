package ipp.estg.restaurantfinder.models;

import java.util.List;

public class SearchRestaurantResponse {

    private String results_found;
    private String reuslts_start;
    private String results_shown;
    private List<Restaurants> restaurants;

    public String getResults_found() {
        return results_found;
    }

    public void setResults_found(String results_found) {
        this.results_found = results_found;
    }

    public String getReuslts_start() {
        return reuslts_start;
    }

    public void setReuslts_start(String reuslts_start) {
        this.reuslts_start = reuslts_start;
    }

    public String getResults_shown() {
        return results_shown;
    }

    public void setResults_shown(String results_shown) {
        this.results_shown = results_shown;
    }

    public List<Restaurants> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurants> restaurants) {
        this.restaurants = restaurants;
    }

    @Override
    public String toString() {
        return "SearchRestaurantResponse{" +
                "results_found='" + results_found + '\'' +
                ", reuslts_start='" + reuslts_start + '\'' +
                ", results_shown='" + results_shown + '\'' +
                ", restaurants=" + restaurants +
                '}';
    }
}
