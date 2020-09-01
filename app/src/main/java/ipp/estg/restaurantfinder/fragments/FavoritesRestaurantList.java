package ipp.estg.restaurantfinder.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ipp.estg.restaurantfinder.R;
import ipp.estg.restaurantfinder.adapters.FavoriteRestaurantAdapter;
import ipp.estg.restaurantfinder.db.RestaurantDB;
import ipp.estg.restaurantfinder.db.RestaurantRoom;

public class FavoritesRestaurantList extends Fragment {

    private Context context;
    private FavoriteRestaurantAdapter favoriteRestaurantAdapter;
    private RecyclerView recyclerView;
    private List<RestaurantRoom> favoriteRestaurants;
    private final ExecutorService databaseReadExecutor = Executors.newFixedThreadPool(1);
    private RestaurantDB db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        favoriteRestaurants = new ArrayList<>();
        this.context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.restaurants_fragment, container, false);

        this.recyclerView = contentView.findViewById(R.id.restaurantsRecyclerView);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        this.getRestaurants();

        this.favoriteRestaurantAdapter = new FavoriteRestaurantAdapter(this.context, this.favoriteRestaurants);

        this.recyclerView.setAdapter(this.favoriteRestaurantAdapter);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL));
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.context));

        return contentView;
    }

    private void getRestaurants() {

        db = Room.databaseBuilder(context, RestaurantDB.class, "RestaurantsDB").build();
        databaseReadExecutor.execute(() -> {
            favoriteRestaurants.addAll(Arrays.asList(db.daoAccess().getAll()));
        });
    }
}
