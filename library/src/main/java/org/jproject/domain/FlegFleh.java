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
@Table(name = "FLEG_FLEH")
public class FlegFleh implements IBaseEntity<FlegFleh.PK> {

    @Embeddable
    public static class PK implements Serializable {

        @Column(name = "fleg_fleg_id")
        private Integer flegFlegId;

        @Column(name = "fleh_fleh_id")
        private Integer flehFlehId;

        public PK() {
        }

        public PK (Integer flegFlegId, Integer flehFlehId) {
            this.flegFlegId = flegFlegId;
            this.flehFlehId = flehFlehId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            PK pk = (PK) o;

            return new EqualsBuilder()
                    .append(flegFlegId, pk.flegFlegId)
                    .append(flehFlehId, pk.flehFlehId)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(flegFlegId)
                    .append(flehFlehId)
                    .toHashCode();
        }

        public Integer getFlegFlegId() {
            return this.flegFlegId;
        }

        public void setFlegFlegId(Integer flegFlegId) {
            this.flegFlegId = flegFlegId;
        }

        public Integer getFlehFlehId() {
            return this.flehFlehId;
        }

        public void setFlehFlehId(Integer flehFlehId) {
            this.flehFlehId = flehFlehId;
        }
    }

    @EmbeddedId
    private PK id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fleg_fleg_id", nullable = false, insertable=false, updatable=false)
    private TFileGroup fileGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fleh_fleh_id", nullable = false, insertable=false, updatable=false)
    private TFileHist fileHist;

    @Override
    public PK getId() {
        return id;
    }

    @Override
    public void setId(PK id) {
        this.id = id;
    }

    public TFileGroup getFileGroup() {
        return fileGroup;
    }

    public void setFileGroup(TFileGroup fileGroup) {
        this.fileGroup = fileGroup;
    }

    public TFileHist getFileHist() {
        return fileHist;
    }

    public void setFileHist(TFileHist fileHist) {
        this.fileHist = fileHist;
    }
}
