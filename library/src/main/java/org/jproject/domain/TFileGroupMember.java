package org.jproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "FILE_GROUP_MEMBER")
@SQLRestriction("now() between start_date and end_date and start_date < end_date") // TODO подумать как сделать более универсально
public class TFileGroupMember extends AbstractHistEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flgm_seq")
    @SequenceGenerator(name = "flgm_seq", sequenceName = "flgm_seq", allocationSize = 1)
    @Column(name = "flgm_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fleg_fleg_id")
    private TFileGroup fileGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fle_fle_id")
    private TFile file;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TFileGroupMember that = (TFileGroupMember) o;

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

    public TFileGroup getFileGroup() {
        return fileGroup;
    }

    public void setFileGroup(TFileGroup fileGroup) {
        this.fileGroup = fileGroup;
    }

    public TFile getFile() {
        return file;
    }

    public void setFile(TFile file) {
        this.file = file;
    }
}
