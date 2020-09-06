package ipp.estg.restaurantfinder.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ipp.estg.restaurantfinder.R;
import ipp.estg.restaurantfinder.services.LocationService;

import static ipp.estg.restaurantfinder.activities.PreferencesActivity.KEY_NOTIFICATION;
import static ipp.estg.restaurantfinder.activities.PreferencesActivity.KEY_USER_EMAIL;
import static ipp.estg.restaurantfinder.activities.PreferencesActivity.SHARED_PREF_NAME;

public class NearbyRestaurants extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_restaurants);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        this.editor = sharedPreferences.edit();

        this.startService();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), PreferencesActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_favourite) {
            Intent intent = new Intent(getApplicationContext(), FavoritesRestaurants.class);
            startActivity(intent);
        } else if (id == R.id.action_historic) {
            Intent intent = new Intent(getApplicationContext(), HistoricActivity.class);
            startActivity(intent);
        } else if(id == R.id.action_signout) {
            this.editor.putString(KEY_USER_EMAIL, null);
            this.editor.commit();
            Intent intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void startService() {
        this.sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String strNotification = this.sharedPreferences.getString(KEY_NOTIFICATION, "");

        if (strNotification.equals("true")) {
            Intent serviceIntent = new Intent(this, LocationService.class);
            startService(serviceIntent);
        } else {
            Toast.makeText(getApplicationContext(), "Unable to notify you about your near favorite restaurants", Toast.LENGTH_LONG).show();
        }
    }
}