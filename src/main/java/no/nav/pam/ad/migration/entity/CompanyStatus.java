package no.nav.pam.ad.migration.entity;

public enum CompanyStatus {

    INACTIVE(0),
    ACTIVE(1);

    private int value;

    CompanyStatus(int value) {
        this.value = value;
    }

}
