package ipp.estg.restaurantfinder.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ipp.estg.restaurantfinder.MainActivity;
import ipp.estg.restaurantfinder.R;
import ipp.estg.restaurantfinder.activities.FavoritesRestaurants;
import ipp.estg.restaurantfinder.db.RestaurantDB;
import ipp.estg.restaurantfinder.db.RestaurantRoom;
import ipp.estg.restaurantfinder.helpers.RetrofitHelper;
import ipp.estg.restaurantfinder.interfaces.ZomatoApi;
import ipp.estg.restaurantfinder.models.Restaurants;
import ipp.estg.restaurantfinder.models.SearchResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationService extends Service {

    private Context context;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private List<Restaurants> restaurants;
    private final ExecutorService databaseReadExecutor = Executors.newFixedThreadPool(1);
    private RestaurantDB db;
    private List<RestaurantRoom> favoriteRestaurantsList;
    private Bitmap restaurantBitmap;
    public static final String CHANNEL_ID = "LocationService";

    @Override
    public void onCreate() {
        super.onCreate();

        this.createNotificationChannel();
        this.favoriteRestaurantsList = new ArrayList<>();
        this.context = getApplicationContext();
        this.locationRequest = new LocationRequest();
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        this.getRestaurants();

        this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        this.locationRequest.setInterval(60000);
        this.locationRequest.setFastestInterval(60000);

        Calendar rightNow = Calendar.getInstance();

        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    if ((rightNow.get(Calendar.HOUR_OF_DAY) <= 11 && rightNow.get(Calendar.HOUR_OF_DAY) <= 15) || (rightNow.get(Calendar.HOUR_OF_DAY) >= 18 && rightNow.get(Calendar.HOUR_OF_DAY) <= 22)) {
                        getNearbyRestaurantFromAPI(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), 50000, "real_distance");
                    }
                }
            }
        };

        startLocationUpdates();
    }

    private void getNearbyRestaurantFromAPI(String latitude, String longitude, int radius, String distance) {
        ZomatoApi zomatoapi = RetrofitHelper.getRetrofit().create(ZomatoApi.class);
        Call<SearchResponse> call = zomatoapi.getNearbyRestaurantsDistance(latitude, longitude, String.valueOf(radius), "real_distance");
        this.restaurants = new ArrayList<>();

        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful()) {
                    restaurants.addAll(response.body().getRestaurants());

                    if (favoriteRestaurantsList.size() != 0) {
                        for (int i = 0; i < restaurants.size(); i++) {
                            for (int j = 0; j < favoriteRestaurantsList.size(); j++) {
                                if (restaurants.get(i).getRestaurant().getName().equals(favoriteRestaurantsList.get(j).getName())) {
                                    new GetRestaurantImage(restaurants.get(i).getRestaurant().getName()).execute(response.body().getRestaurants().get(i).getRestaurant().getThumb());
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error fetching data from Zomato, please try again later!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("RestaurantFinder")
                .setContentText("We are currently looking for your nearby favorite restaurants!")
                .setSmallIcon(R.drawable.restaurant_icon)
                .build();

        startForeground(1, notification);

        return START_NOT_STICKY;
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        this.fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void getRestaurants() {
        db = Room.databaseBuilder(this.context, RestaurantDB.class, "RestaurantsDB").build();
        databaseReadExecutor.execute(() -> {
            favoriteRestaurantsList.addAll(Arrays.asList(db.daoAccess().getAll()));
        });
    }

    private class GetRestaurantImage extends AsyncTask<String, Void, Bitmap> {

        private String restaurantName;

        public GetRestaurantImage(String restaurantName) {
            this.restaurantName = restaurantName;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;

            try {
                InputStream inputStream = new java.net.URL(strings[0]).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            restaurantBitmap = bitmap;

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentTitle("RestaurantFinder")
                    .setContentText("You are near - " + this.restaurantName + "! Your favorite restaurant!")
                    .setSmallIcon(R.drawable.restaurant_icon)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(restaurantBitmap))
                    .setOngoing(false);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(2, builder.build());
        }
    }

    private void createNotificationChannel() {
        NotificationChannel locationServiceChannel = new NotificationChannel(
                CHANNEL_ID,
                "Location Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(locationServiceChannel);
    }
}
