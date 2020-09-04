package no.nav.pam.ad.migration.mapper;


import no.nav.pam.ad.migration.dto.CompanyDTO;
import no.nav.pam.ad.migration.entity.Company;
import no.nav.pam.ad.migration.entity.PropertyNames;

import java.util.List;
import java.util.stream.Collectors;

public class CompanyMapper {

    private CompanyMapper() {}

    public static CompanyDTO toDTO(Company company) {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(company.getId());
        companyDTO.setUuid(company.getUuid());
        companyDTO.setUpdatedBy(company.getUpdatedBy());
        companyDTO.setUpdated(company.getUpdated());
        companyDTO.setCreatedBy(company.getCreatedBy());
        companyDTO.setCreated(company.getCreated());
        companyDTO.setMediaList(MediaMapper.toDTOList(company.getMediaListImmutable()));
        companyDTO.setContactList(ContactMapper.from(company));
        company.getLocation().ifPresent(loc -> companyDTO.setLocation(LocationMapper.toDTO(loc)));
        companyDTO.setLocationList(company.getLocationListImmutable().stream().map(LocationMapper::toDTO).collect(Collectors.toList()));
        companyDTO.setName(company.getName());
        companyDTO.setStatus(company.getStatus().name());
        companyDTO.setParentOrgnr(company.getParentOrgnr());
        companyDTO.setPublicName(company.getPublicName());
        companyDTO.setDeactivated(company.getDeactivated());
        companyDTO.setEmployees(company.getEmployees());
        companyDTO.setOrgform(company.getOrgform());
        company.getPropertiesImmutable().forEach(
                (name, value) -> companyDTO.putProperties(name.name(), value));
        return companyDTO
                .setOrgnr(company.getOrgnr());
    }

    public static Company fromDTO(CompanyDTO companyDTO) {
        Company company = new Company(companyDTO.getId(),
                companyDTO.getUuid(),
                companyDTO.getName(),
                companyDTO.getOrgnr(),
                companyDTO.getParentOrgnr(),
                companyDTO.getPublicName(),
                companyDTO.getOrgform(),
                companyDTO.getEmployees());
        if (companyDTO.getLocationList() != null) {
            company.buildLocations(companyDTO.getLocationList().stream().map(LocationMapper::fromDTO).collect(Collectors.toList()));
        }
        company.setCreated(companyDTO.getCreated());
        companyDTO.getProperties().forEach((name, value) ->
                company.putProperty(PropertyNames.valueOf(name), value));
        companyDTO.getMediaList().forEach(dto -> company.addMedia(dto.getFilename(), dto.getMediaLink()));
        companyDTO.getContactList().forEach(dto -> company.addContact(dto.getName(), dto.getEmail(),
                dto.getPhone(), dto.getRole(), dto.getTitle()));

        company.setUpdated(companyDTO.getUpdated());
        return company;
    }

    public static List<CompanyDTO> toDTOList(List<Company> companies) {
        return companies.stream()
                .map(CompanyMapper::toDTO)
                .collect(Collectors.toList());
    }

}
