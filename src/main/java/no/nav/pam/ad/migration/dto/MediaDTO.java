package no.nav.pam.ad.migration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Basic DTO for media content, which contains a filename (with extension) and a media link URL for accessing the media
 * content.
 */
public class MediaDTO extends IdentificationDTO {

    @JsonProperty(required = true)
    @Size(max = 255)
    private String filename;

    @JsonProperty(required = true)
    @Size(max = 512)
    private String mediaLink;

    public MediaDTO() {
    }

    @Override
    public MediaDTO setId(Long id) {
        return (MediaDTO) super.setId(id);
    }

    public String getFilename() {
        return filename;
    }

    public MediaDTO setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public String getMediaLink() {
        return mediaLink;
    }

    public MediaDTO setMediaLink(String reference) {
        this.mediaLink = reference;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MediaDTO)) {
            return false;
        }
        MediaDTO other = (MediaDTO) o;
        return other.getFilename().equals(this.getFilename()) && other.getMediaLink().equals(this.getMediaLink());
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename, mediaLink);
    }

}
