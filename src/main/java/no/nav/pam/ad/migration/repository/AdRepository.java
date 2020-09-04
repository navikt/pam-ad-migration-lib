package no.nav.pam.ad.migration.repository;


import no.nav.pam.ad.migration.entity.Ad;
import no.nav.pam.ad.migration.entity.AdStatus;
import no.nav.pam.ad.migration.entity.AdministrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long>, JpaSpecificationExecutor<Ad> {

    Ad findByUuid(String uuid);

}
