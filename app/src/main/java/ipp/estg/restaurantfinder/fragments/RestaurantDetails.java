package ipp.estg.restaurantfinder.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import androidx.room.Room;

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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ipp.estg.restaurantfinder.R;
import ipp.estg.restaurantfinder.activities.MapActivity;
import ipp.estg.restaurantfinder.activities.WebViewActivity;
import ipp.estg.restaurantfinder.adapters.ReviewAdapter;
import ipp.estg.restaurantfinder.db.HistoricDB;
import ipp.estg.restaurantfinder.db.HistoricRoom;
import ipp.estg.restaurantfinder.db.Review;
import ipp.estg.restaurantfinder.helpers.RetrofitHelper;
import ipp.estg.restaurantfinder.interfaces.ZomatoApi;
import ipp.estg.restaurantfinder.models.Location;
import ipp.estg.restaurantfinder.models.Restaurant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDetails extends Fragment {

    DatabaseReference ref;
    private Context context;
    private TextView restaurant_selected;
    private ImageView restaurantImage;
    private Button mapButton, rateButton;
    private Button menuButton;
    private Location location;
    private String restaurantName;
    private int cleanRateNumber, foodRateNumber = 0;
    private RadioGroup foodRate;
    private RadioGroup cleanRate;
    private RadioGroup mealChoosed;
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
    private double countFood, countClean;
    private double totalRateFood, totalRateClean;
    private double meanFood, meanClean;
    private TextView cleanRateText, foodRateText, avgRateText;
    private TextView distanceTextView;
    private RatingBar ratingbar;
    private String food;
    private HistoricDB db;
    private String menuUrl;
    private final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(1);

    private void makeHistoric(HistoricRoom historicRoom) {
        db = Room.databaseBuilder(context, HistoricDB.class, "HistoricsDB").build();
        databaseWriterExecutor.execute(() -> {
            db.daoAccess().insertHistoric(historicRoom);
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getActivity();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.ref = database.getReference("reviews");
        this.reviewList = new ArrayList<>();
        this.restaurantID = getActivity().getIntent().getExtras().getString("res_id");
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context.getApplicationContext());
        getLastLocation();
    }

    @Override
    public void onStart() {
        super.onStart();

        this.ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewList.clear();
                countFood = countClean = 0;
                totalRateClean = totalRateFood = 0;
                for (DataSnapshot review : snapshot.getChildren()) {
                    Review r = review.getValue(Review.class);
                    if (r.getRestaurantId().equals(restaurantID)) {
                        reviewList.add(r);
                        countClean++;
                        countFood++;
                        totalRateClean += r.getCleanRate();
                        totalRateFood += r.getFoodRate();
                    }
                }
                meanClean = totalRateClean / countClean;
                meanFood = totalRateFood / countFood;
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

        this.restaurant_selected = contentView.findViewById(R.id.restaurant_selected);
        this.restaurantImage = contentView.findViewById(R.id.restaurant_selected_image);
        this.mapButton = contentView.findViewById(R.id.open_restaurant_map);
        this.rateButton = contentView.findViewById(R.id.rate_restaurant_selected);
        this.ratingbar = contentView.findViewById(R.id.ratingBar);
        this.cleanRateText = contentView.findViewById(R.id.clean_rate);
        this.foodRateText = contentView.findViewById(R.id.food_rate);
        this.distanceTextView = contentView.findViewById(R.id.distance_text_view);
        this.avgRateText = contentView.findViewById(R.id.avg_rate);
        this.menuButton = contentView.findViewById(R.id.check_menu_restaurant);
        this.recyclerView = contentView.findViewById(R.id.classifications_restaurant_selected);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.reviewAdapter = new ReviewAdapter(this.context, this.reviewList);
        this.recyclerView.setAdapter(this.reviewAdapter);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL));
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.context));

        ZomatoApi zomatoapi = RetrofitHelper.getRetrofit().create(ZomatoApi.class);
        Call<Restaurant> call = zomatoapi.getIndividualRestaurant(Objects.requireNonNull(restaurantID));

        call.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                if (response.isSuccessful()) {
                    restaurant_selected.setText(response.body().getName());
                    restaurantName = response.body().getName();
                    menuUrl = response.body().getMenuUrl();

                    if (response.body().getFeaturedImage().equals("")) {
                        restaurantImage.setImageResource(R.drawable.no_image);
                    } else {
                        new GetRestaurantImage(restaurantImage).execute(response.body().getFeaturedImage());
                    }

                    location = response.body().getLocation();

                    restaurantLatitude = Double.parseDouble(location.getLatitude());
                    restaurantLongitude = Double.parseDouble(location.getLongitude());
                    double distanceBetweenUserAndRestaurant = distance(restaurantLatitude, restaurantLongitude, currentLatitude, currentLongitude);

                    if (distanceBetweenUserAndRestaurant > 100) {
                        rateButton.setEnabled(false);
                    }

                    DecimalFormat df = new DecimalFormat("#");
                    distanceBetweenUserAndRestaurant = Double.valueOf(df.format(distanceBetweenUserAndRestaurant));

                    distanceTextView.setText("Distância: " + distanceBetweenUserAndRestaurant + "km");

                    double mean = (meanClean + meanFood) / 2;

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

        this.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent webViewintent = new Intent(getContext(), WebViewActivity.class);
                webViewintent.putExtra("url", menuUrl);
                startActivity(webViewintent);
            }
        });

        this.mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(getActivity(), MapActivity.class);
                mapIntent.putExtra("latitude", location.getLatitude());
                mapIntent.putExtra("longitude", location.getLongitude());

                startActivity(mapIntent);
            }
        });

        this.rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateDialog();
            }
        });

        return contentView;
    }

    private void rateDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        alert.setTitle("Rate");

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.rate_dialog, null);

        EditText name = view.findViewById(R.id.person_name);
        EditText comment = view.findViewById(R.id.comment);
        Button submit = view.findViewById(R.id.submit_button);
        this.foodRate = view.findViewById(R.id.food_group);
        this.cleanRate = view.findViewById(R.id.clean_group);
        this.mealChoosed = view.findViewById(R.id.meal_group);
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
        RadioButton meal1 = view.findViewById(R.id.meal1);
        RadioButton meal2 = view.findViewById(R.id.meal2);
        RadioButton meal3 = view.findViewById(R.id.meal3);
        RadioButton meal4 = view.findViewById(R.id.meal4);
        RadioButton meal5 = view.findViewById(R.id.meal5);
        EditText numberKM = view.findViewById(R.id.number_kms);
        EditText foodEated = view.findViewById(R.id.food_eated);

        alert.setView(view);

        AlertDialog dialog = alert.create();
        dialog.show();

        this.mealChoosed.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (id == meal5.getId()) {
                    foodEated.setVisibility(View.VISIBLE);
                } else {
                    foodEated.setVisibility(View.INVISIBLE);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(comment.getText().toString())) {

                    int selectedFoodId = foodRate.getCheckedRadioButtonId();
                    int selectedCleanId = cleanRate.getCheckedRadioButtonId();
                    int selectedMealChoosed = mealChoosed.getCheckedRadioButtonId();

                    if (selectedFoodId == food1.getId()) {
                        foodRateNumber = 1;
                    } else if (selectedFoodId == food2.getId()) {
                        foodRateNumber = 2;
                    } else if (selectedFoodId == food3.getId()) {
                        foodRateNumber = 3;
                    } else if (selectedFoodId == food4.getId()) {
                        foodRateNumber = 4;
                    } else if (selectedFoodId == food5.getId()) {
                        foodRateNumber = 5;
                    }

                    if (selectedCleanId == clean1.getId()) {
                        cleanRateNumber = 1;
                    } else if (selectedCleanId == clean2.getId()) {
                        cleanRateNumber = 2;
                    } else if (selectedCleanId == clean3.getId()) {
                        cleanRateNumber = 3;
                    } else if (selectedCleanId == clean4.getId()) {
                        cleanRateNumber = 4;
                    } else if (selectedCleanId == clean5.getId()) {
                        cleanRateNumber = 5;
                    }

                    if (selectedMealChoosed == meal1.getId()) {
                        food = "HotDog";
                    } else if (selectedMealChoosed == meal2.getId()) {
                        food = "Lasagna";
                    } else if (selectedMealChoosed == meal3.getId()) {
                        food = "Pizza";
                    } else if (selectedMealChoosed == meal4.getId()) {
                        food = "Hamburger";
                    } else if (selectedMealChoosed == meal5.getId()) {
                        food = foodEated.getText().toString();
                    }

                    if (food.equals("") || food == null) {
                        food = "Not specified";
                    }

                    Calendar rightNow = Calendar.getInstance();

                    String date = rightNow.get(Calendar.DAY_OF_MONTH) + "/" + rightNow.get(Calendar.MONTH) + "/" + rightNow.get(Calendar.YEAR) + "    " + rightNow.get(Calendar.HOUR_OF_DAY) + ":" + rightNow.get(Calendar.MINUTE);

                    HistoricRoom historic = new HistoricRoom(restaurantID, restaurantName, date, food, Double.parseDouble(numberKM.getText().toString()));

                    makeHistoric(historic);

                    String id = ref.push().getKey();
                    Review review = new Review(name.getText().toString(), restaurantID, comment.getText().toString(), foodRateNumber, cleanRateNumber);
                    ref.child(id).setValue(review);
                    name.setText("");
                    comment.setText("");
                    reviewList.add(review);
                    reviewAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Please type restaurant review!", Toast.LENGTH_SHORT);

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
        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }

        this.fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<android.location.Location>() {
                    @Override
                    public void onSuccess(android.location.Location location) {
                        if (location != null) {
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
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
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
