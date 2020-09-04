package no.nav.pam.ad.migration.mapper;



import no.nav.pam.ad.migration.dto.AdDTO;
import no.nav.pam.ad.migration.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public class AdMapper {

    private AdMapper() {
    }

    public static AdDTO toDTO(Ad ad) {
        AdDTO adDTO = new AdDTO();
        adDTO.setId(ad.getId());
        adDTO.setUuid(ad.getUuid());
        adDTO.setUpdatedBy(ad.getUpdatedBy());
        adDTO.setUpdated(ad.getUpdated());
        adDTO.setCreatedBy(ad.getCreatedBy());
        adDTO.setCreated(ad.getCreated());
        adDTO.setMediaList(MediaMapper.toDTOList(ad.getMediaListImmutable()));
        adDTO.setContactList(ContactMapper.from(ad));
        ad.getLocation().ifPresent(loc -> adDTO.setLocation(LocationMapper.toDTO(loc)));
        adDTO.setLocationList(ad.getLocationListImmutable().stream().map(LocationMapper::toDTO).collect(Collectors.toList()));
        adDTO.setTitle(ad.getTitle());
        ad.getEmployer().ifPresent(employer -> {
            adDTO.setEmployer(CompanyMapper.toDTO(employer));
            // fallback compatible
            adDTO.setBusinessName(employer.getName());
        });
        if (ad.getBusinessName()!=null) {
            adDTO.setBusinessName(ad.getBusinessName());
        }
        adDTO.setStatus(ad.getStatus().name());
        ad.getAdministration().ifPresent(administration -> adDTO.setAdministration(AdministrationMapper.toDTO(administration)));
        adDTO.setCategoryList(CategoryMapper.toDTOList(ad.getCategoryList()));
        adDTO.setExpires(ad.getExpires());
        ad.getPropertiesImmutable().forEach((name, value) -> adDTO.putProperties(name.name(), value));
        adDTO.setPublished(ad.getPublished());
        adDTO.setPrivacy(ad.getPrivacy().name());
        adDTO.setSource(ad.getSource());
        adDTO.setMedium(ad.getMedium());
        adDTO.setReference(ad.getReference());

        adDTO.setPublishedByAdmin(ad.getPublishedByAdmin());
        if (ad.getPublishedByAdmin()!=null) {
            adDTO.setFirstPublished(true);
        }
        adDTO.setDeactivatedByExpiry(ad.deactivatedByExpiry());
        adDTO.setActivationOnPublishingDate(ad.activationOnPublishingDate());

        return adDTO;
    }

    public static List<AdDTO> toDTOList(List<Ad> adList) {
        return adList.stream().map(AdMapper::toDTO).collect(Collectors.toList());
    }

    public static Ad fromDTO(AdDTO dto, Company company, List<Category> categories) {
        Ad target = new Ad(dto.getId(),
                dto.getUuid(),
                dto.getTitle(),
                dto.getSource(),
                dto.getMedium(),
                dto.getReference(),
                company,
                dto.getStatus() != null ? AdStatus.valueOf(dto.getStatus()) : AdStatus.INACTIVE
        );
        target.setCreated(dto.getCreated());
        target.setExpires(dto.getExpires());
        target.setUpdated(dto.getUpdated());
        target.setPublished(dto.getPublished());
        if (dto.getUpdatedBy() != null) {
            target.setUpdatedBy(dto.getUpdatedBy());
        }
        if (dto.getCreatedBy() != null) {
            target.setCreatedBy(dto.getCreatedBy());
        }
        dto.getMediaList().forEach(media -> target.addMedia(media.getFilename(), media.getMediaLink()));
        // Only read data from new 'locationList' if old 'location' field is explicitly not set or null (otherwise stay backwards compatible)
        if (dto.getLocationList() != null) {
            target.buildLocations(dto.getLocationList().stream().map(LocationMapper::fromDTO).collect(Collectors.toList()));
        }
        if (dto.getPrivacy() != null){
            target.setPrivacy(PrivacyChannel.valueOf(dto.getPrivacy()));
        }
        if (dto.getAdministration() != null) {
            target.buildAdministration(AdministrationMapper.fromDTO(dto.getAdministration()));
        }
        if (categories!=null) {
            target.setCategoryList(categories);
        }

        dto.getProperties().forEach((name, value) -> {
            PropertyNames propertyName = PropertyNames.valueOf(name);
            String propertyValue = value;
            target.putProperty(propertyName, propertyValue);
        });

        dto.getContactList().forEach(contactDTO -> target.addContact(contactDTO.getName(), contactDTO.getEmail(),
                contactDTO.getPhone(), contactDTO.getRole(), contactDTO.getTitle()));
        target.setPublishedByAdmin(dto.getPublishedByAdmin());
        target.setBusinessName(dto.getBusinessName());
        return target;
    }


}
