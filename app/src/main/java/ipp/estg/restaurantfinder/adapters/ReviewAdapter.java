package ipp.estg.restaurantfinder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import ipp.estg.restaurantfinder.R;
import ipp.estg.restaurantfinder.db.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private List<Review> reviews;

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View reviewView = inflater.inflate(R.layout.comment_layout, parent, false);

        return new ReviewAdapter.ReviewViewHolder(reviewView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        final Review review = this.reviews.get(position);

        TextView foodRate = holder.foodRate;
        TextView personName = holder.personName;
        TextView cleanRate = holder.cleanRate;
        TextView comment = holder.comment;
        ImageView photo = holder.photo;

        foodRate.setText("Food Rate: " + review.getFoodRate());
        cleanRate.setText("Clean Rate: " + review.getCleanRate());
        comment.setText(review.getComment());
        personName.setText(review.getUserId());
        photo.setImageResource(R.drawable.someone);
    }

    @Override
    public int getItemCount() {
        return this.reviews.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        public TextView comment;
        public TextView foodRate;
        public TextView cleanRate;
        public TextView personName;
        public ImageView photo;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            this.foodRate = itemView.findViewById(R.id.food_rate);
            this.cleanRate = itemView.findViewById(R.id.clean_rate);
            this.personName = itemView.findViewById(R.id.person_id);
            this.comment = itemView.findViewById(R.id.restaurant_comment);
            this.photo = itemView.findViewById(R.id.person_icon);
        }
    }
}
