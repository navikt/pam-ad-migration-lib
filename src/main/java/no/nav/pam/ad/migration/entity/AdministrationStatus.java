package no.nav.pam.ad.migration.entity;

public enum AdministrationStatus {

    RECEIVED(0),
    PENDING(1),

    @Deprecated
    APPROVED(2),

    @Deprecated
    REJECTED(3),

    @Deprecated
    STOPPED(4),

    DONE(5);

    private static final AdministrationStatus[] COPY_OF_VALUES = values();
    private final int value;

    AdministrationStatus(int value) {
        this.value = value;
    }

    /**
     * Used to get enum name from String value, typically from JSON
     */
    public static AdministrationStatus findName(String name) {
        for (AdministrationStatus a : COPY_OF_VALUES) {
            if (a.name().equals(name)) {
                return a;
            }
        }
        return null;
    }

}
