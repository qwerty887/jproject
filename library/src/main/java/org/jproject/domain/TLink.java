package org.jproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "LINK")
@SQLRestriction("del_date is null")
public class TLink extends AbstractDeleteEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lnk_seq")
    @SequenceGenerator(name = "lnk_seq", sequenceName = "lnk_seq", allocationSize = 1)
    @Column(name = "lnk_id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fle_fle_id", nullable = false)
    private TFile file;

    // TODO ошибка. Сделать так же как с файлами
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lnk_id", referencedColumnName = "lnk_lnk_id", nullable = false)
    private TLinkHist linkHist;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TLink that = (TLink) o;

        return new EqualsBuilder()
                .append(getId(), that.getId())
                .append(getDelDate(), that.getDelDate())
                .isEquals()
                && that.getFile().equals(getFile());
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

    public TLink setFile(TFile file) {
        this.file = file;
        return this;
    }

    public TLinkHist getLinkHist() {
        return linkHist;
    }

    public TLink setLinkHist(TLinkHist linkHist) {
        this.linkHist = linkHist;
        return this;
    }
}
