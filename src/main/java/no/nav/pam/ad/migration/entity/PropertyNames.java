package no.nav.pam.ad.migration.entity;


public enum PropertyNames {

    adtext(PropertyType.HTML),
    sourceurl(PropertyType.URL),
    applicationdue(PropertyType.TEXT),
    applicationemail(PropertyType.EMAIL),
    applicationmail(PropertyType.TEXT),
    applicationlabel(PropertyType.TEXT),
    applicationurl(PropertyType.URL),
    @Deprecated
    employer(PropertyType.TEXT),
    employerdescription(PropertyType.HTML),
    employerhomepage(PropertyType.URL),
    engagementtype(PropertyType.TEXT),
    extent(PropertyType.TEXT),
    occupation(PropertyType.TEXT),
    positioncount(PropertyType.INTEGER),
    salary(PropertyType.INTEGER),
    starttime(PropertyType.TEXT),
    role(PropertyType.TEXT),
    sector(PropertyType.TEXT),
    location(PropertyType.TEXT),
    externalref(PropertyType.TEXT),
    jobtitle(PropertyType.TEXT),
    keywords(PropertyType.TEXT),
    sourcecreated(PropertyType.TEXT),
    sourceupdated(PropertyType.TEXT),
    logomain(PropertyType.URL),
    logolisting(PropertyType.URL),
    author(PropertyType.TEXT),
    address(PropertyType.TEXT),
    industry(PropertyType.TEXT),
    nace2(PropertyType.JSON),  //SN2007 or NACE Rev 2
    searchtags(PropertyType.JSON), // Tags related to category of ads
    classification_styrk08_score(PropertyType.DOUBLE),
    classification_input_source(PropertyType.TEXT),
    categories(PropertyType.JSON),
    euresflagg(PropertyType.TEXT),

    // Properties specifically for/from stillingsregistrering and internal ad registrations
    tags(PropertyType.JSON),             // Shape: Array of strings, generic tags
    ontologyJobtitle(PropertyType.JSON), // Shape: Object with string fields "konspetId", "label", "styrk08"
    workhours(PropertyType.TEXT),
    workday(PropertyType.TEXT),
    facebookpage(PropertyType.URL),
    contactperson(PropertyType.TEXT),
    contactpersontitle(PropertyType.TEXT),
    contactpersonemail(PropertyType.TEXT),
    contactpersonphone(PropertyType.TEXT),
    linkedinpage(PropertyType.URL),
    jobpercentage(PropertyType.TEXT),
    jobarrangement(PropertyType.TEXT),
    twitteraddress(PropertyType.URL),

    // Properties specifically from stillingsolr
    education(PropertyType.TEXT),
    certificate(PropertyType.TEXT),
    expertise(PropertyType.TEXT),
    practice(PropertyType.TEXT),

    @Deprecated
    hardrequirements(PropertyType.JSON),
    @Deprecated
    softrequirements(PropertyType.JSON),
    @Deprecated
    personalattributes(PropertyType.JSON),

    // internal apps properties
    _approvedby(PropertyType.TEXT),
    _noorgnr(PropertyType.TEXT),
    // used by importapi
    _providerid(PropertyType.TEXT),
    _versionid(PropertyType.INTEGER);

    private PropertyType type;

    PropertyNames(PropertyType type) {
        this.type = type;
    }

    public PropertyType getType() {
        return type;
    }

}
