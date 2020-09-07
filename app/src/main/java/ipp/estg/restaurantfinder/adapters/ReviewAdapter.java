package ipp.estg.restaurantfinder.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import ipp.estg.restaurantfinder.R;
import ipp.estg.restaurantfinder.db.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private List<Review> reviews;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRefDownload = storage.getReference("reviews");
    StorageReference pathReferenceDownload ;
    Bitmap bitmap;

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
        bitmap = null;
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
        if(review.getImageRef().equals("")){
            photo.setImageResource(R.drawable.someone);
        }else{
            new ReviewAdapter.GetUserImage(holder.photo).execute(review.getImageRef());
        }

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

    private class GetUserImage extends AsyncTask<String, Void, Bitmap> {

        private ImageView imageView;

        public GetUserImage(ImageView imageView) {
            this.imageView = imageView;
        }



        @Override
        protected Bitmap doInBackground(String... strings) {

            if (imageView != null) {
                Log.d("SACOU",strings[0]);
                pathReferenceDownload = storageRefDownload.child(strings[0]);
                try {
                    File localFile = File.createTempFile("tmp", ".jpg");
                    pathReferenceDownload.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            imageView.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            return bitmap;

        }

        @Override
        protected void onPostExecute(Bitmap result) {

        }
    }
}
