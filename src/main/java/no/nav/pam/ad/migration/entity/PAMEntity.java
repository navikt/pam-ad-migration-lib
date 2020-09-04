package no.nav.pam.ad.migration.entity;


import org.hibernate.Hibernate;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Type;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "PAMENTITY")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PAMEntity {

    /**
     * The default "application user" used for fields {@link #getCreatedBy()} and {@link #getUpdatedBy()} for
     * entities created and modified by external sources with no/unclaimed administrative application domain.
     */
    public static final String DEFAULT_APPLICATION_DOMAIN = "pam-ad";

    @Id
    protected Long id;
    protected String uuid = UUID.randomUUID().toString();
    protected LocalDateTime created;
    @Column(name = "createdby")
    private String createdBy = DEFAULT_APPLICATION_DOMAIN;

    @Type(type = "no.nav.pam.ad.migration.entity.MicrosecondLocalDateTimeVersionType")
    protected LocalDateTime updated;

    @Column(name = "updatedby")
    private String updatedBy = DEFAULT_APPLICATION_DOMAIN;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pamEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Media> mediaList = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pamEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contact> contactList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pamEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy // Default order field is primary key of associated entity, stabilize order of locationList items
    private List<Location> locationList = new ArrayList<>();
    @ElementCollection(fetch = FetchType.LAZY)
    @MapKeyColumn(name = "NAME")
    @Column(name = "VALUE", nullable = false)
    @BatchSize(size = 200)
    @MapKeyEnumerated(EnumType.STRING)
    @CollectionTable(
            name = "PROPERTY",
            joinColumns = @JoinColumn(name = "PAMENTITY_ID"),
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"PAMENTITY_ID", "NAME"})
            })
    protected Map<PropertyNames, String> properties = new EnumMap<>(PropertyNames.class);

    public PAMEntity() {
    }

    @PrePersist
    @PreUpdate
    protected void onMerge() {
        if (isNew()) {
            // Ensure precision is exactly representable in database for optimistic locking field, truncate to micro seconds
            this.created = this.updated = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        }
    }

    public boolean isNew() {
        return id == null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
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

    /**
     * The time of last change to an ad. This field also acts as a versioning field, and setting this value on an entity
     * will cause optimistic lock checking to be performed, if the entity is saved as an update.
     *
     * <p>If this field is not set ({@code null}) when entity instance is used for update/merge,
     * no optimistic lock checks will be performed.</p>
     *
     * <p>It will be set automatically to creation time for new ads.</p>
     * @param updated
     */
    public void setUpdated(LocalDateTime updated) {
        // Ensure precision is exactly representable in database for optimistic locking field, truncate to micro seconds
        this.updated = updated != null ? updated.truncatedTo(ChronoUnit.MICROS) : null;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public List<Media> getMediaListImmutable() {
        return Collections.unmodifiableList(mediaList);
    }

    public List<Contact> getContactListImmutable() {
        return Collections.unmodifiableList(contactList);
    }

    public Optional<Location> getLocation() {
        return Optional.ofNullable((locationList.size() > 0) ? locationList.get(0) : null);
    }

    public void buildLocation(Location.Builder builder) {
        Location location = builder.build(this);
        if (locationList.size() > 0) {
            locationList.set(0, location);
        } else {
            locationList.add(builder.build(this));
        }
    }

    public void buildLocations(List<Location.Builder> locations) {
        if (this.locationList!=null) {
            this.locationList.clear();
        }
        this.locationList.addAll(locations.stream().map(b -> b.build(this)).collect(Collectors.toList()));
    }

    public List<Location> getLocationListImmutable() {
        return Collections.unmodifiableList(locationList);
    }

    public PAMEntity addMedia(String filename, String mediaLink) {
        mediaList.add(new Media(this, filename, mediaLink));
        return this;
    }

    public PAMEntity addContact(String name, String email, String phone, String role, String title) {
        Contact contact = new Contact(this, name, email, phone, role, title);
        this.contactList.add(contact);
        return this;
    }

    public PAMEntity putProperty(PropertyNames propertyNames, String value) {
        if (!StringUtils.isEmpty(value)) {
            properties.put(propertyNames, value);
        }
        return this;
    }

    public Map<PropertyNames, String> getPropertiesImmutable() {
        return Collections.unmodifiableMap(properties);
    }

    public Optional<String> getProperty(PropertyNames property) {
        return Optional.ofNullable(properties.get(property));
    }

    /**
     * Delete a property.
     * @param property
     * @return value of deleted property, if found
     */
    public Optional<String> deleteProperty(PropertyNames property) {
        return Optional.ofNullable(properties.remove(property));
    }

    public PAMEntity merge(PAMEntity inDb) {
        this.id = inDb.getId();
        this.uuid = inDb.getUuid();
        this.createdBy = inDb.getCreatedBy();
        this.created = inDb.getCreated();

        // Only do optimistic locking check if provided ad has the updated field set. Otherwise set it to db value
        // to pass checks and let JPA update the value to current time.
        if (this.updated == null) {
            this.updated = inDb.updated;
        }

        return this;
    }

    protected void initLazyFields() {
        Hibernate.initialize(mediaList);
        Hibernate.initialize(properties);
        Hibernate.initialize(contactList);
        Hibernate.initialize(locationList);
    }
}
