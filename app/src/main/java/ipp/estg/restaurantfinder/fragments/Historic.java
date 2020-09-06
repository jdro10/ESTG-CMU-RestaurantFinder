package ipp.estg.restaurantfinder.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ipp.estg.restaurantfinder.R;
import ipp.estg.restaurantfinder.adapters.HistoricAdapter;
import ipp.estg.restaurantfinder.db.HistoricDB;
import ipp.estg.restaurantfinder.db.HistoricRoom;

public class Historic extends Fragment {

    private Context context;
    private HistoricDB db;
    private RecyclerView recyclerView;
    private HistoricAdapter historicAdapter;
    private List<HistoricRoom> historicRoomList;
    private final ExecutorService databaseReadExecutor = Executors.newFixedThreadPool(1);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getActivity();
        this.historicRoomList = new ArrayList<>();
        this.getHistoric();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.reviews_fragment, container, false);

        this.recyclerView = contentView.findViewById(R.id.historicRecyclerView);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(contentView.getContext()));
        this.historicAdapter = new HistoricAdapter(this.context, historicRoomList);
        this.recyclerView.setAdapter(this.historicAdapter);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL));
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.context));

        return contentView;
    }

    private void getHistoric() {
        db = Room.databaseBuilder(context, HistoricDB.class, "HistoricsDB").build();
        databaseReadExecutor.execute(() -> {
            historicRoomList.addAll(Arrays.asList(db.daoAccess().getAll()));
        });
    }
}
