package no.nav.pam.ad.migration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyDTO extends PAMEntityDTO {

    @NotNull
    private String name;

    private String orgnr;
    private String status;
    private String parentOrgnr;
    private String publicName;
    private LocalDateTime deactivated;
    private String orgform;
    private Integer employees;


    public CompanyDTO() {
    }

    public String getName() {
        return name;
    }

    public CompanyDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getOrgnr() {
        return orgnr;
    }

    public CompanyDTO setOrgnr(String orgnr) {
        this.orgnr = orgnr;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParentOrgnr() {
        return parentOrgnr;
    }

    public void setParentOrgnr(String parentOrgnr) {
        this.parentOrgnr = parentOrgnr;
    }

    public String getPublicName() {
        return publicName;
    }

    public void setPublicName(String publicName) {
        this.publicName = publicName;
    }

    public LocalDateTime getDeactivated() {
        return deactivated;
    }

    public void setDeactivated(LocalDateTime deactivated) {
        this.deactivated = deactivated;
    }

    public String getOrgform() {
        return orgform;
    }

    public void setOrgform(String orgform) {
        this.orgform = orgform;
    }

    public Integer getEmployees() {
        return employees;
    }

    public void setEmployees(Integer employees) {
        this.employees = employees;
    }
}
