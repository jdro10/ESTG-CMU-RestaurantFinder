package ipp.estg.restaurantfinder.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ipp.estg.restaurantfinder.R;
import ipp.estg.restaurantfinder.interfaces.ZomatoApi;
import ipp.estg.restaurantfinder.models.Restaurant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantDetails extends Fragment {

    private Context context;
    private Restaurant restaurant;
    private TextView restaurant_selected;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.restaurant_layout, container, false);

        restaurant_selected = contentView.findViewById(R.id.restaurant_selected);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://developers.zomato.com/api/v2.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ZomatoApi zomatoapi = retrofit.create(ZomatoApi.class);

        Call<Restaurant> call = zomatoapi.getRestaurant("19428023"); //estatico

        //getActivity().getIntent().getStringExtra("phoneNumber")


        call.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                if (response.isSuccessful()) {
                    restaurant_selected.setText(response.body().getName());
                }
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {

            }
        });


        return contentView;
    }


}
