package org.jproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "LINK_HIST")
@SQLRestriction("now() between start_date and end_date and start_date < end_date") // TODO подумать как сделать более универсально
public class TLinkHist extends AbstractHistEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lnkh_seq")
    @SequenceGenerator(name = "lnkh_seq", sequenceName = "lnkh_seq", allocationSize = 1)
    @Column(name = "lnkh_id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lnk_lnk_id", nullable = false)
    private TLink link;

    @Column(name = "path", nullable = false)
    private String path;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "lnks_lnks_id", nullable = false)
    private ELinkStatus linkStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TLinkHist that = (TLinkHist) o;

        return new EqualsBuilder()
                .append(getId(), that.getId())
                .append(getPath(), that.getPath())
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

    public TLink getLink() {
        return link;
    }

    public TLinkHist setLink(TLink link) {
        this.link = link;
        return this;
    }

    public String getPath() {
        return path;
    }

    public TLinkHist setPath(String path) {
        this.path = path;
        return this;
    }

    public ELinkStatus getLinkStatus() {
        return linkStatus;
    }

    public TLinkHist setLinkStatus(ELinkStatus linkStatus) {
        this.linkStatus = linkStatus;
        return this;
    }
}
