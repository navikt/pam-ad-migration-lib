package no.nav.pam.ad.migration.entity;

public enum PrivacyChannel {

    INTERNAL_NOT_SHOWN, //"Kun behandles ved NAV lokal"
    SHOW_ALL, //"Presenteres eksternt - NAVs kanaler"
    DONT_SHOW_EMPLOYER, //"Presenteres eksternt uten arbeidsgiver opplysninger"
    DONT_SHOW_AUTHOR; //"Presenteres eksternt uten annonsørens opplysninger"

}
