package ipp.estg.restaurantfinder.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import ipp.estg.restaurantfinder.R;
import ipp.estg.restaurantfinder.activities.MapActivity;
import ipp.estg.restaurantfinder.adapters.ReviewAdapter;
import ipp.estg.restaurantfinder.db.Review;
import ipp.estg.restaurantfinder.interfaces.ZomatoApi;
import ipp.estg.restaurantfinder.models.Location;
import ipp.estg.restaurantfinder.models.Restaurant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantDetails extends Fragment {

    private Context context;
    private TextView restaurant_selected;
    private ImageView restaurantImage;
    private Button mapButton,rateButton;
    private Location location;
    DatabaseReference ref;
    private String restaurantName;
    private int cleanRateNumber,foodRateNumber = 0;
    private RadioGroup foodRate;
    private RadioGroup cleanRate;
    private List<Review> reviewList;
    private String restaurantID;
    private RecyclerView recyclerView;
    private ReviewAdapter reviewAdapter;
    private static final int REQUEST_FINE_LOCATION = 100;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Double currentLatitude;
    private Double currentLongitude;
    private Double restaurantLatitude;
    private Double restaurantLongitude;
    private double countFood,countClean;
    private double totalRateFood,totalRateClean;
    private double meanFood,meanClean;
    private TextView cleanRateText, foodRateText,avgRateText;
    private RatingBar ratingbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getActivity();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("reviews");
        reviewList = new ArrayList<>();
        restaurantID = getActivity().getIntent().getExtras().getString("res_id");
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context.getApplicationContext());
        getLastLocation();
        Calendar rightNow = Calendar.getInstance();

        Log.d("teste",rightNow.get(Calendar.HOUR_OF_DAY) + "" + rightNow.get(Calendar.MINUTE));
    }

    @Override
    public void onStart() {
        super.onStart();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewList.clear();
                countFood = countClean = 0;
                totalRateClean = totalRateFood = 0;
                for(DataSnapshot review: snapshot.getChildren()){
                    Review r = review.getValue(Review.class);
                    if(r.getRestaurantId().equals(restaurantID)){
                        reviewList.add(r);
                        countClean++;
                        countFood++;
                        totalRateClean += r.getCleanRate();
                        totalRateFood += r.getFoodRate();
                    }
                }
                meanClean = totalRateClean/countClean;
                meanFood = totalRateFood/countFood;

                Log.d("VALORES","total pontuaçao tacho = " + totalRateFood + " /   " + "total votos " + countFood);

                Log.d("VALORES",meanClean + " / " + meanFood);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.restaurant_layout, container, false);

        restaurant_selected = contentView.findViewById(R.id.restaurant_selected);
        restaurantImage = contentView.findViewById(R.id.restaurant_selected_image);
        mapButton = contentView.findViewById(R.id.open_restaurant_map);
        rateButton = contentView.findViewById(R.id.rate_restaurant_selected);
        ratingbar=contentView.findViewById(R.id.ratingBar);
        cleanRateText = contentView.findViewById(R.id.clean_rate);
        foodRateText = contentView.findViewById(R.id.food_rate);
        avgRateText = contentView.findViewById(R.id.avg_rate);

        this.recyclerView = contentView.findViewById(R.id.classifications_restaurant_selected);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        this.reviewAdapter = new ReviewAdapter(this.context,this.reviewList);
        this.recyclerView.setAdapter(this.reviewAdapter);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL));
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.context));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://developers.zomato.com/api/v2.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ZomatoApi zomatoapi = retrofit.create(ZomatoApi.class);

        Call<Restaurant> call = zomatoapi.getRestaurant(Objects.requireNonNull(restaurantID));

        call.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                if (response.isSuccessful()) {
                    restaurant_selected.setText(response.body().getName());
                    restaurantName = response.body().getName();
                    if (response.body().getThumb().equals("")) {
                        restaurantImage.setImageResource(R.drawable.no_image);
                    } else {
                        new GetRestaurantImage(restaurantImage).execute(response.body().getThumb());
                    }

                    location = response.body().getLocation();

                    restaurantLatitude = Double.parseDouble(location.getLatitude());
                    restaurantLongitude = Double.parseDouble(location.getLongitude());
                    double distanceBetweenUserAndRestaurant = distance(restaurantLatitude,restaurantLongitude,currentLatitude,currentLongitude);
                    Log.d("teste",distanceBetweenUserAndRestaurant + "");
                    if(distanceBetweenUserAndRestaurant > 20){
                        rateButton.setEnabled(false);
                    }

                    double  mean = (meanClean + meanFood)/2;

                    Log.d("teste", mean + "" + "AQUI" + meanClean);

                    foodRateText.setText("Food Rate: " + meanFood);
                    cleanRateText.setText("Clean Rate: " + meanClean);
                    avgRateText.setText("Average Rate: " + mean);
                    

                    ratingbar.setRating((float) mean);

                    getActivity().findViewById(R.id.loadingPanelRestaurantDetails).setVisibility(View.INVISIBLE);
                    getActivity().findViewById(R.id.restaurant_details_linear_layout).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {

            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(getActivity(), MapActivity.class);
                mapIntent.putExtra("latitude", location.getLatitude());
                mapIntent.putExtra("longitude", location.getLongitude());

                startActivity(mapIntent);
            }
        });

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateDialog();
            }
        });

        return contentView;
    }

    private void rateDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_NoActionBar);
        alert.setTitle("Rate");

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.rate_dialog,null);

        EditText name = view.findViewById(R.id.person_name);
        EditText comment = view.findViewById(R.id.comment);
        Button submit = view.findViewById(R.id.submit_button);
        foodRate = view.findViewById(R.id.food_group);
        cleanRate = view.findViewById(R.id.clean_group);
        RadioButton food1 = view.findViewById(R.id.food1);
        RadioButton food2 = view.findViewById(R.id.food2);
        RadioButton food3 = view.findViewById(R.id.food3);
        RadioButton food4 = view.findViewById(R.id.food4);
        RadioButton food5 = view.findViewById(R.id.food5);
        RadioButton clean1 = view.findViewById(R.id.clean1);
        RadioButton clean2 = view.findViewById(R.id.clean2);
        RadioButton clean3 = view.findViewById(R.id.clean3);
        RadioButton clean4 = view.findViewById(R.id.clean4);
        RadioButton clean5 = view.findViewById(R.id.clean5);

        alert.setView(view);

        AlertDialog dialog = alert.create();
        dialog.show();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(comment.getText().toString())){

                    int selectedFoodId = foodRate.getCheckedRadioButtonId();
                    int selectedCleanId = cleanRate.getCheckedRadioButtonId();

                    if(selectedFoodId == food1.getId()){
                        foodRateNumber = 1;
                    }else if(selectedFoodId == food2.getId()){
                        foodRateNumber = 2;
                    }else if(selectedFoodId == food3.getId()){
                        foodRateNumber = 3;
                    }else if(selectedFoodId == food4.getId()){
                        foodRateNumber = 4;
                    }else if(selectedFoodId == food5.getId()){
                        foodRateNumber = 5;
                    }

                    if(selectedCleanId == clean1.getId()){
                        cleanRateNumber = 1;
                    }else if(selectedCleanId == clean2.getId()){
                        cleanRateNumber = 2;
                    }else if(selectedCleanId == clean3.getId()){
                        cleanRateNumber = 3;
                    }else if(selectedCleanId == clean4.getId()){
                        cleanRateNumber = 4;
                    }else if(selectedCleanId == clean5.getId()){
                        cleanRateNumber = 5;
                    }


                    String id = ref.push().getKey();
                    Review review = new Review(name.getText().toString(),restaurantID,comment.getText().toString(),foodRateNumber,cleanRateNumber);
                    ref.child(id).setValue(review);
                    name.setText("");
                    comment.setText("");

                }else{
                    Toast.makeText(context,"Please type restaurant review!",Toast.LENGTH_SHORT);

                }
                dialog.dismiss();
            }

        });

    }

    private class GetRestaurantImage extends AsyncTask<String, Void, Bitmap> {

        private ImageView imageView;

        public GetRestaurantImage(ImageView imageView) {
            this.imageView = imageView;
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
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }

    }
    private void getLastLocation() {
        if(ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<android.location.Location>() {
                    @Override
                    public void onSuccess(android.location.Location location) {
                        if(location != null){
                            currentLatitude = location.getLatitude();
                            currentLongitude = location.getLongitude();

                        }
                    }
                }).addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Couldn't get your location. Try again later!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        dist = dist * 1.609344;

        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
