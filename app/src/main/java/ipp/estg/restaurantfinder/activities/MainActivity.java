package ipp.estg.restaurantfinder.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ipp.estg.restaurantfinder.R;
import ipp.estg.restaurantfinder.db.Review;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private Button send;
    private TextView comentario,nomePessoa;
    DatabaseReference ref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Log.d("antes", "antes");
        ref = database.getReference("reviews");


        Log.d("depois", "depois");

        send = findViewById(R.id.send);
        comentario = findViewById(R.id.comentario);
        nomePessoa = findViewById(R.id.nome_pessoa);

        Button button = findViewById(R.id.loginActivityButton);
        Button button1 = findViewById(R.id.restaurantButton);
        Button button2 = findViewById(R.id.favoritesActivity);
        Button button3 = findViewById(R.id.restaurant_details);

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

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "MANDEI DADOS", Toast.LENGTH_SHORT).show();
                addReview();
            }
        });



    }

    public void addReview(){

        if(!TextUtils.isEmpty(nomePessoa.getText().toString()) && !TextUtils.isEmpty(comentario.getText().toString())){

            String id = ref.push().getKey();
            Review review = new Review(nomePessoa.getText().toString(),"algum restaurante",comentario.getText().toString(),5,5);
            ref.child(id).setValue(review);
            nomePessoa.setText("");
            comentario.setText("");

        }else{
            Toast.makeText(MainActivity.this,"Please type restaurant review!",Toast.LENGTH_SHORT);

        }

    }
}