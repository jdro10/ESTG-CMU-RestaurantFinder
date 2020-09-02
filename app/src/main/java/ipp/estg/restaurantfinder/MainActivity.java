package ipp.estg.restaurantfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ipp.estg.restaurantfinder.activities.AuthenticationActivity;
import ipp.estg.restaurantfinder.activities.FavoritesRestaurants;
import ipp.estg.restaurantfinder.activities.NearbyRestaurants;
import ipp.estg.restaurantfinder.activities.RestaurantSelected;
import ipp.estg.restaurantfinder.services.LocationService;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    public static final String CHANNEL_ID = "LocationService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        Button button = findViewById(R.id.loginActivityButton);
        Button button1 = findViewById(R.id.restaurantButton);
        Button button2 = findViewById(R.id.favoritesActivity);
        Button button3 = findViewById(R.id.restaurant_details);
        Button button4 = findViewById(R.id.startService);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
                startActivity(intent);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NearbyRestaurants.class);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FavoritesRestaurants.class);
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RestaurantSelected.class);
                startActivity(intent);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(view);
            }
        });

    }

    public void startService(View v) {
        Intent serviceIntent = new Intent(this, LocationService.class);
        startService(serviceIntent);
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