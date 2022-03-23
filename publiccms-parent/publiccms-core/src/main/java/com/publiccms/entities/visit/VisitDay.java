package com.publiccms.entities.visit;
// Generated 2021-1-14 22:33:12 by Hibernate Tools 6.0.0-SNAPSHOT

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.publiccms.common.generator.annotation.GeneratorColumn;

/**
 * VisitDay generated by hbm2java
 */
@Entity
@Table(name = "visit_day")
@DynamicUpdate
public class VisitDay implements java.io.Serializable {

    /**
    *
    */
    private static final long serialVersionUID = 1L;

    @GeneratorColumn(title = "ID")
    private VisitDayId id;
    @GeneratorColumn(title = "PV")
    private long pv;
    @GeneratorColumn(title = "UV")
    private Long uv;
    @GeneratorColumn(title = "IP Views")
    private Long ipviews;

    public VisitDay() {
    }

    public VisitDay(VisitDayId id, int pv) {
        this.id = id;
        this.pv = pv;
    }

    public VisitDay(short siteId, Date visitDate, byte visitHour, long pv, Long uv, Long ipviews) {
        this.id = new VisitDayId(siteId, visitDate, visitHour);
        this.pv = pv;
        this.uv = uv;
        this.ipviews = ipviews;
    }

    public VisitDay(short siteId, Date visitDate, long pv, Long uv, Long ipviews) {
        this.id = new VisitDayId(siteId, visitDate, (byte) -1);
        this.pv = pv;
        this.uv = uv;
        this.ipviews = ipviews;
    }

    @EmbeddedId

    @AttributeOverrides({ @AttributeOverride(name = "siteId", column = @Column(name = "site_id", nullable = false)),
            @AttributeOverride(name = "visitDate", column = @Column(name = "visit_date", nullable = false, length = 10)),
            @AttributeOverride(name = "visitHour", column = @Column(name = "visit_hour", nullable = false)) })
    public VisitDayId getId() {
        return this.id;
    }

    public void setId(VisitDayId id) {
        this.id = id;
    }

    @Column(name = "pv", nullable = false)
    public long getPv() {
        return this.pv;
    }

    public void setPv(long pv) {
        this.pv = pv;
    }

    @Column(name = "uv")
    public Long getUv() {
        return this.uv;
    }

    public void setUv(Long uv) {
        this.uv = uv;
    }

    @Column(name = "ipviews")
    public Long getIpviews() {
        return this.ipviews;
    }

    public void setIpviews(Long ipviews) {
        this.ipviews = ipviews;
    }

}