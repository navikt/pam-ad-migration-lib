package no.nav.pam.ad.migration.mapper;

import no.nav.pam.ad.migration.dto.CategoryDTO;
import no.nav.pam.ad.migration.entity.Category;
import no.nav.pam.ad.migration.entity.CategoryType;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {

    private CategoryMapper() {}

    public static CategoryDTO toDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(category.getName());
        categoryDTO.setId(category.getId());
        categoryDTO.setDescription(category.getDescription());
        categoryDTO.setCode(category.getCode());
        categoryDTO.setCategoryType(category.getCategoryType().toString());
        categoryDTO.setParentId(category.getParentId());
        return categoryDTO;
    }

    public static List<CategoryDTO> toDTOList(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static Category fromDTO(CategoryDTO dto) {
        return new Category(dto.getId(),dto.getName(), dto.getCode(), dto.getDescription(),
                CategoryType.valueOf(dto.getCategoryType()), dto.getParentId());
    }
}
