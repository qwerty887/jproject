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
import org.jproject.domain.base.AbstractHistEntity;

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
    @JoinColumn(name = "lnk_lnk_id")
    private TLink link;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flgm_flgm_id")
    private TFileGroupMember fileGroupMember;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TLinkHist that = (TLinkHist) o;

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

    public TLink getLink() {
        return link;
    }

    public void setLink(TLink link) {
        this.link = link;
    }

    public TFileGroupMember getFileGroupMember() {
        return fileGroupMember;
    }

    public void setFileGroupMember(TFileGroupMember fileGroupMember) {
        this.fileGroupMember = fileGroupMember;
    }
}
