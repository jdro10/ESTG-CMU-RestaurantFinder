package ipp.estg.restaurantfinder.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ipp.estg.restaurantfinder.models.Restaurant;

@Database(entities = {RestaurantRoom.class},version = 1)
public abstract class RestaurantDB extends RoomDatabase {

    public abstract RestaurantRoom.RestaurantRoomDao daoAccess();

}
