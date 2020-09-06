package ipp.estg.restaurantfinder.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ipp.estg.restaurantfinder.R;
import ipp.estg.restaurantfinder.db.HistoricRoom;

public class HistoricAdapter extends RecyclerView.Adapter<HistoricAdapter.HistoricViewHolder> {

    private Context context;
    private List<HistoricRoom> historic;

    public HistoricAdapter(Context context, List<HistoricRoom> historic) {
        this.context = context;
        this.historic = historic;
    }


    @NonNull
    @Override
    public HistoricViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View historicView = inflater.inflate(R.layout.one_historic,parent,false);

        return new HistoricAdapter.HistoricViewHolder(historicView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoricViewHolder holder, int position) {

        final HistoricRoom historic = this.historic.get(position);

        holder.restaurant.setText(historic.getRestaurantName());
        holder.distance.setText(historic.getDistance()+"");
        holder.date.setText(historic.getDate());
        holder.type_food.setText(historic.getType());

    }

    @Override
    public int getItemCount() {
        return historic.size();
    }


    public class HistoricViewHolder extends RecyclerView.ViewHolder{

        public TextView restaurant, distance, date , type_food;

        public HistoricViewHolder(@NonNull View itemView){
            super(itemView);

            this.restaurant = itemView.findViewById(R.id.restaurant_id);
            this.distance = itemView.findViewById(R.id.distance);
            this.date = itemView.findViewById(R.id.date_food);
            this.type_food = itemView.findViewById(R.id.type_food);
        }
    }

}
