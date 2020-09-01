package ipp.estg.restaurantfinder.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ipp.estg.restaurantfinder.R;
import ipp.estg.restaurantfinder.activities.RestaurantSelected;
import ipp.estg.restaurantfinder.db.RestaurantDB;
import ipp.estg.restaurantfinder.db.RestaurantRoom;

public class FavoriteRestaurantAdapter extends RecyclerView.Adapter<FavoriteRestaurantAdapter.FavoriteRestaurantViewHolder> {

    private Context context;
    private List<RestaurantRoom> restaurants;
    private final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(1);
    private RestaurantDB db;

    private void deleteRestaurants(String id) {

        db = Room.databaseBuilder(context, RestaurantDB.class, "RestaurantsDB").build();
        databaseWriterExecutor.execute(() -> {
            db.daoAccess().deleteRestaurant(id);
        });
    }

    public FavoriteRestaurantAdapter(Context context, List<RestaurantRoom> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public FavoriteRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View restaurantView = inflater.inflate(R.layout.restaurant_list_layout, parent, false);

        return new FavoriteRestaurantViewHolder(restaurantView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteRestaurantViewHolder holder, int position) {

        RestaurantRoom restaurant = this.restaurants.get(position);

        Button call_button = holder.call;
        TextView nameTextView = holder.name;
        TextView addressTextView = holder.address;
        ImageView photo = holder.photo;
        ImageView favorite = holder.favorite;


        nameTextView.setText(restaurant.getName());
        addressTextView.setText(restaurant.getAddress());
        favorite.setImageResource(R.drawable.delete);

        if (restaurant.getThumb().equals("")) {
            photo.setImageResource(R.drawable.no_image);
        } else {
            new GetRestaurantImage(holder.photo).execute(restaurant.getThumb());
        }

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RestaurantSelected.class);
                intent.putExtra("res_id", restaurant.getId());
                context.startActivity(intent);
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRestaurants(restaurant.getId());

                restaurants.remove(position);
                notifyDataSetChanged();
            }
        });

        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", restaurant.getPhoneNumber(), null));
                context.startActivity(phoneIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.restaurants.size();
    }

    public class FavoriteRestaurantViewHolder extends RecyclerView.ViewHolder {

        public TextView address, name;
        public Button call;
        public ImageView photo, favorite;

        public FavoriteRestaurantViewHolder(@NonNull View itemView) {
            super(itemView);

            address = itemView.findViewById(R.id.restaurant_address);
            name = itemView.findViewById(R.id.restaurant_name);
            photo = itemView.findViewById(R.id.restaurant_icon);
            call = itemView.findViewById(R.id.call_button);
            favorite = itemView.findViewById(R.id.isFavoriteOrNot);
        }
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