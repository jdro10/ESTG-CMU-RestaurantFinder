package ipp.estg.restaurantfinder.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ipp.estg.restaurantfinder.R;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private Double latitude;
    private Double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        Intent coordinatesIntent = getIntent();

        this.latitude = Double.parseDouble(coordinatesIntent.getStringExtra("latitude"));
        this.longitude = Double.parseDouble(coordinatesIntent.getStringExtra("longitude"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng restaurantCoordinates = new LatLng(this.latitude, this.longitude);
        googleMap.addMarker(new MarkerOptions().position(restaurantCoordinates));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(restaurantCoordinates));
    }
}