package ipp.estg.restaurantfinder.fragments;

import android.content.Context;
import android.content.Intent;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.util.Objects;

import ipp.estg.restaurantfinder.R;
import ipp.estg.restaurantfinder.activities.MapActivity;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getActivity();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("reviews");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.restaurant_layout, container, false);

        restaurant_selected = contentView.findViewById(R.id.restaurant_selected);
        restaurantImage = contentView.findViewById(R.id.restaurant_selected_image);
        mapButton = contentView.findViewById(R.id.open_restaurant_map);
        rateButton = contentView.findViewById(R.id.rate_restaurant_selected);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://developers.zomato.com/api/v2.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ZomatoApi zomatoapi = retrofit.create(ZomatoApi.class);

        Call<Restaurant> call = zomatoapi.getRestaurant(Objects.requireNonNull(getActivity().getIntent().getExtras().getString("res_id")));

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

                    getActivity().findViewById(R.id.loadingPanelRestaurantDetails).setVisibility(View.GONE);
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
        RadioGroup foodRate = view.findViewById(R.id.food_rate);
        RadioGroup cleanRate = view.findViewById(R.id.clean_rate);
        Button submit = view.findViewById(R.id.submit_button);

        alert.setView(view);

        AlertDialog dialog = alert.create();
        dialog.show();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(comment.getText().toString())){

                    String id = ref.push().getKey();
                    /*Review review = new Review(name.getText().toString(),"algum restaurante",comment.getText().toString(),foodRate.getCheckedRadioButtonId(),cleanRate.getCheckedRadioButtonId());*/
                    Review review = new Review(name.getText().toString(),restaurantName,comment.getText().toString(),5,5);
                    //Log.d("teste",foodRate.getCheckedRadioButtonId()+"");
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
}
