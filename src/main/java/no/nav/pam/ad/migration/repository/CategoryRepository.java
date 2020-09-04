package no.nav.pam.ad.migration.repository;


import no.nav.pam.ad.migration.entity.Category;
import no.nav.pam.ad.migration.entity.CategoryType;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

    Category findByCodeAndCategoryType(String code, CategoryType categoryType);

}
