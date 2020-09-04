package no.nav.pam.ad.migration.mapper;

import no.nav.pam.ad.migration.dto.MediaDTO;
import no.nav.pam.ad.migration.entity.Media;

import java.util.List;
import java.util.stream.Collectors;

class MediaMapper {

    static MediaDTO toDTO(Media media) {

        return new MediaDTO()
                .setId(media.getId())
                .setFilename(media.getFilename())
                .setMediaLink(media.getMediaLink());

    }

    static List<MediaDTO> toDTOList(List<Media> mediaList) {

        return mediaList.stream()
                .map(MediaMapper::toDTO)
                .collect(Collectors.toList());

    }

}
