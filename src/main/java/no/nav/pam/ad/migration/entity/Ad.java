package no.nav.pam.ad.migration.entity;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Entity
@Table(name = "AD")
public class Ad extends PAMEntity {

    private String title;

    @Enumerated(EnumType.STRING)
    private AdStatus status = AdStatus.INACTIVE;

    @Enumerated(EnumType.STRING)
    private PrivacyChannel privacy = PrivacyChannel.SHOW_ALL;

    private LocalDateTime published = LocalDateTime.now();
    private LocalDateTime expires = LocalDateTime.now();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true, name = "COMPANY_ID")
    private Company employer;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "AD_CATEGORY",
            joinColumns = @JoinColumn(name = "AD_ID"),
            inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID")
    )
    private List<Category> categoryList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "pamEntity", orphanRemoval = true)
    private Administration administration;

    private String source;
    private String medium;
    private String reference;
    @Column(name = "PUBLISHEDBYADMIN")
    private LocalDateTime publishedByAdmin;

    @Column(name = "BUSINESSNAME")
    private String businessName;

    public Ad() {
    }

    public Ad(String title, String source, String medium, String reference, Company employer, AdStatus status) {
        this.source = source;
        this.medium = medium;
        this.reference = reference;
        this.title = title;
        this.employer = employer;
        this.status = status;
        this.administration = new Administration.Builder().build(this);
    }

    public Ad(Long id, String uuid, String title, String source, String medium, String reference, Company employer, AdStatus status) {
        this.id = id;
        this.uuid = (uuid != null) ? uuid : UUID.randomUUID().toString();
        this.source = source;
        this.medium = medium;
        this.reference = reference;
        this.title = title;
        this.employer = employer;
        this.status = status;
        this.administration = new Administration.Builder().build(this);
    }

    public PrivacyChannel getPrivacy() {
        return privacy;
    }

    public void setPrivacy(PrivacyChannel privacy) {
        this.privacy = privacy;
    }

    public String getTitle() {
        return title;
    }

    public AdStatus getStatus() {
        return status;
    }

    public LocalDateTime getPublished() {
        return published;
    }

    public Ad setPublished(LocalDateTime published) {
        if (published != null) {
            this.published = published;
        }
        return this;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    // TODO should probably enforce hard limit on 6 months into the future, like in annonsemottak
    public Ad setExpires(LocalDateTime expires) {
        if (expires != null) {
            this.expires = expires;
        }
        return this;
    }

    public Optional<Company> getEmployer() {
        return Optional.ofNullable(employer);
    }

    public Optional<Administration> getAdministration() {
        return Optional.ofNullable(administration);
    }

    public Ad buildAdministration(Administration.Builder administration) {
        this.administration = administration.build(this);
        return this;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    /**
     * Mark this ad as deleted, by setting its status to {@link AdStatus#DELETED}.
     *
     * @return The deleted ad instance.
     */
    public Ad markAsDeleted() {
        this.status = AdStatus.DELETED;
        return this;
    }

    public Ad markAsRejected() {
        this.status = AdStatus.REJECTED;
        return this;
    }

    public Ad markAsAutoApproved() {
        String oldComments = getAdministration().isPresent()
                ? getAdministration().get().getComments()
                : "";
        markAsDone("AUTO", "Auto approved - " + LocalDateTime.now().toString()
            + "\n" + (oldComments == null ? "" : oldComments));
        setPublishedByAdmin(LocalDateTime.now());
        setUpdatedBy("nss-admin");
        putProperty(PropertyNames._approvedby, "AUTO");

        return this;
    }

    public Ad markAsDuplicate(String reportee, String otherId) {
        this.markAsRejected();
        this.buildAdministration(new Administration.Builder(administration)
                .status(AdministrationStatus.DONE)
                .remarks(List.of(RemarkType.DUPLICATE))
                .reportee(reportee)
                .comments("Duplicate of "+otherId));
        return this;
    }

    public Ad markAsDone(String reportee, String comments) {
        this.buildAdministration(new Administration.Builder(administration)
                .status(AdministrationStatus.DONE)
                .reportee(reportee)
                .comments(comments));
        return this;
    }

    /**
     * Update {@link AdStatus} based on {@link AdministrationStatus administrative status} and published/expires timestamps.
     *
     * <p>Transitions status to {@link AdStatus#INACTIVE} if current status is {@link AdStatus#ACTIVE} and
     * published date is in the future, or if expiry date has passed.</p>
     * <p>Transitions status to {@link AdStatus#ACTIVE} if current status is {@link AdStatus#INACTIVE}, ad has been approved administratively,
     * published date is in the past and expires date is in the future.</p>
     *
     * @return updated ad instance
     */
    public Ad updateAdStatus() {

        if (status == AdStatus.ACTIVE) {
            if (expiresBeforeToday() || this.getPublished().isAfter(LocalDateTime.now())) {
                this.status = AdStatus.INACTIVE;
            }
        } else if (status == AdStatus.INACTIVE) {
            if (!expiresBeforeToday()
                    && this.getPublished().isBefore(LocalDateTime.now())
                    && this.getAdministration().isPresent()
                    && this.getAdministration().get().getStatus().equals(AdministrationStatus.DONE)) {
                this.status = AdStatus.ACTIVE;
            }
        }

        return this;
    }

    private boolean expiresBeforeToday() {
        return getExpires().isBefore(LocalDate.now().atStartOfDay());
    }

    /**
     * @return {@code true} if ad has expired normally, e.g. it has previously been approved and published (ACTIVE), but is now
     * past its expiry time and is {@link AdStatus#INACTIVE inactive}.
     */
    public boolean deactivatedByExpiry() {
        return getPublishedByAdmin() != null
                && getStatus() == AdStatus.INACTIVE
                && expiresBeforeToday()
                && getAdministration().map(a -> a.getStatus() == AdministrationStatus.DONE).orElse(true);
    }

    /**
     * @return {@code true} if ad is currently inactive, but conditions are all set so that it will become automatically
     * activated in the future. All of the following conditions must apply to the ad:
     * <ul>
     * <li>Status is {@link AdStatus#INACTIVE}</li>
     * <li>Is administratively completed and published.</li>
     * <li>Has a {@link #getPublished() publishing date} some time in the future, at which point it will
     * transition to status {@link AdStatus#ACTIVE} (become published) automatically.</li>
     * </ul>
     */
    public boolean activationOnPublishingDate() {
        return getPublishedByAdmin() != null
                && getAdministration().map(a -> a.getStatus() == AdministrationStatus.DONE).orElse(false)
                && getStatus() == AdStatus.INACTIVE
                && getPublished().isAfter(LocalDateTime.now())
                && !getExpires().isBefore(getPublished().truncatedTo(ChronoUnit.DAYS));
    }

    public void addCategoryToCategoryList(Category category) {
        this.categoryList.add(category);
    }

    public String getSource() {
        return source;
    }

    public String getMedium() {
        return medium;
    }

    public String getReference() {
        return reference;
    }

    public void setEmployer(Company employer) {
        this.employer = employer;
    }

    @Override
    public Ad putProperty(PropertyNames propertyNames, String value) {
        return (Ad) super.putProperty(propertyNames, value);
    }

    @Override
    public Ad addContact(String name, String email, String phone, String role, String title) {
        return (Ad) super.addContact(name, email, phone, role, title);
    }


    private void preserveInternalProperties(Ad merged, Ad inDb) {
        inDb.getPropertiesImmutable().keySet().stream()
                .filter(key -> key.name().startsWith("_"))
                .forEach(prop -> merged.putProperty(prop, inDb.getProperty(prop).get()));
    }

    private boolean updateToSameOrDefaultApplicationDomain(Ad update, Ad inDb) {
        return PAMEntity.DEFAULT_APPLICATION_DOMAIN.equals(inDb.getUpdatedBy())
                || inDb.getUpdatedBy().equals(update.getUpdatedBy());
    }

    private void mergeProperty(Ad merged, Ad inDb, PropertyNames propertyName) {
        if (!merged.getProperty(propertyName).isPresent()) {
            inDb.getProperty(propertyName).ifPresent(value -> merged.putProperty(propertyName, value));
        }
    }

    public LocalDateTime getPublishedByAdmin() {
        return publishedByAdmin;
    }

    public void setPublishedByAdmin(LocalDateTime publishedByAdmin) {
        this.publishedByAdmin = publishedByAdmin;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

}
