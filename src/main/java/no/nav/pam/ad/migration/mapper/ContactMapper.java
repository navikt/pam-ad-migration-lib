package no.nav.pam.ad.migration.mapper;


import no.nav.pam.ad.migration.dto.ContactDTO;
import no.nav.pam.ad.migration.entity.PAMEntity;
import no.nav.pam.ad.migration.entity.PropertyNames;

import java.util.List;
import java.util.stream.Collectors;

class ContactMapper {

    static List<ContactDTO> from(PAMEntity entity) {

        List<ContactDTO> mapped = entity.getContactListImmutable().stream()
                .map(contact -> new ContactDTO()
                        .setName(contact.getName())
                        .setEmail(contact.getEmail())
                        .setPhone(contact.getPhone())
                        .setTitle(contact.getTitle())
                        .setRole(contact.getRole()))
                .collect(Collectors.toList());

        // Fallback for old property based contacts.
        if (mapped.isEmpty()) {
            ContactDTO contact = new ContactDTO()
                    .setName(entity.getProperty(PropertyNames.contactperson).orElse(null))
                    .setPhone(entity.getProperty(PropertyNames.contactpersonphone).orElse(null))
                    .setEmail(entity.getProperty(PropertyNames.contactpersonemail).orElse(null))
                    .setTitle(entity.getProperty(PropertyNames.contactpersontitle).orElse(null));
            if (!contact.isEmpty()) {
                mapped.add(contact); // Note that we let the properties used above remain in the entity, since a) the API makes properties immutable, and b) this is the most backwards compatible option.
            }
        }

        return mapped;

    }

}
