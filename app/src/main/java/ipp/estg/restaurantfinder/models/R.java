package ipp.estg.restaurantfinder.models;

import com.google.gson.annotations.SerializedName;

public class R {

    @SerializedName("has_menu_status")
    private HasMenuStatus hasMenuStatus;
    @SerializedName("res_id")
    private int resId;
    @SerializedName("is_grocery_store")
    private boolean isGroceryStore;

    public HasMenuStatus getHasMenuStatus() {
        return hasMenuStatus;
    }

    public void setHasMenuStatus(HasMenuStatus hasMenuStatus) {
        this.hasMenuStatus = hasMenuStatus;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public boolean isGroceryStore() {
        return isGroceryStore;
    }

    public void setGroceryStore(boolean groceryStore) {
        isGroceryStore = groceryStore;
    }

    @Override
    public String toString() {
        return "R{" +
                "hasMenuStatus=" + hasMenuStatus +
                ", resId=" + resId +
                ", isGroceryStore=" + isGroceryStore +
                '}';
    }
}
