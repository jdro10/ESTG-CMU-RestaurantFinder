package ipp.estg.restaurantfinder.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import ipp.estg.restaurantfinder.R;
import ipp.estg.restaurantfinder.models.Location;
import ipp.estg.restaurantfinder.services.LocationService;

import static ipp.estg.restaurantfinder.services.LocationService.CHANNEL_ID;

public class PreferencesActivity extends AppCompatActivity {

    private Switch notificationSwitch;
    private RadioButton size10;
    private RadioButton size30;
    private RadioButton size50;
    private RadioButton size100;
    private RadioGroup radiusSize;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static final String SHARED_PREF_NAME = "app_pref";
    public static final String KEY_NOTIFICATION = "notification";
    public static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_SWITCH = "switch";
    public static final String KEY_RADIUS = "radius";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.notificationSwitch = findViewById(R.id.toggle_notification);
        this.radiusSize = findViewById(R.id.radius_group);
        this.size10 = findViewById(R.id.size10);
        this.size30 = findViewById(R.id.size30);
        this.size50 = findViewById(R.id.size50);
        this.size100 = findViewById(R.id.size100);
        this.sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        this.editor = sharedPreferences.edit();

        if (this.sharedPreferences.getString(KEY_SWITCH, "").equals("on")) {
            this.notificationSwitch.setChecked(true);
        } else {
            this.notificationSwitch.setChecked(false);
        }

        if (this.sharedPreferences.getString(KEY_RADIUS, "").equals("10000")) {
            this.size10.setChecked(true);
        } else if (this.sharedPreferences.getString(KEY_RADIUS, "").equals("30000")) {
            this.size30.setChecked(true);
        } else if (this.sharedPreferences.getString(KEY_RADIUS, "").equals("50000")) {
            this.size50.setChecked(true);
        } else if (this.sharedPreferences.getString(KEY_RADIUS, "").equals("100000")) {
            this.size100.setChecked(true);
        } else {
            this.size30.setChecked(true);
        }

        this.notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    editor.putString(KEY_NOTIFICATION, "true");
                    editor.putString(KEY_SWITCH, "on");

                    startService(new Intent(PreferencesActivity.this, LocationService.class));
                } else {
                    editor.putString(KEY_NOTIFICATION, "false");
                    editor.putString(KEY_SWITCH, "off");

                    stopService(new Intent(PreferencesActivity.this, LocationService.class));
                }
                editor.apply();
            }
        });

        this.radiusSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (id == size10.getId()) {
                    editor.putString(KEY_RADIUS, "10000");
                } else if (id == size30.getId()) {
                    editor.putString(KEY_RADIUS, "30000");
                } else if (id == size50.getId()) {
                    editor.putString(KEY_RADIUS, "50000");
                } else if (id == size100.getId()) {
                    editor.putString(KEY_RADIUS, "100000");
                }
                editor.commit();
            }
        });
    }

    public static String getNotificationPreference(String key, Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, null);
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
}