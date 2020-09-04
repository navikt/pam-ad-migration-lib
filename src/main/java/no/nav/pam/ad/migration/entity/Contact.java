package no.nav.pam.ad.migration.entity;


import javax.persistence.*;

@Entity
@Table(name = "CONTACT")
@SequenceGenerator(name = "CONTACT_SEQ", sequenceName = "CONTACT_SEQ", allocationSize = 1)
public class Contact {


    @Id
    @GeneratedValue(generator = "CONTACT_SEQ")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "PAMENTITY_ID")
    private PAMEntity pamEntity;

    private String name;
    private String email;
    @Column(length = 36)
    private String phone;
    private String role;
    private String title;

    Contact() {
    }

    public Contact(PAMEntity pamEntity, String name, String email, String phone, String role, String title) {
        this.pamEntity = pamEntity;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }
}
