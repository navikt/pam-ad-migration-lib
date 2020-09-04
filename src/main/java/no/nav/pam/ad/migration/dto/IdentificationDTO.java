package no.nav.pam.ad.migration.dto;

public abstract class IdentificationDTO {

    private Long id;

    public Long getId() {
        return id;
    }

    public IdentificationDTO setId(Long id) {
        this.id = id;
        return this;
    }

}
