package org.jproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;

@MappedSuperclass
@SQLRestriction("del_date is null")
public abstract class AbstractDeleteEntity<K> implements IBaseEntity<K>  {

    @Column(name = "del_date")
    private Instant delDate;

    public Instant getDelDate() {
        return delDate;
    }

    public void setDelDate(Instant delDate) {
        this.delDate = delDate;
    }
}
