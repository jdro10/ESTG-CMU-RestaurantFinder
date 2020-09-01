package ipp.estg.restaurantfinder.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ipp.estg.restaurantfinder.R;

public class NearbyRestaurants extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException ignored) {
        }

        setContentView(R.layout.activity_nearby_restaurants);
    }
}