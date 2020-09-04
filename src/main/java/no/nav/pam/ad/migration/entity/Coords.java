package no.nav.pam.ad.migration.entity;

public class Coords {

    private String latitude;
    private String longitude;

    Coords() {
    }

    Coords(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

}
