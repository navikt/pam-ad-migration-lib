package no.nav.pam.ad.migration.mapper;


import no.nav.pam.ad.migration.dto.AdministrationDTO;
import no.nav.pam.ad.migration.entity.Administration;
import no.nav.pam.ad.migration.entity.AdministrationStatus;
import no.nav.pam.ad.migration.entity.RemarkType;

import java.util.stream.Collectors;

class AdministrationMapper {

    private AdministrationMapper() {}

    static AdministrationDTO toDTO(Administration source) {
        if (source == null) {
            return null;
        }
        AdministrationDTO administrationDTO = new AdministrationDTO();
        administrationDTO.setStatus(source.getStatus().name());
        administrationDTO.setComments(source.getComments());
        administrationDTO.setReportee(source.getReportee());
        administrationDTO.setRemarks(source.getRemarks().stream().map(Enum::name).collect(Collectors.toList()));
        administrationDTO.setNavIdent(source.getNavIdent());
        return administrationDTO;
    }

    static Administration.Builder fromDTO(AdministrationDTO source) {
        Administration.Builder builder = new Administration.Builder();
        builder.comments(source.getComments());
        builder.remarks(source.getRemarks().stream().map(RemarkType::findName).collect(Collectors.toList()));
        builder.status(AdministrationStatus.findName(source.getStatus()));
        builder.reportee(source.getReportee());
        builder.navIdent(source.getNavIdent());
        return builder;
    }

}
