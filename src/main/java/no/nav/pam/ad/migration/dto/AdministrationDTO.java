package no.nav.pam.ad.migration.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdministrationDTO {

    private String status;
    private String comments;
    private String reportee;
    private List<String> remarks = new ArrayList<>();
    private String navIdent;

    public AdministrationDTO() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getReportee() {
        return reportee;
    }

    public void setReportee(String reportee) {
        this.reportee = reportee;
    }

    public List<String> getRemarks() {
        return remarks;
    }

    public void setRemarks(List<String> remarks) {
        if (remarks != null) {
            this.remarks = remarks;
        }
    }

    public String getNavIdent() {
        return navIdent;
    }

    public void setNavIdent(String navIdent) {
        this.navIdent = navIdent;
    }
}
