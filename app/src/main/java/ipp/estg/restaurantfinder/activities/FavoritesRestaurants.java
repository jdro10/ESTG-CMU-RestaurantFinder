package ipp.estg.restaurantfinder.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ipp.estg.restaurantfinder.R;
import ipp.estg.restaurantfinder.adapters.FavoriteRestaurantAdapter;
import ipp.estg.restaurantfinder.fragments.FavoritesRestaurantList;
import ipp.estg.restaurantfinder.fragments.RestaurantDetails;
import ipp.estg.restaurantfinder.fragments.RestaurantsList;

import static ipp.estg.restaurantfinder.activities.PreferencesActivity.KEY_USER_EMAIL;
import static ipp.estg.restaurantfinder.activities.PreferencesActivity.SHARED_PREF_NAME;

public class FavoritesRestaurants extends AppCompatActivity implements FavoritesRestaurantList.FavoriteRestaurantsListFragmentListener {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FavoritesRestaurantList favoritesRestaurantList;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_restaurants);

        this.favoritesRestaurantList = new FavoritesRestaurantList();

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        this.editor = sharedPreferences.edit();

        if(isTablet()){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.newFragment3, this.favoritesRestaurantList).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.auth = FirebaseAuth.getInstance();
        this.user = this.auth.getCurrentUser();

        if(this.user.getEmail() == null){
            Intent intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
        this.overridePendingTransition(0, 0);
    }

    private boolean isTablet() {
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();

        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches *yInches);

        if (diagonalInches>= 7){
            return true;
        }else{
            return false;
        }
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
            this.auth.signOut();
            Intent intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void restaurantId(int id) {
        if(isTablet()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.newFragment4, new RestaurantDetails(String.valueOf(id)));
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }
}