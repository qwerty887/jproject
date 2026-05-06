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
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "FILE_GROUP_HIST")
@SQLRestriction("now() between start_date and end_date and start_date < end_date") // TODO подумать как сделать более универсально
public class TFileGroupHist extends AbstractHistEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flgh_seq")
    @SequenceGenerator(name = "flgh_seq", sequenceName = "flgh_seq", allocationSize = 1)
    @Column(name = "flgh_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fleg_fleg_id", nullable = false)
    private TFileGroup fileGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fleh_fleh_id", nullable = false)
    private TFileHist fileHist;

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

    public TFileHist getFileHist() {
        return fileHist;
    }

    public void setFileHist(TFileHist fileHist) {
        this.fileHist = fileHist;
    }
}
