package no.nav.pam.ad.migration.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;
import java.util.stream.Stream;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactDTO  {

    private String name;
    private String email;
    private String phone;
    private String role;
    private String title;

    public ContactDTO() {
    }

    public String getName() {
        return name;
    }

    public ContactDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ContactDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public ContactDTO setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getRole() {
        return role;
    }

    public ContactDTO setRole(String role) {
        this.role = role;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ContactDTO setTitle(String title) {
        this.title = title;
        return this;
    }


    /**
     * Check if this instance has only {@code null} fields.
     *
     * @return {@code true} if so.
     */
    @JsonIgnore
    public boolean isEmpty() {
        return Stream.of(name, email, phone, role, title).allMatch(Objects::isNull);
    }

}
