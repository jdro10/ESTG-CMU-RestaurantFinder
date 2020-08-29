package ipp.estg.restaurantfinder.models;

public class R {

    private HasMenuStatus has_menu_status;
    private int res_id;
    private boolean is_grocery_store;

    public HasMenuStatus getHas_menu_status() {
        return has_menu_status;
    }

    public void setHas_menu_status(HasMenuStatus has_menu_status) {
        this.has_menu_status = has_menu_status;
    }

    public int getRes_id() {
        return res_id;
    }

    public void setRes_id(int res_id) {
        this.res_id = res_id;
    }

    public boolean isIs_grocery_store() {
        return is_grocery_store;
    }

    public void setIs_grocery_store(boolean is_grocery_store) {
        this.is_grocery_store = is_grocery_store;
    }

    @Override
    public String toString() {
        return "R{" +
                "has_menu_status=" + has_menu_status +
                ", res_id=" + res_id +
                ", is_grocery_store=" + is_grocery_store +
                '}';
    }
}
