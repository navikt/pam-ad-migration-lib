package no.nav.pam.ad.migration.mapper;

import no.nav.pam.ad.migration.dto.LocationDTO;
import no.nav.pam.ad.migration.entity.Location;

class LocationMapper {

    private LocationMapper() {
    }

    static LocationDTO toDTO(Location location) {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setAddress(location.getAddress());
        locationDTO.setPostalCode(location.getPostalCode());
        locationDTO.setCounty(location.getCounty());
        locationDTO.setMunicipal(location.getMunicipal());
        locationDTO.setCity(location.getCity());
        locationDTO.setCountry(location.getCountry());
        location.getCoords().ifPresent(c -> {
            locationDTO.setLatitude(c.getLatitude());
            locationDTO.setLongitude(c.getLongitude());
        });
        locationDTO.setMunicipalCode(location.getMunicipalCode());
        return locationDTO;
    }

    static Location.Builder fromDTO(LocationDTO dto) {

        Location.Builder builder = new Location.Builder(dto.getAddress(), dto.getPostalCode(),
                dto.getCity(), dto.getCountry());
        builder.coords(dto.getLatitude(), dto.getLongitude());
        builder.municipal(dto.getMunicipal());
        builder.county(dto.getCounty());
        builder.municipalCode(dto.getMunicipalCode());
        return builder;
    }

}
