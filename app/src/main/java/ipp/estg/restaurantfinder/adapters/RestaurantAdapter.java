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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ipp.estg.restaurantfinder.R;
import ipp.estg.restaurantfinder.activities.RestaurantSelected;
import ipp.estg.restaurantfinder.db.RestaurantDB;
import ipp.estg.restaurantfinder.db.RestaurantRoom;
import ipp.estg.restaurantfinder.models.Restaurants;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> implements Filterable {

    private Context context;
    private List<Restaurants> restaurants;
    private List<Restaurants> allRestaurants;
    private final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(1);
    private RestaurantDB db;
    private List<RestaurantRoom> favorites;

    private void makeFavorite(RestaurantRoom restaurantRoom) {

        db = Room.databaseBuilder(context, RestaurantDB.class, "RestaurantsDB").build();
        databaseWriterExecutor.execute(() -> {
            db.daoAccess().insertRestaurant(restaurantRoom);
        });
    }

    public RestaurantAdapter(Context context, List<Restaurants> restaurants, List<RestaurantRoom> favorites) {
        this.context = context;
        this.restaurants = restaurants;
        this.favorites = favorites;
        this.allRestaurants = restaurants;
    }

    public void setRestaurants(List<Restaurants> restaurants) {
        this.restaurants = restaurants;
        this.allRestaurants.addAll(restaurants);
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View restaurantView = inflater.inflate(R.layout.restaurant_list_layout, parent, false);

        return new RestaurantViewHolder(restaurantView);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {

        Restaurants restaurant = this.restaurants.get(position);

        Button call_button = holder.call;
        TextView nameTextView = holder.name;
        TextView addressTextView = holder.address;
        ImageView favorite = holder.favorite;
        ImageView photo = holder.photo;

        nameTextView.setText(restaurant.getRestaurant().getName());
        addressTextView.setText(restaurant.getRestaurant().getLocation().getAddress());

        if (restaurant.getRestaurant().getThumb().equals("")) {
            new GetRestaurantImage(holder.favorite).execute(restaurant.getRestaurant().getId());
            holder.photo.setImageResource(R.drawable.no_image);
        } else {
            new GetRestaurantImage(holder.photo, holder.favorite).execute(restaurant.getRestaurant().getThumb(), restaurant.getRestaurant().getId());
        }

        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", restaurant.getRestaurant().getPhoneNumbers(), null));
                context.startActivity(phoneIntent);
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RestaurantSelected.class);
                intent.putExtra("res_id", restaurant.getRestaurant().getId());
                intent.putExtra("thumb", restaurant.getRestaurant().getThumb());
                context.startActivity(intent);
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RestaurantRoom tmp = new RestaurantRoom(restaurant.getRestaurant().getId(),
                        restaurant.getRestaurant().getName(),
                        restaurant.getRestaurant().getThumb(),
                        restaurant.getRestaurant().getPhoneNumbers(),
                        restaurant.getRestaurant().getUrl(),
                        restaurant.getRestaurant().getLocation().getAddress());

                makeFavorite(tmp);
                favorite.setImageResource(R.drawable.favorite);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.restaurants.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return cuisineFilter;
    }

    private Filter cuisineFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Restaurants> filteredRestaurants = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0) {
                filteredRestaurants.addAll(allRestaurants);
                restaurants.addAll(allRestaurants);
            } else {
                String cuisineType = charSequence.toString().toLowerCase().trim();

                for(Restaurants restaurant: allRestaurants) {
                    if(restaurant.getRestaurant().getCuisines().toLowerCase().contains(cuisineType)) {
                        filteredRestaurants.add(restaurant);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredRestaurants;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            restaurants.clear();
            restaurants.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class RestaurantViewHolder extends RecyclerView.ViewHolder {

        public TextView address, name;
        public Button call;
        public ImageView photo, favorite;

        public RestaurantViewHolder(@NonNull View itemView) {
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
        private ImageView favorite;

        public GetRestaurantImage(ImageView imageView, ImageView favorite) {
            this.imageView = imageView;
            this.favorite = favorite;
        }

        public GetRestaurantImage(ImageView favorite) {
            this.favorite = favorite;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;


            if (imageView != null) {
                if (check(strings[1])) {
                    favorite.setImageResource(R.drawable.favorite);
                } else {
                    favorite.setImageResource(R.drawable.favorite_border);
                }

                try {
                    InputStream inputStream = new java.net.URL(strings[0]).openStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (check(strings[0])) {
                    favorite.setImageResource(R.drawable.favorite);
                } else {
                    favorite.setImageResource(R.drawable.favorite_border);
                }
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (imageView != null) {
                imageView.setImageBitmap(result);
            }
        }
    }

    private boolean check(String id) {

        for (RestaurantRoom restaurant : this.favorites) {
            if (restaurant.getId().equals(id)) {
                return true;
            }
        }

        return false;
    }
}
