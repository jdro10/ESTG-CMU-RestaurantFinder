package ipp.estg.restaurantfinder.interfaces;

import ipp.estg.restaurantfinder.models.Restaurant;
import ipp.estg.restaurantfinder.models.SearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ZomatoApi {

    @Headers("user-key: 4ab7c3277ea06db31648d38b298d8e7e")
    @GET("search")
    Call<SearchResponse> getAllRestaurants();

    @Headers("user-key: 4ab7c3277ea06db31648d38b298d8e7e")
    @GET("search")
    Call<SearchResponse> getNearbyRestaurants(@Query("lat") String lat, @Query("lon") String lon, @Query("radius") String radius);

    @Headers("user-key: 4ab7c3277ea06db31648d38b298d8e7e")
    @GET("search")
    Call<SearchResponse> getNearbyRestaurantsAsc(@Query("lat") String lat, @Query("lon") String lon, @Query("radius") String radius, @Query("order") String order);

    @Headers("user-key: 4ab7c3277ea06db31648d38b298d8e7e")
    @GET("search")
    Call<SearchResponse> getNearbyRestaurantsByCuisineType(@Query("lat") String lat, @Query("lon") String lon, @Query("radius") String radius, @Query("cuisines") String cuisines);

    @Headers("user-key: 4ab7c3277ea06db31648d38b298d8e7e")
    @GET("restaurant")
    Call<Restaurant> getIndividualRestaurant(@Query("res_id") String id);
}
