package com.publiccms.entities.visit;
// Generated 2021-8-6 14:20:45 by Hibernate Tools 5.4.32.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.publiccms.common.generator.annotation.GeneratorColumn;

/**
 * VisitUrlId generated by hbm2java
 */
@Embeddable
public class VisitUrlId implements java.io.Serializable {

    /**
    * 
    */
    private static final long serialVersionUID = 1L;
    @GeneratorColumn(title = "站点", condition = true)
    private short siteId;
    @GeneratorColumn(title = "访问日期", condition = true)
    private Date visitDate;
    @GeneratorColumn(title = "页面md5")
    private String urlMd5;
    @GeneratorColumn(title = "页面sha")
    private String urlSha;

    public VisitUrlId() {
    }

    public VisitUrlId(short siteId, Date visitDate, String urlMd5, String urlSha) {
        this.siteId = siteId;
        this.visitDate = visitDate;
        this.urlMd5 = urlMd5;
        this.urlSha = urlSha;
    }

    @Column(name = "site_id", nullable = false)
    public short getSiteId() {
        return this.siteId;
    }

    public void setSiteId(short siteId) {
        this.siteId = siteId;
    }

    @Column(name = "visit_date", nullable = false, length = 10)
    public Date getVisitDate() {
        return this.visitDate;
    }

    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

    @Column(name = "url_md5", nullable = false, length = 50)
    public String getUrlMd5() {
        return this.urlMd5;
    }

    public void setUrlMd5(String urlMd5) {
        this.urlMd5 = urlMd5;
    }

    @Column(name = "url_sha", nullable = false, length = 100)
    public String getUrlSha() {
        return this.urlSha;
    }

    public void setUrlSha(String urlSha) {
        this.urlSha = urlSha;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof VisitUrlId))
            return false;
        VisitUrlId castOther = (VisitUrlId) other;

        return (this.getSiteId() == castOther.getSiteId())
                && ((this.getVisitDate() == castOther.getVisitDate()) || (this.getVisitDate() != null
                        && castOther.getVisitDate() != null && this.getVisitDate().equals(castOther.getVisitDate())))
                && ((this.getUrlMd5() == castOther.getUrlMd5()) || (this.getUrlMd5() != null && castOther.getUrlMd5() != null
                        && this.getUrlMd5().equals(castOther.getUrlMd5())))
                && ((this.getUrlSha() == castOther.getUrlSha()) || (this.getUrlSha() != null
                        && castOther.getUrlSha() != null && this.getUrlSha().equals(castOther.getUrlSha())));
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getSiteId();
        result = 37 * result + (getVisitDate() == null ? 0 : this.getVisitDate().hashCode());
        result = 37 * result + (getUrlMd5() == null ? 0 : this.getUrlMd5().hashCode());
        result = 37 * result + (getUrlSha() == null ? 0 : this.getUrlSha().hashCode());
        return result;
    }

}