package org.jproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.jproject.domain.base.IBaseEntity;

import java.util.List;

@Entity
@Table(name = "LINK")
public class TLink implements IBaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lnk_seq")
    @SequenceGenerator(name = "lnk_seq", sequenceName = "lnk_seq", allocationSize = 1)
    @Column(name = "lnk_id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fle_fle_id")
    private TFile file;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "lnk_lnk_id", referencedColumnName = "lnk_id", nullable = false, insertable=false, updatable=false)
    private List<TLinkHist> linkHistList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TLink that = (TLink) o;

        return new EqualsBuilder()
                .append(getId(), that.getId())
                .isEquals();
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public TFile getFile() {
        return file;
    }

    public void setFile(TFile file) {
        this.file = file;
    }

    public List<TLinkHist> getLinkHistList() {
        return linkHistList;
    }

    public void setLinkHistList(List<TLinkHist> linkHistList) {
        this.linkHistList = linkHistList;
    }
}
