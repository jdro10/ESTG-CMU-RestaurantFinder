package ipp.estg.restaurantfinder.db;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Query;

@Entity
public class RestaurantRoom {

    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String thumb;
    private String phoneNumber;
    private String url;
    private String address;

    public RestaurantRoom(String id, String name, String thumb, String phoneNumber, String url, String address) {
        this.id = id;
        this.name = name;
        this.thumb = thumb;
        this.phoneNumber = phoneNumber;
        this.url = url;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Dao
    public interface RestaurantRoomDao{
        @Query("SELECT * FROM RestaurantRoom")
        public RestaurantRoom[] getAll();

        @Query("SELECT* FROM RestaurantRoom WHERE phoneNumber = (:phoneNumber)")
        public RestaurantRoom getRestaurantFromPhoneNumber(int phoneNumber);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        public void insertRestaurant(RestaurantRoom restaurantRoom);

        @Query("DELETE FROM RestaurantRoom WHERE id = :id")
        public void deleteRestaurant(String id);

        @Query("SELECT* FROM RestaurantRoom WHERE address LIKE :address ")
        public RestaurantRoom getRestaurantFromAddress(String address);
        //TEMOS Q PASSAR A STRING COM %ADDRESS%

    }


}
