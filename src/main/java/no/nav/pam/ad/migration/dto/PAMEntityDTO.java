package no.nav.pam.ad.migration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class PAMEntityDTO extends IdentificationDTO {

    private String uuid;
    private LocalDateTime created;
    private String createdBy;
    private LocalDateTime updated;
    private String updatedBy;

    @Valid
    private List<MediaDTO> mediaList = new ArrayList<>();

    private List<ContactDTO> contactList = new ArrayList<>();
    @Deprecated
    private LocationDTO location;
    private List<LocationDTO> locationList = new ArrayList<>();
    private Map<String, String> properties = new HashMap<>();

    PAMEntityDTO() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public List<MediaDTO> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<MediaDTO> mediaList) {
        this.mediaList = mediaList;
    }

    public List<ContactDTO> getContactList() {
        return contactList;
    }

    public void setContactList(List<ContactDTO> contactList) {
        this.contactList = contactList;
    }

    @Deprecated
    public LocationDTO getLocation() {
        return location;
    }

    @Deprecated
    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public List<LocationDTO> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<LocationDTO> locationList) {
        this.locationList = locationList;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public void putProperties(String name, String value) {
        this.properties.put(name, value);
    }
}
