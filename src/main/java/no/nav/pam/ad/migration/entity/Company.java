package no.nav.pam.ad.migration.entity;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "COMPANY")
public class Company extends PAMEntity {

    private static final long COMPANY_UPDATE_MONTH = 1;

    private String name;
    private String orgnr;
    @Enumerated(EnumType.STRING)
    private CompanyStatus status = CompanyStatus.ACTIVE;
    @Column(name = "parentorgnr")
    private String parentOrgnr;
    @Column(name = "publicname")
    private String publicName;
    private LocalDateTime deactivated;
    private String orgform;
    private Integer employees;

    public Company() {
    }

    public Company(String name, String orgnr,
                   String parentOrgnr, String publicName,
                   String orgform, Integer employees) {
        this.name = name;
        this.orgnr = orgnr;
        this.parentOrgnr = parentOrgnr;
        this.publicName = publicName;
        this.orgform = orgform;
        this.employees = employees;
    }

    public Company(Long id, String uuid, String name, String orgnr,
                   String parentOrgnr, String publicName,
                   String orgform, Integer employees) {
        this.id = id;
        this.uuid = (uuid != null) ? uuid : UUID.randomUUID().toString();
        this.name = name;
        this.orgnr = orgnr;
        this.parentOrgnr = parentOrgnr;
        this.publicName = publicName;
        this.orgform = orgform;
        this.employees = employees;
    }

    public String getName() {
        return name;
    }

    public String getOrgnr() {
        return orgnr;
    }

    public CompanyStatus getStatus() {
        return status;
    }

    public String getParentOrgnr() {
        return parentOrgnr;
    }

    public String getPublicName() {
        return publicName;
    }

    public LocalDateTime getDeactivated() {
        return deactivated;
    }

    /**
     * Will mark this company with status {@link CompanyStatus#INACTIVE} and set the deactivated timestamp to current time.
     *
     * @return The deactivated company instance.
     */
    public Company deactivate() {

        this.deactivated = LocalDateTime.now();
        this.status = CompanyStatus.INACTIVE;
        return this;

    }


    public String getOrgform() {
        return orgform;
    }

    public Integer getEmployees() {
        return employees;
    }

    @Override
    public Company addMedia(String filename, String mediaLink) {
        return (Company) super.addMedia(filename, mediaLink);
    }

    @Override
    public Company addContact(String name, String email, String phone, String role, String title) {
        return (Company) super.addContact(name, email, phone, role, title);
    }

    @Override
    public Company putProperty(PropertyNames propertyNames, String value) {
        return (Company) super.putProperty(propertyNames, value);
    }

    public boolean isRecentlyUpdated() {
        LocalDateTime d = LocalDateTime.now().minusMonths(COMPANY_UPDATE_MONTH);
        return getUpdated().isAfter(d);
    }

    public void initLazyFields() {
        super.initLazyFields();
    }
}
