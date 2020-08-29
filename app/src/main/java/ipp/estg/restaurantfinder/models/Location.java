package ipp.estg.restaurantfinder.models;

public class Location {

    private String address;
    private String locality;
    private String city;
    private int city_id;
    private String latitude;
    private String longitude;
    private String zipcode;
    private int country_id;
    private String locality_verbose;

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

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public String getLocality_verbose() {
        return locality_verbose;
    }

    public void setLocality_verbose(String locality_verbose) {
        this.locality_verbose = locality_verbose;
    }

    @Override
    public String toString() {
        return "Location{" +
                "address='" + address + '\'' +
                ", locality='" + locality + '\'' +
                ", city='" + city + '\'' +
                ", city_id=" + city_id +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", country_id=" + country_id +
                ", locality_verbose='" + locality_verbose + '\'' +
                '}';
    }
}
