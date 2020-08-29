package ipp.estg.restaurantfinder.interfaces;

import ipp.estg.restaurantfinder.models.SearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ZomatoApi {

    @Headers("user-key: 4ab7c3277ea06db31648d38b298d8e7e")
    @GET("search")
    Call<SearchResponse> getAllRestaurants();
}
