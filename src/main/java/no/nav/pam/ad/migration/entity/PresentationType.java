package no.nav.pam.ad.migration.entity;

public enum PresentationType {
    MAIN(1),
    LIST(2);

    private int value;

    PresentationType(int value) {
        this.value = value;
    }

}
