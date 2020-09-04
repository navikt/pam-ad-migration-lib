package no.nav.pam.ad.migration.entity;

import javax.persistence.*;

@Entity
@Table(name = "MEDIA")
@SequenceGenerator(name = "MEDIA_SEQ", sequenceName = "MEDIA_SEQ", allocationSize = 1)
public class Media {

    @Id
    @GeneratedValue(generator = "MEDIA_SEQ")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "PAMENTITY_ID", nullable = false)
    private PAMEntity pamEntity;

    @Column(name = "MEDIALINK")
    private String mediaLink;
    private String filename;

    public Media() {
    }

    public Media(PAMEntity pam, String filename, String mediaLink) {
        this.pamEntity = pam;
        this.filename = filename;
        this.mediaLink = mediaLink;
    }

    public Long getId() {
        return id;
    }

    public String getMediaLink() {
        return mediaLink;
    }

    public String getFilename() {
        return filename;
    }

}
