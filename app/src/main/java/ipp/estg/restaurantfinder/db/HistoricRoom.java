package ipp.estg.restaurantfinder.db;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Query;

@Entity
public class HistoricRoom {

    private String restaurantId;
    private String restaurantName;
    @PrimaryKey
    @NonNull
    private String date;
    private String type;
    private double distance;


    public HistoricRoom(String restaurantId, String restaurantName, String date, String type, double distance) {
        this.restaurantId = restaurantId;
        this.date = date;
        this.type = type;
        this.distance = distance;
        this.restaurantName = restaurantName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "HistoricRoom{" +
                "restaurantId='" + restaurantId + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                ", distance=" + distance +
                '}';
    }

    @Dao
    public interface HistoricRoomDao {
        @Query("SELECT * FROM HistoricRoom")
        public HistoricRoom[] getAll();

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        public void insertHistoric(HistoricRoom historicRoom);
    }
}
