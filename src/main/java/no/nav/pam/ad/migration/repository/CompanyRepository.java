package no.nav.pam.ad.migration.repository;


import no.nav.pam.ad.migration.entity.Company;
import no.nav.pam.ad.migration.entity.CompanyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Company findByUuid(String uuid);

}
