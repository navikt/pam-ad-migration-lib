package no.nav.pam.ad.migration.entity;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "CATEGORY")
@SequenceGenerator(name = "CATEGORY_SEQ", sequenceName = "CATEGORY_SEQ", allocationSize = 1)
public class Category {

    @Id
    @GeneratedValue(generator = "CATEGORY_SEQ")
    private Long id;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "CATEGORY_TYPE")
    private CategoryType categoryType;

    @NotEmpty
    private String code;

    @NotEmpty
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(name = "PARENT_ID")
    private Long parentId;

    Category() {
    }

    public Category(Long id, String name, String code, String description, CategoryType categoryType, Long parentId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.categoryType = categoryType;
        this.parentId = parentId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public CategoryType getCategoryType() {
        return categoryType;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRoot() {
        return parentId == null;
    }

    public Long getParentId() {
        return parentId;
    }

    public Category merge(Category inDb) {
        this.id = inDb.getId();
        return this;
    }

}
