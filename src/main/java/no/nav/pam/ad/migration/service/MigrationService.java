package no.nav.pam.ad.migration.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import no.nav.pam.ad.migration.repository.AdRepository;
import no.nav.pam.ad.migration.repository.CategoryRepository;
import no.nav.pam.ad.migration.repository.CompanyRepository;
import no.nav.pam.feed.client.FeedConnector;
import no.nav.pam.feed.taskscheduler.FeedTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import java.util.stream.Collectors;

@Service
public class MigrationService {

    private static final Logger LOG = LoggerFactory.getLogger(MigrationService.class);
    private final FeedTaskService feedTaskService;
    private final FeedConnector feedConnector;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String categoriesUrl;
    private final CompanyRepository companyRepository;
    private final AdRepository adRepository;
    private final CategoryRepository categoryRepository;
    private final String adUrl;
    private final String TASK_NAME = "migrate_ads_from_feed";

    @Autowired
    public MigrationService(FeedConnector feedConnector, FeedTaskService feedTaskService,
                            RestTemplate restTemplate, ObjectMapper objectMapper, CompanyRepository companyRepository,
                            AdRepository adRepository, CategoryRepository categoryRepository,  @Value("${migrate.url}") String migrateUrl) {
        this.feedConnector = feedConnector;
        this.feedTaskService = feedTaskService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.adUrl = migrateUrl+"/ads";
        this.categoriesUrl = migrateUrl+"/categories";
        this.companyRepository = companyRepository;
        this.adRepository = adRepository;
        this.categoryRepository = categoryRepository;
    }

    public void migrateFromFeed(LocalDateTime start) throws Exception {
        Long currentTime = System.currentTimeMillis();
        Integer adsCount = 0;
        LocalDateTime startTime = start;
        mapSaveCategories();
        if (start == null) {
            startTime = feedTaskService.fetchLastRunDateForJob(TASK_NAME).orElse(LocalDateTime.now().minusYears(10));
        }
        LocalDateTime lastUpdatedDate = LocalDateTime.now();
        List<AdDTO> feedList = feedConnector.fetchContentList(adUrl, startTime, AdDTO.class);
        if (!feedList.isEmpty()) {
            feedList.sort(Comparator.comparing(AdDTO::getUpdated));
            lastUpdatedDate = feedList.get(feedList.size() - 1).getUpdated();
        }
        while (!feedList.isEmpty() && startTime.isBefore(lastUpdatedDate)) {
            mapSaveAll(feedList);
            adsCount+=feedList.size();
            feedTaskService.save(TASK_NAME, lastUpdatedDate);
            LOG.info("Saved new last run time for task {} as {} ", TASK_NAME, lastUpdatedDate);
            startTime = lastUpdatedDate;
            LOG.info("fetching from date {}", startTime);
            feedList = feedConnector.fetchContentList(adUrl, startTime, AdDTO.class);
            feedList.sort(Comparator.comparing(AdDTO::getUpdated));
            lastUpdatedDate = feedList.get(feedList.size() - 1).getUpdated();
        }
        LOG.info("Migrated {} ads ", adsCount);
        LOG.info("Time it took to finished: {}s", (System.currentTimeMillis()-currentTime)/1000);
    }

    public void mapSaveCategories() throws JsonProcessingException {
        LOG.info("fetching categories");
        String catString = restTemplate.getForObject(categoriesUrl, String.class);
        JavaType listCategory = objectMapper.getTypeFactory().constructCollectionLikeType(List.class, CategoryDTO.class);
        List<CategoryDTO> categoryDTOS = objectMapper.readValue(catString, listCategory);
        categoryDTOS.sort(Comparator.comparing(CategoryDTO::getId));
        List<Category> allCategories = categoryDTOS.stream().map(CategoryMapper::fromDTO).collect(Collectors.toList());
        categoryRepository.saveAll(allCategories);
        LOG.info("All categories updated");
    }

    public void mapSaveAll( List<AdDTO> adDTOs) throws Exception {
        LOG.info("Saving {} items", adDTOs.size());
        List<Ad> ads = adDTOs.stream().map(ad -> {
            Company company = null;
            List<Category> categories = ad.getCategoryList().stream().map(CategoryMapper::fromDTO).collect(Collectors.toList());
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
    }

}
