package ipp.estg.restaurantfinder.models;

public class Restaurant {

    private String apikey;
    private String id;
    private String url;
    private Location location;

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "apikey='" + apikey + '\'' +
                ", id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", location=" + location +
                '}';
    }
}
