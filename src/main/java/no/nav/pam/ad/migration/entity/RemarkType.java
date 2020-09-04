package no.nav.pam.ad.migration.entity;

public enum RemarkType {
    // values are backcompatible with old annonsemottaket
    NOT_APPROVED_BY_LABOUR_INSPECTION(1),
    NO_EMPLOYMENT(2),
    DUPLICATE(3),
    DISCRIMINATING(4),
    REJECT_BECAUSE_CAPACITY(5),
    FOREIGN_JOB(6),
    COLLECTION_JOB(7),
    UNKNOWN(100);

    private final int value;

    RemarkType(int value) {
        this.value = value;
    }

    public static RemarkType findName(String name) {
        for (RemarkType r : values()) {
            if (r.name().equals(name)) {
                return r;
            }
        }
        return UNKNOWN;
    }

    public static RemarkType findValue(int value) {
        for (RemarkType r : values()) {
            if (r.value == value) {
                return r;
            }
        }
        return UNKNOWN;
    }
}
