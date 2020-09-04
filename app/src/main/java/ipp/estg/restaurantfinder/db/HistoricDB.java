package ipp.estg.restaurantfinder.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {HistoricRoom.class}, version = 1)
public abstract class HistoricDB extends RoomDatabase {

    public abstract HistoricRoom.HistoricRoomDao daoAccess();

}
