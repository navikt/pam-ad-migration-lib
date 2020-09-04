create sequence PAMENTITY_SEQ start with 1000000;

create sequence LOCATION_SEQ start with 1000;

create sequence MEDIA_SEQ start with 1000;

create sequence CONTACT_SEQ start with 1000;

create sequence ADMINISTRATION_SEQ start with 1000;

create sequence CATEGORY_SEQ start with 9000;

create sequence FEEDTASK_SEQ start with 1000;

create table PAMENTITY
(
    ID NUMERIC(19) not null
        constraint PK_PAMENTITY
            primary key,
    UUID VARCHAR(36) not null
        constraint UQ_UUID
            unique,
    CREATED TIMESTAMP(6) not null,
    CREATEDBY VARCHAR(255) not null,
    UPDATED TIMESTAMP(6) not null,
    UPDATEDBY VARCHAR(255) not null
);

create index IX_PAMENTITY_UPDATED
    on PAMENTITY (UPDATED);

create table COMPANY(
    ID NUMERIC(19) not null constraint PK_COMPANY primary key,
    NAME VARCHAR(255) not null,
    ORGNR VARCHAR(255)
        constraint UQ_ORGNR
            unique,
    STATUS VARCHAR(36) not null,
    PARENTORGNR VARCHAR(255),
    PUBLICNAME VARCHAR(255),
    DEACTIVATED TIMESTAMP(6),
    ORGFORM VARCHAR(36),
    EMPLOYEES INTEGER,
    constraint FK_COMPANY_PAMENTITY FOREIGN KEY (ID) references PAMENTITY(ID)
);

create table AD
(
    ID NUMERIC(19) not null constraint PK_AD primary key,
    TITLE VARCHAR(512) not null,
    STATUS VARCHAR(36) not null,
    PRIVACY VARCHAR(128) not null,
    SOURCE VARCHAR(255) not null,
    MEDIUM VARCHAR(255) not null,
    REFERENCE VARCHAR(255) not null,
    PUBLISHED TIMESTAMP(6),
    EXPIRES TIMESTAMP(6),
    COMPANY_ID NUMERIC(19) constraint FK_AD_COMPANY references COMPANY(ID),
    PUBLISHEDBYADMIN TIMESTAMP(6),
    BUSINESSNAME VARCHAR(255),
    constraint UQ_SOURCE_MEDIUM_REFERENCE unique (SOURCE, MEDIUM, REFERENCE),
    constraint FK_AD_PAMENTITY FOREIGN KEY(ID) references PAMENTITY(ID)
);

create index IX_AD_PUBLISHED
    on AD (PUBLISHED);

create index IX_AD_EXPIRES
    on AD (EXPIRES);

create index IX_AD_SOURCE
    on AD (SOURCE);

create index IX_AD_STATUS
    on AD (STATUS);

create index IX_AD_BUSINESSNAME
    on AD (BUSINESSNAME);

create table LOCATION
(
    ID NUMERIC(19) not null constraint PK_LOCATION primary key,
    PAMENTITY_ID NUMERIC(19) not null constraint FK_LOCATION_PAMENTITY references PAMENTITY(ID),
    ADDRESS VARCHAR(255),
    POSTALCODE VARCHAR(10),
    COUNTY VARCHAR(255),
    MUNICIPAL VARCHAR(255),
    CITY VARCHAR(255),
    COUNTRY VARCHAR(255),
    LATITUDE VARCHAR(30),
    LONGITUDE VARCHAR(30),
    MUNICIPALCODE VARCHAR(10)
);

create index IX_LOCATION_MUNICIPAL
    on LOCATION (MUNICIPAL);

create table MEDIA
(
    ID NUMERIC(19) not null constraint PK_MEDIA primary key,
    PAMENTITY_ID NUMERIC(19) constraint FK_MEDIA_PAMENTITY references PAMENTITY (ID),
    FILENAME VARCHAR(255) not null,
    MEDIALINK VARCHAR(512) not null
);

create table CONTACT
(
    ID NUMERIC(19) not null constraint PK_CONTACT primary key,
    PAMENTITY_ID NUMERIC(19) constraint FK_CONTACT_PAMENTITY references PAMENTITY(ID),
    NAME VARCHAR(512),
    PHONE VARCHAR(36),
    EMAIL VARCHAR(255),
    ROLE VARCHAR(255),
    TITLE VARCHAR(512)
);

create table ADMINISTRATION
(
    ID NUMERIC(19) not null constraint PK_ADMINISTRATION primary key,
    OFFICERNAME VARCHAR(255),
    STATUS VARCHAR(36) not null,
    COMMENTS VARCHAR(512),
    CANDIDATES INTEGER,
    EMPLOYERCONTACT VARCHAR(255),
    CREATED TIMESTAMP(6) default CURRENT_TIMESTAMP not null,
    PAMENTITY_ID NUMERIC(19) constraint FK_ADMINISTRATION_PAMENTITY references PAMENTITY(ID),
    REPORTEE VARCHAR(255),
    REMARKS VARCHAR(512),
    NAVIDENT VARCHAR(25)
);

create table AD_ADMINISTRATION
(
    AD_ID NUMERIC(19) not null constraint FK_AD_ADMINISTRATION_AD references AD(ID),
    ADMINISTRATION_ID NUMERIC(19) not null constraint FK_AD_ADMINISTRATION_ADMIN references ADMINISTRATION(ID)
);

create table PROPERTY
(
    PAMENTITY_ID NUMERIC(19) not null constraint FK_PROPERTY_PAMENTITY references PAMENTITY(ID),
    NAME VARCHAR(255) not null,
    VALUE TEXT,
    constraint UQ_PROPERTY unique (PAMENTITY_ID, NAME)
);

create table CATEGORY
(
    ID NUMERIC(19) not null constraint PK_CATEGORY primary key,
    NAME VARCHAR(255) not null,
    PARENT_ID NUMERIC(19) constraint FK_CATEGORY_CATEGORY references CATEGORY(ID),
    CODE VARCHAR(255),
    DESCRIPTION VARCHAR(2000),
    CATEGORY_TYPE SMALLINT
);

create table AD_CATEGORY
(
    AD_ID NUMERIC(19) not null constraint FK_AD_CATEGORY_AD references AD(ID),
    CATEGORY_ID NUMERIC(19) not null constraint FK_AD_CATEGORY_CATEGORY references CATEGORY(ID)
);

create table FEEDTASK
(
    ID NUMERIC(19) not null constraint PK_FEEDTASK primary key,
    FEED_NAME VARCHAR(255) not null unique,
    LAST_RUN_DATE TIMESTAMP(6)
);

create table SHEDLOCK
(
    NAME VARCHAR(64) not null primary key,
    LOCK_UNTIL TIMESTAMP(3),
    LOCKED_AT TIMESTAMP(3),
    LOCKED_BY VARCHAR(255)
);



