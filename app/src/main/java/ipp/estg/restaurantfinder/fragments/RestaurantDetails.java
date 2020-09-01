package ipp.estg.restaurantfinder.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

import ipp.estg.restaurantfinder.R;
import ipp.estg.restaurantfinder.activities.MapActivity;
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
    private Button mapButton;
    private Location location;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.restaurant_layout, container, false);

        restaurant_selected = contentView.findViewById(R.id.restaurant_selected);
        restaurantImage = contentView.findViewById(R.id.restaurant_selected_image);
        mapButton = contentView.findViewById(R.id.open_restaurant_map);

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

        return contentView;
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
