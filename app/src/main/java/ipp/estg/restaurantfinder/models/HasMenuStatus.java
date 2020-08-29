package ipp.estg.restaurantfinder.models;

public class HasMenuStatus {

    private int delivery;
    private int takeaway;

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }

    public int getTakeaway() {
        return takeaway;
    }

    public void setTakeaway(int takeaway) {
        this.takeaway = takeaway;
    }

    @Override
    public String toString() {
        return "HasMenuStatus{" +
                "delivery=" + delivery +
                ", takeaway=" + takeaway +
                '}';
    }
}
