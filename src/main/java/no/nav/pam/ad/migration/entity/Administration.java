package no.nav.pam.ad.migration.entity;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Administration")
@SequenceGenerator(name = "ADMINISTRATION_SEQ", sequenceName = "ADMINISTRATION_SEQ", allocationSize = 1)
public class Administration {

    @Id
    @GeneratedValue(generator = "ADMINISTRATION_SEQ")
    private Long id;

    @Enumerated(EnumType.STRING)
    private AdministrationStatus status = AdministrationStatus.RECEIVED;

    private String comments;

    @Deprecated
    @Column(name = "OFFICERNAME")
    private String officerName;

    @Column(name = "REPORTEE")
    private String reportee;

    @Column(name = "REMARKS")
    @Convert(converter = RemarkTypeListConverter.class)
    private List<RemarkType> remarks = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "PAMENTITY_ID")
    private PAMEntity pamEntity;

    @Column(name="NAVIDENT")
    private String navIdent;

    public Administration() {
    }

    public Administration(AdministrationStatus status, String comments, String reportee, List<RemarkType> remarks,
                          PAMEntity pamEntity, String navIdent) {
        this.status = status;
        this.comments = comments;
        this.reportee = reportee;
        this.remarks = remarks;
        this.pamEntity = pamEntity;
        // backward compatible just incase
        this.officerName = reportee;
        this.navIdent = navIdent;
    }

    public AdministrationStatus getStatus() {
        return status;
    }

    public String getComments() {
        return comments;
    }

    public String getReportee() {
        return (reportee!=null)  ? reportee : officerName;
    }

    public List<RemarkType> getRemarks() {
        return remarks;
    }

    public String getNavIdent() {
        return navIdent;
    }

    public static class Builder {
        private AdministrationStatus status = AdministrationStatus.RECEIVED;;
        private String comments;
        private String reportee;
        private List<RemarkType> remarks = new ArrayList<>();
        private PAMEntity pamEntity;
        private String navIdent;

        public Builder() {

        }

        public Builder(Administration a) {
            this.status = a.status;
            this.comments = a.comments;
            this.reportee = a.reportee;
            this.remarks = a.remarks;
            this.navIdent = a.navIdent;
        }

        public Builder status(AdministrationStatus status) {
            this.status = status;
            return this;
        }

        public Builder comments(String comments) {
            this.comments = comments;
            return this;
        }

        public Builder reportee(String reportee) {
            this.reportee = reportee;
            return this;
        }

        public Builder remarks(List<RemarkType> remarks) {
            this.remarks = remarks;
            return this;
        }

        public Builder navIdent(String navIdent) {
            this.navIdent = navIdent;
            return this;
        }

        public Administration build(PAMEntity pamEntity) {
            return new Administration(status,comments, reportee,remarks,pamEntity,navIdent);
        }

    }
}
