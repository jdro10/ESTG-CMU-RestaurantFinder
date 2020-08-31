package ipp.estg.restaurantfinder.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
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
import java.util.concurrent.atomic.AtomicBoolean;

import ipp.estg.restaurantfinder.R;
import ipp.estg.restaurantfinder.db.RestaurantDB;
import ipp.estg.restaurantfinder.db.RestaurantRoom;
import ipp.estg.restaurantfinder.models.Restaurants;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private Context context;
    private List<Restaurants> restaurants;
    private  final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(1);
    private RestaurantDB db;

    private void makeFavorite(RestaurantRoom restaurantRoom){

        db = Room.databaseBuilder(context, RestaurantDB.class,"RestaurantsDB").build();
        databaseWriterExecutor.execute(() -> {
            db.daoAccess().insertRestaurant(restaurantRoom);
        });
    }

    public RestaurantAdapter(Context context, List<Restaurants> restaurants){
        this.context = context;
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("teste", "teste");

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View restaurantView = inflater.inflate(R.layout.restaurant_list_layout, parent, false);

        return new RestaurantViewHolder (restaurantView);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {

        final Restaurants restaurant = this.restaurants.get(position);

        Button call_button = holder.call;

        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", restaurant.getRestaurant().getPhoneNumbers(), null));
                context.startActivity(phoneIntent);
            }
        });

        TextView nameTextView = holder.name;
        TextView addressTextView = holder.address;

        nameTextView.setText(restaurant.getRestaurant().getName());
        addressTextView.setText(restaurant.getRestaurant().getLocation().getAddress());

        new GetRestaurantImage(holder.photo).execute(restaurant.getRestaurant().getThumb());

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/ //EM CASO DE QUEREMOS USAR O TOQUE NA LINHA

        final ImageView favorite = holder.favorite;

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

    public class RestaurantViewHolder extends RecyclerView.ViewHolder{

        public TextView address , name;
        public Button call;
        public ImageView photo,favorite;

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
