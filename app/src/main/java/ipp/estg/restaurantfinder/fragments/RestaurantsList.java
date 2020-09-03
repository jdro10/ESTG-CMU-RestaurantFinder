package ipp.estg.restaurantfinder.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ipp.estg.restaurantfinder.R;
import ipp.estg.restaurantfinder.adapters.RestaurantAdapter;
import ipp.estg.restaurantfinder.db.RestaurantDB;
import ipp.estg.restaurantfinder.db.RestaurantRoom;
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
    private List<RestaurantRoom> favoriteRestaurantsList;
    private final ExecutorService databaseReadExecutor = Executors.newFixedThreadPool(1);
    private RestaurantDB db;
    private static final int REQUEST_FINE_LOCATION = 100;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String latitude;
    private String longitude;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getActivity();
        this.favoriteRestaurantsList = new ArrayList<>();
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context.getApplicationContext());
        setHasOptionsMenu(true);

        this.getLastLocation();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.restaurants_fragment, container, false);

        this.recyclerView = contentView.findViewById(R.id.restaurantsRecyclerView);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(contentView.getContext()));

        this.getRestaurants();

        this.restaurantAdapter = new RestaurantAdapter(this.context, new ArrayList<>(), this.favoriteRestaurantsList);
        this.recyclerView.setAdapter(this.restaurantAdapter);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL));
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.context));

        return contentView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.restaurant_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                restaurantAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    private void getRestaurants(){

        db = Room.databaseBuilder(context, RestaurantDB.class,"RestaurantsDB").build();
        databaseReadExecutor.execute(() -> {
            favoriteRestaurantsList.addAll(Arrays.asList(db.daoAccess().getAll()));
        });
    }

    private void getLastLocation() {
        if(ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());

                    getRestaurantsFromAPI(latitude, longitude, 50000);
                }
            }
        }).addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Couldn't get your location. Try again later!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
    }

    private void getRestaurantsFromAPI(String latitude, String longitude, int radius) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://developers.zomato.com/api/v2.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ZomatoApi zomatoapi = retrofit.create(ZomatoApi.class);
        Call<SearchResponse> call = zomatoapi.getNearbyRestaurants(latitude, longitude, String.valueOf(radius));
        this.searchResponseList = new ArrayList<>();

        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful()) {
                    searchResponseList.addAll(response.body().getRestaurants());
                    restaurantAdapter.setRestaurants(searchResponseList);

                    getActivity().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(context, "Error fetching data from Zomato, please try again later!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
