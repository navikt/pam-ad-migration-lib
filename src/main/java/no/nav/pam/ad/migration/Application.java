package no.nav.pam.ad.migration;

import no.nav.pam.ad.migration.service.MigrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static Logger LOG = LoggerFactory.getLogger(Application.class);

    @Autowired
    private MigrationService migrationService;

    public static void main(String[] args) {
        LOG.info("Starting pam-ad-migration");
        SpringApplication.run(Application.class, args).close();
        LOG.info("Finished");
    }

    @Override
    public void run(String... args) throws Exception {
        LocalDateTime dateTime = null;
        if (args.length>0 && "migrate".equals(args[0])) {
            if (args.length>1 && "date".equals(args[1])) {
                dateTime = LocalDateTime.parse(args[2], DateTimeFormatter.ISO_DATE);
            }
            LOG.info("starting migration from date {} ",dateTime);
            migrationService.migrateFromFeed(dateTime);
        }

    }
}
