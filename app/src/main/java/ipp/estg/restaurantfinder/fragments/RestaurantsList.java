package ipp.estg.restaurantfinder.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ipp.estg.restaurantfinder.R;
import ipp.estg.restaurantfinder.adapters.RestaurantAdapter;
import ipp.estg.restaurantfinder.interfaces.ZomatoApi;
import ipp.estg.restaurantfinder.models.Restaurants;
import ipp.estg.restaurantfinder.models.SearchResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestaurantsList extends Fragment {

    private Context context;
    private RestaurantAdapter restaurantAdapter;
    private RecyclerView recyclerView;
    private List<Restaurants> searchResponseList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.restaurants_fragment, container, false);

        this.recyclerView = contentView.findViewById(R.id.restaurantsRecyclerView);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        this.getRestaurantsFromAPI();

        this.restaurantAdapter = new RestaurantAdapter(this.context, this.searchResponseList);

        this.recyclerView.setAdapter(this.restaurantAdapter);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL));
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.context));

        return contentView;
    }

    private void getRestaurantsFromAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://developers.zomato.com/api/v2.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ZomatoApi zomatoapi = retrofit.create(ZomatoApi.class);
        Call<SearchResponse> call = zomatoapi.getAllRestaurants();
        this.searchResponseList = new ArrayList<>();

        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful()) {
                    searchResponseList.addAll(response.body().getRestaurants());
                    restaurantAdapter.notifyDataSetChanged();
                    getActivity().findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {

            }
        });
    }
}