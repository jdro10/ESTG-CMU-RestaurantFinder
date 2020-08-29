package ipp.estg.restaurantfinder.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {

    @SerializedName("results_found")
    private String resultsFound;
    @SerializedName("results_start")
    private String resultsStart;
    @SerializedName("results_shown")
    private String resultsShown;
    @SerializedName("restaurants")
    private List<Restaurants> restaurants;

    public String getResultsFound() {
        return resultsFound;
    }

    public void setResultsFound(String resultsFound) {
        this.resultsFound = resultsFound;
    }

    public String getResultsStart() {
        return resultsStart;
    }

    public void setResultsStart(String resultsStart) {
        this.resultsStart = resultsStart;
    }

    public String getResultsShown() {
        return resultsShown;
    }

    public void setResultsShown(String resultsShown) {
        this.resultsShown = resultsShown;
    }

    public List<Restaurants> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurants> restaurants) {
        this.restaurants = restaurants;
    }

    @Override
    public String toString() {
        return "SearchResponse{" +
                "resultsFound='" + resultsFound + '\'' +
                ", resultsStart='" + resultsStart + '\'' +
                ", resultsShown='" + resultsShown + '\'' +
                ", restaurants=" + restaurants +
                '}';
    }
}
