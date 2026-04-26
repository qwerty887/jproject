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

@Entity
@Table(name = "PROCESS_LOCK")
public class TProcessLock implements IBaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prlk_seq")
    @SequenceGenerator(name = "prlk_seq", sequenceName = "prlk_seq", allocationSize = 1)
    @Column(name = "prlk_id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prcd_prcd_id")
    private TProcess process;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TProcessLock that = (TProcessLock) o;

        return new EqualsBuilder()
                .append(getId(), that.getId())
                .isEquals()
                && getProcess().equals(that.getProcess());
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public TProcess getProcess() {
        return process;
    }

    public void setProcess(TProcess process) {
        this.process = process;
    }
}
