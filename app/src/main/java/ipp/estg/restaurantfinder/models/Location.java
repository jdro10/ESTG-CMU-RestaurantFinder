package ipp.estg.restaurantfinder.models;

public class Location {

    private String address;
    private String locality;
    private String city;
    private float latitude;
    private float longitude;
    private int zipcode;

    public Location(String address, String locality, String city, float latitude, float longitude, int zipcode) {
        this.address = address;
        this.locality = locality;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.zipcode = zipcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public String toString() {
        return "Location{" +
                "address='" + address + '\'' +
                ", locality='" + locality + '\'' +
                ", city='" + city + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", zipcode=" + zipcode +
                '}';
    }
}
