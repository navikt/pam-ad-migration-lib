package no.nav.pam.ad.migration.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryDTO extends IdentificationDTO implements Serializable {

    @NotEmpty
    private String code;

    @NotEmpty
    @Pattern(regexp = "STYRK08NAV")
    private String categoryType;

    @NotEmpty
    private String name;

    private String description;

    private Long parentId;

    public CategoryDTO() {
    }

    public CategoryDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public CategoryDTO setName(String name) {
        this.name = name;
        return this;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getCode() {
        return code;
    }

    public CategoryDTO setCode(String code) {
        this.code = code;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public CategoryDTO setCategoryType(String categoryType) {
        this.categoryType = categoryType;
        return this;
    }
}
