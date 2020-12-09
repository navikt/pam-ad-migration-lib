package no.nav.pam.ad.migration.repository;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.pam.ad.migration.dto.AdDTO;
import no.nav.pam.ad.migration.dto.CategoryDTO;
import no.nav.pam.ad.migration.entity.Ad;
import no.nav.pam.ad.migration.entity.Category;
import no.nav.pam.ad.migration.entity.Company;
import no.nav.pam.ad.migration.mapper.AdMapper;
import no.nav.pam.ad.migration.mapper.CategoryMapper;
import no.nav.pam.ad.migration.mapper.CompanyMapper;
import no.nav.pam.feed.client.FeedTransport;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.flyway.enabled=true"})
public class MigrationRepositoryTest {

    private static final Logger LOG = LoggerFactory.getLogger(MigrationRepositoryTest.class);

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void insertUpdateAdMigration()  throws Exception {

        // fetch categories
        JavaType listCategory = objectMapper.getTypeFactory().constructCollectionLikeType(List.class, CategoryDTO.class);
        List<CategoryDTO> categoriesDTO = objectMapper.readValue(MigrationRepositoryTest.class.getResourceAsStream("/categories.json"), listCategory);
        categoriesDTO.sort(Comparator.comparing(CategoryDTO::getId));
        List<Category> allCategories = categoriesDTO.stream().map(CategoryMapper::fromDTO).collect(Collectors.toList());
        categoryRepository.saveAll(allCategories);
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(FeedTransport.class, new Class[]{AdDTO.class});
        FeedTransport<AdDTO> transport = objectMapper.readValue(MigrationRepositoryTest.class.getResourceAsStream("/ad-feed.json"), javaType);
        List<AdDTO> adDTOs = transport.content;

        List<Ad> ads = adDTOs.stream().map(ad -> {
            Company company = null;
            List<Category> categories = ad.getCategoryList().stream().map(CategoryMapper::fromDTO).collect(Collectors.toList());
            LOG.info("Ad id {}", ad.getId());
            if (ad.getEmployer() != null ) {
                if (companyRepository.existsById(ad.getEmployer().getId())) {
                    company = new Company();
                    company.setId(ad.getEmployer().getId());
                } else {
                    company = CompanyMapper.fromDTO(ad.getEmployer());
                    companyRepository.save(company);
                }
            }
            return AdMapper.fromDTO(ad, company, categories);
        }).collect(Collectors.toList());
        adRepository.saveAll(ads);
        int companySize = companyRepository.findAll().size();
        int adSize = adRepository.findAll().size();
        ads = adDTOs.stream().map(ad -> {
            Company company = null;
            List<Category> categories = ad.getCategoryList().stream().map(CategoryMapper::fromDTO).collect(Collectors.toList());
            LOG.info("Ad id {}", ad.getId());
            if (ad.getEmployer() != null ) {
                if (companyRepository.existsById(ad.getEmployer().getId())) {
                    company = new Company();
                    company.setId(ad.getEmployer().getId());
                } else {
                    company = CompanyMapper.fromDTO(ad.getEmployer());
                    companyRepository.save(company);
                }
            }
            return AdMapper.fromDTO(ad, company, categories);
        }).collect(Collectors.toList());
        adRepository.saveAll(ads);
        List<Ad> all = adRepository.findAll();
        Assert.assertEquals(companySize, companyRepository.findAll().size());
        Assert.assertEquals(adSize, adRepository.findAll().size());

    }


}
