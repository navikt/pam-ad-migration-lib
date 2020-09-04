package no.nav.pam.ad.migration.dto;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdDTO extends PAMEntityDTO {

    private String title;
    private String status;
    private String privacy;
    private String source;
    private String medium;
    private String reference;
    private LocalDateTime published;
    private LocalDateTime expires;
    private CompanyDTO employer;
    private List<CategoryDTO> categoryList = new ArrayList<>();
    private AdministrationDTO administration;
    private LocalDateTime publishedByAdmin;
    private String businessName;
    // Meta fields not part of core ad data model
    private boolean firstPublished = false; // Set to true by API user when an ad is published the first time
    private boolean deactivatedByExpiry = false;
    private boolean activationOnPublishingDate = false;


    public AdDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getPublished() {
        return published;
    }

    public void setPublished(LocalDateTime published) {
        this.published = published;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    public void setExpires(LocalDateTime expires) {
        this.expires = expires;
    }

    public CompanyDTO getEmployer() {
        return employer;
    }

    public void setEmployer(CompanyDTO companyDto) {
        this.employer = companyDto;
    }

    public AdministrationDTO getAdministration() {
        return administration;
    }

    public void setAdministration(AdministrationDTO administration) {
        this.administration = administration;
    }

    public List<CategoryDTO> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryDTO> categoryList) {
        this.categoryList = categoryList;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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

    public boolean isFirstPublished() {
        return firstPublished;
    }

    public void setFirstPublished(boolean firstPublished) {
        this.firstPublished = firstPublished;
    }

    public boolean isDeactivatedByExpiry() {
        return deactivatedByExpiry;
    }

    public void setDeactivatedByExpiry(boolean deactivatedByExpiry) {
        this.deactivatedByExpiry = deactivatedByExpiry;
    }

    public boolean isActivationOnPublishingDate() {
        return activationOnPublishingDate;
    }

    public void setActivationOnPublishingDate(boolean activationOnPublishingDate) {
        this.activationOnPublishingDate = activationOnPublishingDate;
    }

}
