package no.nav.pam.ad.migration.entity;


import javax.persistence.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Entity
@Table(name = "LOCATION")
@SequenceGenerator(name = "LOCATION_SEQ", sequenceName = "LOCATION_SEQ", allocationSize = 1)
public class Location {

    @Id
    @GeneratedValue(generator = "LOCATION_SEQ")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "PAMENTITY_ID", nullable = false)
    private PAMEntity pamEntity;

    private String address;
    @Column(name = "POSTALCODE")
    private String postalCode;
    private String country;
    private String county;
    private String municipal;
    @Column(name = "MUNICIPALCODE")
    private String municipalCode;
    private String city;
    @Embedded
    private Coords coords;


    Location() {
    }

    private Location(PAMEntity pamEntity, String address, String postalCode, String country,
                     String municipal, String city, String county, Coords coords, String municipalCode) {
        this.pamEntity = pamEntity;
        this.address = address;
        this.postalCode = postalCode;
        this.country = country;
        this.municipal = municipal;
        this.city = city;
        this.county = county;
        this.coords = coords;
        this.municipalCode = municipalCode;
    }


    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCounty() {
        return county;
    }

    public String getMunicipal() {
        return municipal;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public Optional<Coords> getCoords() {
        return Optional.ofNullable(coords);
    }

    public String getMunicipalCode() {
        return municipalCode;
    }

    public static class Builder {
        private String address;
        private String postalCode;
        private String city;
        private String country;
        private String county;
        private String municipal;
        private Coords coords;
        private String municipalCode;

        public Builder(String address, String postalCode, String city, String country) {
            this.address = address;
            this.postalCode = postalCode;
            this.city = city;
            this.country = country;
        }

        public Builder(Location location) {
            this.address = location.address;
            this.postalCode = location.postalCode;
            this.municipal = location.municipal;
            this.county = location.county;
            this.city = location.city;
            this.coords = location.coords;
            this.country = location.country;
            this.municipalCode = location.municipalCode;
            this.coords = location.coords;
        }

        public static List<Builder> copyOf(List<Location> locations) {
            return locations.stream().map(Builder::new).collect(Collectors.toList());
        }

        public Builder coords(String latitude, String longitude) {
            if (latitude != null && longitude != null) {
                this.coords = new Coords(latitude, longitude);
            }
            return this;
        }

        public Builder municipal(String municipal) {
            this.municipal = municipal;
            return this;
        }

        public Builder county(String county) {
            this.county = county;
            return this;
        }

        public Builder municipalCode(String municipalCode) {
            this.municipalCode = municipalCode;
            return this;
        }

        public Location build(PAMEntity pamEntity) {
            return new Location(pamEntity, address, postalCode, country, municipal,
                    city, county, coords, municipalCode);
        }

    }
}
