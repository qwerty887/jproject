package org.jproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

@Entity
@Table(name = "FLEH_LNK")
public class FlehLnk implements IBaseEntity<FlehLnk.PK> {

    @Embeddable
    public static class PK implements Serializable {

        @Column(name = "fleh_fleh_id")
        private Integer flehFlehId;

        @Column(name = "lnk_lnk_id")
        private Integer lnkLnkId;

        public PK() {
        }

        public PK (Integer flehFlehId, Integer lnkLnkId) {
            this.flehFlehId = flehFlehId;
            this.lnkLnkId = lnkLnkId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            PK pk = (PK) o;

            return new EqualsBuilder()
                    .append(flehFlehId, pk.flehFlehId)
                    .append(lnkLnkId, pk.lnkLnkId)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(flehFlehId)
                    .append(lnkLnkId)
                    .toHashCode();
        }

        public Integer getFlehFlehId() {
            return this.flehFlehId;
        }

        public void setFlehFlehId(Integer flehFlehId) {
            this.flehFlehId = flehFlehId;
        }

        public Integer getLnkLnkId() {
            return lnkLnkId;
        }

        public void setLnkLnkId(Integer lnkLnkId) {
            this.lnkLnkId = lnkLnkId;
        }
    }

    @EmbeddedId
    private PK id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fleh_fleh_id", nullable = false, insertable=false, updatable=false)
    private TFileHist fileHist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lnk_lnk_id", nullable = false, insertable=false, updatable=false)
    private TLink link;

    @Override
    public PK getId() {
        return id;
    }

    @Override
    public void setId(PK id) {
        this.id = id;
    }

    public TFileHist getFileHist() {
        return fileHist;
    }

    public void setFileHist(TFileHist fileHist) {
        this.fileHist = fileHist;
    }

    public TLink getLink() {
        return link;
    }

    public void setLink(TLink link) {
        this.link = link;
    }
}
