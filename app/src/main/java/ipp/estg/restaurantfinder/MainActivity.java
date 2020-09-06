package ipp.estg.restaurantfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ipp.estg.restaurantfinder.activities.AuthenticationActivity;
import ipp.estg.restaurantfinder.activities.FavoritesRestaurants;
import ipp.estg.restaurantfinder.activities.NearbyRestaurants;
import ipp.estg.restaurantfinder.activities.PreferencesActivity;
import ipp.estg.restaurantfinder.activities.RestaurantSelected;

import ipp.estg.restaurantfinder.activities.WebViewActivity;
import ipp.estg.restaurantfinder.db.HistoricDB;
import ipp.estg.restaurantfinder.db.HistoricRoom;
import ipp.estg.restaurantfinder.db.Review;
import ipp.estg.restaurantfinder.services.LocationService;

import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ipp.estg.restaurantfinder.activities.PreferencesActivity.KEY_NOTIFICATION;
import static ipp.estg.restaurantfinder.activities.PreferencesActivity.SHARED_PREF_NAME;

public class MainActivity extends AppCompatActivity {

    private TextView textView;


    private Button send,picture_btn,manda_foto;
    private TextView comentario,nomePessoa;
    private Button send;
    private TextView comentario, nomePessoa;
    DatabaseReference ref;
    private final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(1);
    private HistoricDB db;
    private SharedPreferences sharedPreferences;
    ImageView image ;
    private static final int CAMERA_PIC_REQUEST = 1337;


    private void makeHistoric() {

        Log.d("ENTREI AQUI MANUUUU", "yah manuh");
        HistoricRoom historic = new HistoricRoom("restaurante", " mm", "date", "food", 22.4);
        db = Room.databaseBuilder(getApplicationContext(), HistoricDB.class, "HistoricsDB").build();
        databaseWriterExecutor.execute(() -> {
            db.daoAccess().insertHistoric(historic);
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            ImageView imageview = (ImageView) findViewById(R.id.picture_taken); //sets imageview as the bitmap
            imageview.setImageBitmap(image);

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeHistoric();



        picture_btn = findViewById(R.id.picture_btn);




        picture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });



        Log.d("antes", "antes");



        Log.d("depois", "depois");

        send = findViewById(R.id.send);


        Button button = findViewById(R.id.loginActivityButton);
        Button button1 = findViewById(R.id.restaurantButton);

        Button button3 = findViewById(R.id.restaurant_details);
        Button button4 = findViewById(R.id.startService);

        Button button6 = findViewById(R.id.preferences_button);

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



        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PreferencesActivity.class);
                startActivity(intent);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "MANDEI DADOS", Toast.LENGTH_SHORT).show();
                addReview();
            }
        });
    }

    public void addReview() {

        if (!TextUtils.isEmpty(nomePessoa.getText().toString()) && !TextUtils.isEmpty(comentario.getText().toString())) {

            String id = ref.push().getKey();
            Review review = new Review(nomePessoa.getText().toString(), "algum restaurante", comentario.getText().toString(), 5, 5);
            ref.child(id).setValue(review);
            nomePessoa.setText("");
            comentario.setText("");

        } else {
            Toast.makeText(MainActivity.this, "Please type restaurant review!", Toast.LENGTH_SHORT);

        }
    }

    public void startService(View v) {
        this.sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String strNotification = this.sharedPreferences.getString(KEY_NOTIFICATION, "");

        if (strNotification.equals("true")) {
            Intent serviceIntent = new Intent(this, LocationService.class);
            startService(serviceIntent);
        } else {
            Toast.makeText(getApplicationContext(), "Please enable notifications to use this service", Toast.LENGTH_LONG).show();
        }
    }




}