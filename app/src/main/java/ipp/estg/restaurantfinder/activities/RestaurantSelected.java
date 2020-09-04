package ipp.estg.restaurantfinder.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ipp.estg.restaurantfinder.R;

public class RestaurantSelected extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_selected);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            Intent intent = new Intent(getApplicationContext(), FavoritesRestaurants.class);
            startActivity(intent);
        }else if(id == R.id.action_favourite){
            Intent intent = new Intent(getApplicationContext(),FavoritesRestaurants.class);
            startActivity(intent);
        }else if(id == R.id.action_historic){
            Intent intent = new Intent(getApplicationContext(),HistoricActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
